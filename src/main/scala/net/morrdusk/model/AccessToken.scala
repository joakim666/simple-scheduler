package net.morrdusk.model

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection

case class AccessToken(@Key("_id") identifier: String, value: String, secret: String)

object AccessTokenDao extends SalatDAO[AccessToken, String] (
  collection = MongoConnection()("simple-scheduler")("accesstoken")
) {
}