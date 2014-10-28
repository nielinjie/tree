package totemPoles.domain


import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import totemPoles.domain.Objs.Affected

import scalaz._
import Scalaz._

import java.util.{Date, UUID}

import name.nielinjie.common.UUIDSerializer
import org.json4s._


case class Action(id: UUID, `type`: UUID, properties: JObject, parameters: JObject, obj: UUID)
case class Range(min:Int,max:Int)

trait HasSub {
  self:TypeOps=>
  import Objs._
  val sub=prop[UUID]("sub")

}


trait ActionType extends TypeOps with Injectable {

  def id: UUID

  val objs: Objs = inject[Objs]



  implicit val formats = DefaultFormats + UUIDSerializer


  def name: String


  def apply(act: Action): ValidationNel[String, Affected]

  def enabled(obj: UUID): List[Action]

  def objWithType(obj: UUID, typeId: UUID): ValidationNel[String, Obj] = objs.getObj(obj) match {
    case None => ("obj not find").failureNel
    case Some(t) if t.`type` == typeId => (t).successNel
    case _ => (s"obj type must be ${typeId}").failureNel
  }



  def sure(validate: => Boolean, message: String): ValidationNel[String, Unit] = {
    if (validate)
      ().successNel
    else
      message.failureNel
  }


}

object ActionType {

}

class ActionTypes(implicit val bindingModule: BindingModule) extends Injectable {
  val acts = List(new Grow()) // ++ dynamic ones

  def enabled(obj: UUID) = {
    acts.map(_.enabled(obj)).flatten
  }

  def find(id: UUID): Option[ActionType] = {
    acts.find(_.id == id)
  }
}


class Actions(implicit val bindingModule: BindingModule) extends Injectable {
  val objs: Objs = inject[Objs]
  val actionTypes: ActionTypes = inject[ActionTypes]


  def push(action: Action) = {
    actionTypes.find(action.`type`) match {
      case None => ("unknown type").failureNel
      case Some(at) =>
        at.apply(action) match {
          case Success(aff: Affected) => {
            objs.affect(aff)
          }
          case _ =>
        }
      //TODO save snapshot of the world?

    }
  }

  def enabled(obj: UUID): List[Action] = {
    actionTypes.enabled(obj)
  }


}