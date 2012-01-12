package net.morrdusk.model

import json.Device
import net.morrdusk.tellduslive.TelldusLive
import net.morrdusk.ApiKey

class DeviceModel extends TelldusLive {
}

object DeviceModel {
  def list(apiKey: ApiKey, accessToken: AccessToken): List[Device] = {
    val dev = new DeviceModel()
    dev.listDevices(apiKey, accessToken)
  }
}