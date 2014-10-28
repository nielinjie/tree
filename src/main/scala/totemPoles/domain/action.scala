package totemPoles.domain


import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import org.json4s.JsonAST.{JNull, JString}
import org.json4s.scalaz.JsonScalaz
import totemPoles.domain.Objs.Affected

import scalaz._
import Scalaz._

import java.util.{Date, UUID}

import name.nielinjie.common.UUIDSerializer
import org.json4s._


case class Action(id: UUID, `type`: String, properties: JObject, parameters: JObject, obj: UUID)
case class Range(min:Int,max:Int)

trait ActionType extends Injectable {

  def id: UUID

  val objs: Objs = inject[Objs]

  implicit def s2u(s: String): UUID = UUID.fromString(s)

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

  def find(name: String): Option[ActionType] = {
    acts.find(_.name == name)
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