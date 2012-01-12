package net.morrdusk.scheduling

import org.quartz.impl.StdSchedulerFactory
import org.quartz._
import org.quartz.CronScheduleBuilder._

import impl.StdSchedulerFactory
import org.quartz.JobBuilder._
import org.quartz.TriggerBuilder._
import org.quartz.SimpleScheduleBuilder._
import org.slf4j.LoggerFactory
import net.morrdusk.ApiKey
import net.morrdusk.model.{AccessToken, Event}


object JobScheduler {
//  implicit def toJobDetail(jb :JobBuilder): JobDetail= jb.build
//  implicit def toTrigger(tb :TriggerBuilder[CronTrigger]):Trigger = tb.build
  implicit def toScheduler (s:Scheduler):RichScheduler = new RichScheduler(s)

  def apply(apiKey: ApiKey): JobScheduler = {
    val scheduler = StdSchedulerFactory.getDefaultScheduler
    +scheduler

    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run() { -scheduler }
    })

    new JobScheduler(scheduler, apiKey)
  }
}

class JobScheduler(scheduler: RichScheduler, apiKey: ApiKey) {
  val LOG = LoggerFactory.getLogger(getClass)

  def schedule(event: Event, accessToken: AccessToken) {
    val s = cronSchedule(event.cron)
    val job = newJob(classOf[TelldusJob]) withIdentity (event.id.toString, event.deviceId.toString)
    val trigger = newTrigger() withIdentity (event.id.toString, event.deviceId.toString) withSchedule s

    val detail: JobDetail = job.build()

    detail.getJobDataMap.put(TelldusJob.ACTION, event.action)
    detail.getJobDataMap.put(TelldusJob.DEVICE_ID, event.deviceId.toString)
    detail.getJobDataMap.put(TelldusJob.API_KEY, apiKey)
    detail.getJobDataMap.put(TelldusJob.ACCESS_TOKEN, accessToken)

    scheduler <-> (detail, trigger.build())
  }

  def unschedule(event: Event) {
    LOG.info("unscheduling event with id: {} and deviceId: {}", event.id, event.deviceId)
    scheduler <<- (new TriggerKey(event.id.toString, event.deviceId.toString))
  }
}
