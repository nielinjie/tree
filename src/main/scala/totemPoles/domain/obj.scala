package totemPoles.domain



import scalaz._
import Scalaz._
import org.json4s.scalaz.JsonScalaz

import java.util.{Date, UUID}

import name.nielinjie.common.UUIDSerializer
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.JsonAST.{JString, JNull, JObject}
import totemPoles.Geo

import scala.collection.mutable

case class Obj(id: UUID, `type`: UUID, name: String, properties: JObject)


trait TypeOps{
  type O =  {def properties: JObject}
  type P =  {def parameters: JObject}
  class Pro[T:JsonScalaz.JSONR](val name:String){

    def validation:O => ValidationNel[String, T]={
      import Objs._
      obj: O =>
        obj.field[T](name)
    }
    def value(t:T): JObject = {
      implicit val formats = Objs.formats
      (name -> Extraction.decompose(t)):JObject
    }
  }
  class ProP[T:JsonScalaz.JSONR](val name:String){

    def validation:P => ValidationNel[String, T]={
      import Objs._
      obj: P =>
        obj.paraField[T](name)
    }
    def value(t:T): JObject = {
      implicit val formats = Objs.formats
      (name -> Extraction.decompose(t)):JObject
    }
  }
  def prop[T:JsonScalaz.JSONR](name: String)=new Pro[T](name)
  def para[T:JsonScalaz.JSONR](name: String)=new ProP[T](name)
}
trait HasOwner {
  self:TypeOps=>
  import Objs._

  val owner=prop[UUID]("owner")
}


trait ObjType extends TypeOps{


  implicit val formats = DefaultFormats + UUIDSerializer

  implicit def s2u(s: String): UUID = UUID.fromString(s)

  def id: UUID

  def name: String

  def validate(affected: JObject): Validation[String, Unit]

  def update(affected: JObject): Validation[String, Unit]


}

object ObjTypes {
  val ots = List() //++ dynamic ones
}


case class Position(lat: Double, long: Double) {
  def distance(b: Position): Double = {
    Geo.distance(lat, long, b.lat, b.long)
  }
}


/*TODO
  1. 压力
  2. 并发
  3. 存储
  4. 可用性
 */

object Objs {
  implicit val formats = DefaultFormats + UUIDSerializer

  implicit class U(s: String) {
    def toUUID: UUID = UUID.fromString(s)
  }

  implicit val uuidJSONR: JsonScalaz.JSONR[UUID] = new JsonScalaz.JSONR[UUID] {
    def read(json: JValue): JsonScalaz.Result[UUID] = {
      json match {
        case JString(s) => UUID.fromString(s).successNel
        case JNull => Success(null)
        case _ => JsonScalaz.UnexpectedJSONError(json, classOf[JString]).failNel
      }
    }

  }
  implicit val rangeJSONR: JsonScalaz.JSONR[Range] = new JsonScalaz.JSONR[Range] {
    def read(json: JValue): JsonScalaz.Result[Range] = {
      json match {
        case _: JObject => Extraction.extract[Range](json).successNel
        case JNull => Success(null)
        case _ => JsonScalaz.UnexpectedJSONError(json, classOf[JString]).failNel
      }
    }

  }

  implicit class F(jo: {def properties: JObject}) {
    def field[T: JsonScalaz.JSONR](name: String): ValidationNel[String, T] = {
      JsonScalaz.field[T](name)(jo.properties).leftMap(_.map(_.toString()))
    }
  }

  implicit class PF(jo: {def parameters: JObject}) {
    def paraField[T: JsonScalaz.JSONR](name: String): ValidationNel[String, T] = {
      JsonScalaz.field[T](name)(jo.parameters).leftMap(_.map(_.toString()))
    }
  }

  type Affected = List[(UUID, JObject)]


}

class Objs {

  import Objs._

  def affect(affected: Affected): ValidationNel[String, Unit] = {
    affected.foldLeft(().successNel[String]) {
      (r: ValidationNel[String, Unit], aff: (UUID, JObject)) =>
        this.getObj(aff._1).fold {
          "can not find".failureNel[Obj]
        } {
          obj: Obj =>
            obj.successNel[String]
        }.map {
          x: Obj =>
            this.updateObj(aff._1, aff._2)
        }
    }
  }


  val map: mutable.Map[UUID, Obj] = mutable.Map()

  def getByOwner(id: UUID, `type`: UUID): List[Obj] = {
    map.values.filter {
      (ob: Obj) =>
        (for {
          owner <- ob.field[UUID]("owner")
        } yield (ob.`type` == `type` && owner == id))
          .getOrElse(false)
    }.toList
  }

  def getObj(id: UUID): Option[Obj] = {
    map.get(id)
  }

  def updateObj(id: UUID, affected: JObject): ValidationNel[String, Unit] = {
    getObj(id).fold {
      "can not find".failureNel[Obj]
    } {
      obj: Obj =>
        obj.successNel[String]
    }
      .map {
      obj: Obj =>
        val newProperties: JObject = obj.properties.merge(affected)
        map.update(id, obj.copy(properties = newProperties))
    }
  }

  def create(obj: Obj) = {
    map.put(obj.id, obj)
  }

  def findByPosition(po: Position, distance: Double): List[Obj] = {
    Positions.distance(po, distance).map(getObj(_)).flatten
  }


}