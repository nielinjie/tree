package totemPoles.domain.framework

import java.util.UUID

import org.json4s.JsonAST.JObject

import scala.collection.mutable
import scalaz.Scalaz._
import scalaz._


case class Obj(id: UUID, `type`: String, name: String, properties: JObject)


trait HasOwner {
  self: TypeHelper =>

  import Properties._

  val owner = prop[UUID]("owner")
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


  type Affected = List[(UUID, JObject)]

}

class Objs {

  import Objs._
  import Properties._

  def affect(affected: Affected): ValidationNel[String, Affected] = {
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
    }.map {
      _ =>
        affected
    }
  }


  val map: mutable.Map[UUID, Obj] = mutable.Map()

  def getByOwner(id: UUID, `type`: String): List[Obj] = {
    map.values.filter {
      (ob: Obj) =>
        (for {
          owner <- ob.field[UUID]("owner")
        } yield ob.`type` == `type` && owner == id)
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
    Positions.distance(po, distance).map(getObj).flatten
  }

  def objWithType(obj: UUID, typeId: String): ValidationNel[String, Obj] = getObj(obj) match {
    case None => "obj not find".failureNel
    case Some(t) if t.`type` == typeId => t.successNel
    case _ => s"obj type must has type -  '$typeId'".failureNel
  }
}