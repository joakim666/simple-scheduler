package net.morrdusk.web.controller

import scala.collection.mutable.HashMap
import org.fusesource.scalate.TemplateEngine
import com.mongodb.casbah.commons.MongoDBObject
import org.slf4j.LoggerFactory
import net.morrdusk.model.{Event, EventDao, DeviceModel}
import net.morrdusk.model.json.Device

class DeviceController(templateEngine: TemplateEngine, info: List[String]) extends Controller(templateEngine) {
  val LOG = LoggerFactory.getLogger(getClass)

  def index() {
    val devices = DeviceModel.list(info)

    val deviceEvents = new HashMap[Device, List[Event]]
    devices.foreach(d => {
      val events = EventDao.find(ref = MongoDBObject("deviceId" -> d.id)).toList
      deviceEvents += d -> events
    })

    render("devices/index.jade", Map("devices" -> devices, "deviceEvents" -> deviceEvents))
  }

  def show(id: String) {
    LOG.info("in show")
    val events = EventDao.find(ref = MongoDBObject("deviceId" -> id.toLong)).toList
    LOG.info("events: {}", events)
    render("devices/show.jade", Map("deviceId" -> id,
                                     "events" -> events))
  }

  // deprecated, should use EventController.create instead
  def save(params: Map[String,String]) {
    LOG.info("in save")
    LOG.info("id: {}", params("id"))
    LOG.info("action: {}", params("action"))
    LOG.info("cron: {}", params("cron"))

    val event = Event(deviceId = params("id").toLong, action = params("action"), cron = params("cron"))
    EventDao.insert(event)

    show(params("id"))
  }

}