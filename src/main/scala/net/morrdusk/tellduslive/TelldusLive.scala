package net.morrdusk.tellduslive

import dispatch._
import dispatch.oauth._
import dispatch.oauth.OAuth._
import xml.XML

trait TelldusLive {

  def turnOn(info: List[String], id: Int): Boolean = {
    makeRequest(info, id, 1)
  }
  def turnOff(info: List[String], id: Int): Boolean = {
    makeRequest(info, id, 2)
  }

  def requestAccessToken(key: String, secret: String) = {
    val consumer = Consumer(key, secret)
    val http = new Http

    val url = (:/("api.telldus.com") / "oauth").secure

    val requestToken = http(url / "requestToken" <@ (consumer) as_token)

    val params = Map[String, Any]("oauth_token" -> requestToken.value)
    val authorizeUrl = url / "authorize" <<? params <@ (consumer, requestToken)
    println("Please open this url in a web browser: " + authorizeUrl.to_uri)
    println("Press enter when you have authorized the token")
    Console.readLine
    
    http(url / "accessToken" <@ (consumer, requestToken) as_token)
  }

  private

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