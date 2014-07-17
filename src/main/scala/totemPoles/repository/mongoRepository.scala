package totemPoles.repository

import java.util.UUID

import org.json4s._
import JsonDSL._
import org.json4s.mongo.JObjectParser


import com.mongodb.casbah.{MongoCredential, MongoClient}
import com.mongodb.{ ServerAddress, DBObject}
import org.json4s.ext.JodaTimeSerializers
import com.escalatesoft.subcut.inject.{Injectable, BindingModule}

case class MongoConfig(host: String, port: Int, db: String, auth: Option[(String, String)])


class MongoRepository(val collectionName: String)(implicit val bindingModule: BindingModule) extends Repository with Injectable{
  val config:MongoConfig = inject [MongoConfig]
  val MongoConfig(host, port, dbName, auth) = config
  val server = new ServerAddress(host, port)
  val mongoClient =
    auth match {
      case Some((user, pass)) =>
        val credential = MongoCredential(user, dbName,pass.toCharArray)
        MongoClient(server, List(credential))

      case None =>
        MongoClient(server)

    }
  val db = mongoClient(dbName)
  val collection = db(collectionName)

  def add(obj: JObject) = {
    implicit val f = DefaultFormats ++JodaTimeSerializers.all
    def uuid = java.util.UUID.randomUUID
    val dbo: JObject = obj ~ ("id" -> uuid.toString)
    collection.insert(JObjectParser.parse(dbo))
    uuid
  }

  def query(query: Option[JObject]) = {
    implicit val f = DefaultFormats++JodaTimeSerializers.all
    query match {
      case None => collection.toList.map {
        JObjectParser.serialize(_).asInstanceOf[JObject]
      }
      case Some(jobj) => collection.find(JObjectParser.parse(jobj)).toList.map {
        JObjectParser.serialize(_).asInstanceOf[JObject]
      }
    }
  }

  def clear = {
    collection.drop()
  }
}