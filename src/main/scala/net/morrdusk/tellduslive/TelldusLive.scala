package net.morrdusk.tellduslive

import dispatch._
import dispatch.oauth._
import dispatch.oauth.OAuth._
import xml.{Elem, XML}
import org.slf4j.LoggerFactory
import com.codahale.jerkson.Json._
import net.morrdusk.model.json.{DeviceList, Device}
import net.morrdusk.ApiKey
import net.morrdusk.model.AccessToken

trait TelldusLive {
  val LOG = LoggerFactory.getLogger(getClass)

  def turnOn(info: List[String], id: Int): Boolean = {
    makeRequest(info, id, 1)
  }
  def turnOff(info: List[String], id: Int): Boolean = {
    makeRequest(info, id, 2)
  }

  def listDevices(apiKey: ApiKey, accessToken: AccessToken): List[Device] = {
    LOG.info("listDevices: {} {}", apiKey, accessToken)

    makeDeviceRequest(apiKey, accessToken, "list")
  }

  def makeAuthorizeUrl(apiKey: ApiKey, callback: String) = {
    val consumer = Consumer(apiKey.key, apiKey.secret)
    val http = new Http

    val url = (:/("api.telldus.com") / "oauth").secure

    val requestToken = http(url / "requestToken" <@ (consumer, callback) as_token)

    val params = Map[String, Any]("oauth_token" -> requestToken.value)
    val authorizeUrl = url / "authorize" <<? params <@ (consumer, requestToken)

    (requestToken, authorizeUrl.to_uri.toString)
  }

  def getAccessToken(apiKey: ApiKey, requestToken: Token) = {
    val consumer = Consumer(apiKey.key, apiKey.secret)
    val http = new Http

    val url = (:/("api.telldus.com") / "oauth").secure
    http(url / "accessToken" <@ (consumer, requestToken) as_token)
  }

  private

  def makeDeviceRequest(apiKey: ApiKey, accessToken: AccessToken, command: String): List[Device] = {
    LOG.info("makeDeviceRequest")
    val consumer = Consumer(apiKey.key, apiKey.secret)
    val access_token = Token(accessToken.value, accessToken.secret)
    val http = new Http

    val url = (:/("api.telldus.com") / "json").secure
    val res = http(url / "devices" / command <@ (consumer, access_token) as_str)
    LOG.info("res: {}", res)
    val deviceList = parse[DeviceList](res)
    LOG.info("parsed devices: {}", deviceList)
    deviceList.device
  }

  def makeRequest(info: List[String], id: Int, method: Int): Boolean = {
    val consumer = Consumer(info(0), info(1))
    val access_token = Token(info(2), info(3))
    val http = new Http

    val url = (:/("api.telldus.com") / "xml").secure
    val params = Map[String, Any]("id" -> id, "method" -> method)
    val res = http(url / "devices" / "command" <<? params <@ (consumer, access_token) as_str)
    val xml = XML.loadString(res)
    val text = (xml \\ "status").text.trim
    text == "success"
  }
}