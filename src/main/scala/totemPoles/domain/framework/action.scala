package totemPoles.domain.framework

import java.util.UUID

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import org.json4s.JsonAST.JObject
import org.json4s.scalaz.JsonScalaz
import totemPoles.domain.Grow
import totemPoles.domain.framework.Validation.{VE, ErrorMessage}

import scalaz.Scalaz._
import scalaz._
import org.json4s._

import org.json4s.{DefaultFormats, Extraction}
import org.json4s.JsonAST.JObject
import org.json4s.JsonDSL._


case class Action(id: UUID, `type`: String, properties: JObject, parameters: JObject, obj: UUID) extends HasProperties with HasParameters

case class Range(min: Int, max: Int)

trait HasSub {
  self: TypeHelper =>

  val subject = prop[UUID]("subject")

}


trait ActionType extends TypeHelper with Params {


  def apply(act: Action)(implicit objs: Objs): VE[ Event]

  def enabled(obj: UUID)(implicit objs: Objs): List[Action]

  def sure(validate: => Boolean, message: String): VE[Unit] = {
    if (validate)
      ().right
    else
     message.left
  }


}




class ActionTypes(implicit val bindingModule: BindingModule) extends Injectable {
  val acts: List[ActionType] = List(Grow) // ++ dynamic ones
  implicit val objs:Objs  = inject[Objs]
  def enabled(obj: UUID) = {
    acts.map(_.enabled(obj)).flatten
  }

  def find(id: String): Option[ActionType] = {
    acts.find(_.id == id)
  }
}


class Actions(implicit val bindingModule: BindingModule) extends Injectable {
  implicit  val objs: Objs = inject[Objs]
  val actionTypes: ActionTypes = inject[ActionTypes]


  def affected(action: Action): VE[Event] = {
    actionTypes.find(action.`type`) match {
      case None => "unknown type".left
      case Some(at) =>
        at.apply(action)
    }
  }

  def enabled(obj: UUID): List[Action] = {
    actionTypes.enabled(obj)
  }


}
