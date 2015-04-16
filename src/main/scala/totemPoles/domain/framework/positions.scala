package totemPoles.domain.framework

import java.util.UUID

import scala.collection.mutable

object Positions {
  //TODO some high performance repository
  val map: mutable.Map[UUID, Position] = mutable.Map()

  def find(obj: UUID): Option[Position] = {
    map.get(obj)
  }

  def set(obj: UUID, position: Position):Unit = {
    map.update(obj, position)
  }
  def unset(obj:UUID):Unit={
    map.remove(obj)
  }

  def distance(po: Position, distance: Double) :List[UUID]= {
    //TODO some high performance repository
    map.filter {
      case (id, pos) =>
        pos.distance(po) < distance
    }.map(_._1).toList
  }
}