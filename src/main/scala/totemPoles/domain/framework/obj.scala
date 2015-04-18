package totemPoles.domain.framework

import java.util.UUID

import org.json4s.JsonAST.JObject
import totemPoles.domain.framework.Validation._

import scala.collection.mutable
import scalaz.Scalaz._
import scalaz._


case class Obj(id: UUID, `type`: String, name: String, properties: JObject) extends HasProperties


trait HasOwner {
  self: TypeHelper =>
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


class Objs {

  import Properties._


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

  def updateObj(id: UUID, affected: List[AffectPro[_]]): VE[ Unit] = {
    getObj(id).toSuccessE(s"not find obj - $id")
      .map {
      obj: Obj =>
        val a:List[VE[JObject]] =  affected.map(_.field(obj))
        val aff:VE[List[JObject]] = a.
          foldLeftM[VE,List[JObject]](List.empty[JObject])({
          (l:List[JObject],b:VE[JObject])=>
            b.map(_ :: l)
        })
        aff.map({
          af:List[JObject]=>
            val newProperties: JObject = af.foldLeft(obj.properties)(_.merge(_))
            map.update(id, obj.copy(properties = newProperties))
        })
    }
  }

  def create(obj: Obj) = {
    map.put(obj.id, obj)
  }

  def findByPosition(po: Position, distance: Double): List[Obj] = {
    Positions.distance(po, distance).map(getObj).flatten
  }

  def objWithType(obj: UUID, typeId: String): VE[Obj] = getObj(obj) match {
    case None => "obj not find".left
    case Some(t) if t.`type` == typeId => t.right
    case _ => s"obj type must has type -  '$typeId'".left
  }
}