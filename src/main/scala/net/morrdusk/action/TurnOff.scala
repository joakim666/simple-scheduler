package net.morrdusk.action

import net.scala0.schedulr.Job
import net.morrdusk.tellduslive.TelldusLive


class TurnOff(deviceId: Int, telldusInfo: List[String]) extends Job with TelldusLive {
  def execute() {
    if (!turnOff(telldusInfo, deviceId)) {
      println("Failed to turn off device with id " + deviceId)
    }
  }
}