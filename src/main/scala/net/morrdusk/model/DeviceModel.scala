package net.morrdusk.model

import json.Device
import net.morrdusk.tellduslive.TelldusLive

class DeviceModel extends TelldusLive {
}

object DeviceModel {
  def list(info: List[String]): List[Device] = {
    val dev = new DeviceModel()
    dev.listDevices(info)
  }
}