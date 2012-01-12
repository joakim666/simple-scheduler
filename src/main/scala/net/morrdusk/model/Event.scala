package net.morrdusk.model

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection

case class Event(@Key("_id") id: ObjectId = new ObjectId, userIdentifier: String, deviceId: Long, action: String, cron: String)

object EventDao extends SalatDAO[Event, ObjectId] (
  collection = MongoConnection()("simple-scheduler")("events")
) {
    def findAll() = {
    EventDao.find(ref = MongoDBObject()).toList
  }
}