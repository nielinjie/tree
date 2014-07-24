package totemPoles.domain

import scala.collection.mutable.ListBuffer

trait Action {
  def obj: String

  def sub: String

  def affectObj(obj: Obj): Obj = {
    obj
  }

  def affectSub(sub: Obj): Obj = {
    sub
  }

  def time: Long

  def validate(): Option[String] = None
}


case class Create(obj: String, sub: String, time: Long) extends Action

case class Bless(obj: String, sub: String, time: Long) extends Action


object Actions {
  val list: ListBuffer[Action] = new ListBuffer()

  //TODO need to be sync?
  def push(action: Action): Option[String] = {
    action.validate() match {
      case None =>
      case err => err
    }

  }

  def replay(obj: Obj, afterTime: Long = 0): Obj = {
    val subs = list.filter {
      a: Action =>
        a.time >= afterTime && a.sub == obj.id
    }
    val objs = list.filter {
      a: Action =>
        a.time >= afterTime && a.obj == obj.id
    }
    objs.foldLeft(subs.foldLeft(obj) {
      (o: Obj, action: Action) =>
        action.affectSub(o)
    }) {
      (o: Obj, action: Action) =>
        action.affectObj(o)
    }
  }


}