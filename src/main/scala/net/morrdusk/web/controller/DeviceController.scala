package net.morrdusk.web.controller

import scala.collection.mutable.HashMap
import org.fusesource.scalate.TemplateEngine
import com.mongodb.casbah.commons.MongoDBObject
import org.slf4j.LoggerFactory
import net.morrdusk.model.{Event, EventDao, DeviceModel}
import net.morrdusk.model.json.Device
import net.morrdusk.web.AuthUser
import net.morrdusk.ApiKey

class DeviceController(templateEngine: TemplateEngine, apiKey: ApiKey, user: AuthUser) extends Controller(templateEngine) {
  val LOG = LoggerFactory.getLogger(getClass)

  def index() {
    user.accessToken match {
      case Some(accessToken) => {
        val devices = DeviceModel.list(apiKey, accessToken)

        val deviceEvents = new HashMap[Device, List[Event]]
        devices.foreach(d => {
          val events = EventDao.find(ref = MongoDBObject("deviceId" -> d.id)).toList
          deviceEvents += d -> events
        })

        render("devices/index.jade", Map("devices" -> devices, "deviceEvents" -> deviceEvents))
      }
      case None => {
        "Access token missing"
      }
    }
  }

}