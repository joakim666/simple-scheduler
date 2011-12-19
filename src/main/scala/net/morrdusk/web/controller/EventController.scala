package net.morrdusk.web.controller

import org.fusesource.scalate.TemplateEngine
import org.slf4j.LoggerFactory
import net.morrdusk.model.{EventDao, Event}
import org.bson.types.ObjectId
import net.morrdusk.scheduling.JobScheduler

class EventController(templateEngine: TemplateEngine, info: List[String], scheduler: JobScheduler) extends Controller(templateEngine) {
  val LOG = LoggerFactory.getLogger(getClass)

  def newTemplate(params: Map[String,String]) {
    LOG.info("newTemplate: {}", params)
    render("events/newTemplate.jade", Map("deviceId" -> params("deviceId"),
                                           "timestamp" -> System.currentTimeMillis().toString))
  }

  def create(params: Map[String,String]) {
    var cron = "0 " + params("cron") // add 0 as seconds
    if (cron(cron.length()-1) == '*') {
      cron = cron.dropRight(1) + "?"
    }

    val split = cron.split(" ")
    // special day-of-week handling
    if (split(5) != "*" && split(5) != "?") {
      split(5) = (split(5).toInt + 1).toString // quartz expects 1-7
      split(3) = "?" // quartz expects ? as day-of-month if day-of-week is set
    }
    cron = split.mkString(" ")

    val event = Event(deviceId = params("id").toLong, action = params("action"), cron = cron)
    EventDao.insert(event)
    scheduler.schedule(event)
    render("events/createdEvent.jade", Map("event" -> event))
  }

  def remove(params: Map[String,String]) {
    val event = EventDao.findOneByID(id = new ObjectId(params("id")))
    event match {
      case None => {}
      case Some(e) => {
        scheduler.unschedule(e)
        EventDao.remove(e)
      }
    }
  }
}