package totemPoles.domain.framework

import java.util.UUID
import org.json4s._

import _root_.scalaz.Scalaz._

import name.nielinjie.common.UUIDSerializer
import org.json4s.JsonAST.{JObject, JNull, JString}
import org.json4s._
import org.json4s.ext.JodaTimeSerializers
import org.json4s.scalaz.JsonScalaz
import totemPoles.domain.framework

import _root_.scalaz.Success

/**
 * Created by nielinjie on 4/16/15.
 */
object JSON extends JSON
trait JSON {
  implicit val formats = DefaultFormats + UUIDSerializer ++ JodaTimeSerializers.all
  implicit val uuidJSONR: JsonScalaz.JSONR[UUID] = new JsonScalaz.JSONR[UUID] {
    def read(json: JValue): JsonScalaz.Result[UUID] = {
      json match {
        case JString(s) => UUID.fromString(s).successNel
        case JNull => Success(null)
        case _ => JsonScalaz.UnexpectedJSONError(json, classOf[JString]).failNel
      }
    }

  }
  implicit val rangeJSONR: JsonScalaz.JSONR[framework.Range] = new JsonScalaz.JSONR[framework.Range] {
    def read(json: JValue): JsonScalaz.Result[framework.Range] = {
      json match {
        case _: JObject => Extraction.extract[framework.Range](json).successNel
        case JNull => Success(null)
        case _ => JsonScalaz.UnexpectedJSONError(json, classOf[JString]).failNel
      }
    }

  }
}
