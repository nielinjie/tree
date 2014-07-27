package totemPoles.domain

import scala.collection.mutable
import java.util.{UUID, Date}
import scala.util.{Success, Try}

trait Obj {
  def id:String
}
trait HasPosition {
  def position:Position
}

trait Item extends Obj

case class Person(id:String,
                  name:String,
                  gift:Int,
                  items:List[Item],
                  lastPower:Int,
                  lastUpdate:Long,`type`:String="person"
                   ) extends Obj {

  def score:Int={
    gift
  }
  def level:Int={
    //max level=100
    //min level=1
    Math.pow(score,0.25).toInt + 1//everyLevelScore= a* level**3 +b
  }
  def visualRange:Int={
    //min vR=11
    level  + 10
  }
  def maxPower:Int={
    //min mP=25
    level + 24
  }
  def power:Int={
    PowerSystem.power(lastPower,lastUpdate,maxPower,new Date().getTime)
  }
}
object PowerSystem {
  def power(lastPower:Int,lastUpdate:Long,maxPower:Int,now:Long):Int={
    val day:Long =( 1000*60*60*24)
    (Math.max((maxPower*(now-lastUpdate) /day )+lastPower,maxPower)).toInt
  }
}
case class Pole(id:String,
                name:String,
                score:Int,
                position:Position,
                items:List[Item],`type`:String="Pole"
               ) extends Obj with HasPosition
object Pole {
  def create(name:String,position:Position):Try[String]={
    Try{
      val id=UUID.randomUUID().toString
      Objs.create(Pole(id,name,0,position,Nil))
      id
    }
  }
}

case class Position(lat:Double,long:Double) {
  def distance(b:Position):Double={
    Geo.distance(lat,long,b.lat,b.long,"K")*1000
  }
}


/*TODO
  1. 压力
  2. 并发
  3. 存储
  4. 可用性
 */
object Objs{
  val map:mutable.Map[String,Obj] =mutable.Map()
  def getObj(id:String):Option[Obj]={
    map.get(id)
  }
  def updateObj(obj:Obj)=map.update(obj.id,obj)
  def create(obj:Obj)={
    map.put(obj.id,obj)
  }
  def findByPosition(po:Position,distance:Double):List[Obj]={
    //TODO cache？
    map.filter {
      case (id,obj) =>
        obj match {
          case p:HasPosition =>
            p.position.distance(po) < distance
          case _ => false
        }
    } .map (_._2).toList
  }
}