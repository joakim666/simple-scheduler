package net.morrdusk.web.controller

import net.morrdusk.ApiKey
import net.morrdusk.model.Event._
import net.morrdusk.scheduling.JobScheduler
import net.morrdusk.web.AuthUser
import org.fusesource.scalate.TemplateEngine
import org.slf4j.LoggerFactory
import org.bson.types.ObjectId
import net.morrdusk.scheduling.JobScheduler
import net.morrdusk.web.AuthUser
import net.morrdusk.ApiKey
import net.morrdusk.model.{DeviceModel, EventDao, Event}

class CommandController(templateEngine: TemplateEngine, apiKey: ApiKey, user: AuthUser) extends Controller(templateEngine) {
  val LOG = LoggerFactory.getLogger(getClass)

  def on(params: Map[String,String]) {
    user.accessToken match {
      case Some(accessToken) => {
        DeviceModel.turnOn(apiKey, accessToken, params("deviceId").toInt)
      }
      case None => {
        "Access token missing"
      }
    }
  }

  def off(params: Map[String,String]) {
    user.accessToken match {
      case Some(accessToken) => {
        DeviceModel.turnOff(apiKey, accessToken, params("deviceId").toInt)
      }
      case None => {
        "Access token missing"
      }
    }
  }
}