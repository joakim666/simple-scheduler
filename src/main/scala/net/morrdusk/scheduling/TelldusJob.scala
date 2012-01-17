package net.morrdusk.scheduling

import org.quartz.{JobExecutionContext, Job}
import org.slf4j.LoggerFactory
import net.morrdusk.tellduslive.TelldusLive
import net.morrdusk.ApiKey
import net.morrdusk.model.AccessToken

class TelldusJob extends Job with TelldusLive {
  override val LOG = LoggerFactory.getLogger(getClass)

  def execute(context: JobExecutionContext) {
    val data = context.getJobDetail.getJobDataMap
    val action = data.getString(TelldusJob.ACTION)
    val deviceId = data.getString(TelldusJob.DEVICE_ID)
    val apiKey = data.get(TelldusJob.API_KEY).asInstanceOf[ApiKey]
    val accessToken = data.get(TelldusJob.ACCESS_TOKEN).asInstanceOf[AccessToken]

    LOG.debug("in execute with action={} and deviceId={}", action, deviceId)

    if (action.equals("on")) {
      turnOn(apiKey, accessToken, deviceId.toInt)
    }
    else if (action.equals("off")) {
      turnOff(apiKey, accessToken, deviceId.toInt)
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