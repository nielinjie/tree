package totemPoles.domain

import scala.collection.mutable.ListBuffer
import java.util.Date
import scala.util.{Failure, Try, Success}
object Utils {
  implicit def s2t(msg:String):Throwable={
    new IllegalArgumentException(msg)
  }
}

trait Action {
  def obj: String

  def sub: String

  def affect():Try[String] = ???

  def time: Long

  def validate(): Try[String] = ???
  def id:String
  def `type`:String
}


case class Create(obj: String, sub: String,name:String,poObj:Position, position:Position,time: Long,id:String,`type`:String="create") extends Action {
  import Utils._
  override def affect():Try[String]={
    Pole.create(name,poObj)
  }
  override def validate() ={

    Try{
      Objs.getObj(sub).get.asInstanceOf[Person]
    }.flatMap {
      person:Person =>
      if(poObj.distance(position) < person.visualRange){
        Success("")
      }else{
        Failure("too far")
      }
    }
  }
}

case class Bless(obj: String, sub: String, position:Position,time: Long,id:String,`type`:String="bless") extends Action {
  val costPower=2
  val getScore=10
  val returnGift=10

  override def validate():Try[String]={
    (Objs.getObj(sub),Objs.getObj(obj)) match {
      case (Some(s:Person),Some(o:Pole))=>
        if(s.power<costPower)
          Failure(new IllegalArgumentException( "no enough power"))
        else
          Success("")
      case (None,_) => Failure(new IllegalArgumentException("cannot find obj."))
      case (_,None)=>Failure(new IllegalArgumentException("cannot find sub."))
      case _ =>Failure(new IllegalArgumentException("can not bless."))
    }

  }
  override def affect():Try[String]= {
    val pole=Objs.getObj(obj).get.asInstanceOf[Pole]
    Objs.updateObj(pole.copy(score=pole.score+getScore))
    val person=Objs.getObj(sub).get.asInstanceOf[Person]
    Objs.updateObj(person.copy(
      lastPower = person.power-costPower,
      lastUpdate = new Date().getTime,
      gift = person.gift+returnGift
    ))
    Success("")
    //TODO 记下log，以便必要时收回影响。
  }


}


object Actions {
//  val list: ListBuffer[Action] = new ListBuffer()

  //TODO need to be sync?
  def push(action: Action): Try[String] = {
    action.validate() match {
      case Success(_) => {
        action.affect
        Success(action.id)
      }
      case err => err
    }

  }

//  def replay(obj: Obj, afterTime: Long = 0): Obj = {
//    val subs = list.filter {
//      a: Action =>
//        a.time >= afterTime && a.sub == obj.id
//    }
//    val objs = list.filter {
//      a: Action =>
//        a.time >= afterTime && a.obj == obj.id
//    }
//    objs.foldLeft(subs.foldLeft(obj) {
//      (o: Obj, action: Action) =>
//        action.affectSub(o)
//    }) {
//      (o: Obj, action: Action) =>
//        action.affectObj(o)
//    }
//  }


}