package net.morrdusk.web.controller

import org.fusesource.scalate.TemplateEngine
import org.slf4j.LoggerFactory
import net.morrdusk.model.{EventDao, Event}
import org.scalatra.util.{MapWithIndifferentAccess, MultiMapHeadView}
import org.bson.types.ObjectId
import net.morrdusk.scheduling.JobScheduler

class EventController(templateEngine: TemplateEngine, info: List[String], scheduler: JobScheduler) extends Controller(templateEngine) {
  val LOG = LoggerFactory.getLogger(getClass)

  def newTemplate(params: Map[String,String]) {
    LOG.info("newTemplate: {}", params)
    render("events/newTemplate.scaml", Map("deviceId" -> params("deviceId"),
                                           "timestamp" -> System.currentTimeMillis().toString))
  }

  def create(params: Map[String,String]) {
    LOG.info("in save")
    LOG.info("id: {}", params("id"))
    LOG.info("action: {}", params("action"))
    LOG.info("cron: {}", params("cron"))

    var cron = "0 " + params("cron") // add 0 as seconds
    if (cron(cron.length()-1) == '*') {
      cron = cron.dropRight(1) + "?"
    }

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