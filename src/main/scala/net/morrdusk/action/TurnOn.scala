package net.morrdusk.action

import net.scala0.schedulr.Job
import net.morrdusk.tellduslive.TelldusLive


class TurnOn(deviceId: Int, telldusInfo: List[String]) extends Job with TelldusLive {
  def execute() {
    if (!turnOn(telldusInfo, deviceId)) {
      println("Failed to turn on device with id " + deviceId)
    }
  }
}