package totemPoles.domain

import scala.collection.mutable

trait Obj {
  def id:String
  def lastUpdate:Long
}

case class Person(id:String,
                  name:String,
                  lastUpdate:Long,
                  power:Int,
                  visualRange:Int,
                   ) extends Obj
case class Pole(id:String,
                name:String,
                lastUpdate:Long) extends Obj


object Objs{
  val map:mutable.Map[String,Obj] =mutable.Map()
  def getObj(id:String):Option[Obj]={
    map.get(id) match{
      case Some(o) => Some(Actions.replay(o,o.lastUpdate))
      case None =>None
    }
  }
  def create(obj:Obj)={
    map.put(obj.id,obj)
  }
}