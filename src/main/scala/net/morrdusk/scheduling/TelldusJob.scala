package net.morrdusk.scheduling

import org.quartz.{JobExecutionContext, Job}
import org.slf4j.LoggerFactory
import net.morrdusk.tellduslive.TelldusLive

class TelldusJob extends Job with TelldusLive {
  override val LOG = LoggerFactory.getLogger(getClass)

  def execute(context: JobExecutionContext) {
    val data = context.getJobDetail.getJobDataMap
    val action = data.getString(TelldusJob.ACTION)
    val deviceId = data.getString(TelldusJob.DEVICE_ID)
    val info: List[String] = data.get(TelldusJob.API_KEY).asInstanceOf[List[String]]

    LOG.info("in execute with action={} and deviceId={}", action, deviceId)

    if (action.equals("on")) {
      turnOn(info, deviceId.toInt)
    }
    else if (action.equals("off")) {
      turnOff(info, deviceId.toInt)
    }
    else {
      LOG.error("Unknown action {} for deviceId {}", action, deviceId)
    }
  }
}

object TelldusJob {
  val ACTION = "action"
  val DEVICE_ID = "deviceId"
  val API_KEY = "apiKey"
  val ACCESS_TOKEN = "accessToken"
}