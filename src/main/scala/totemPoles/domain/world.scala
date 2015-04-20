package totemPoles.domain

import java.util.UUID

import com.escalatesoft.subcut.inject.{Injectable, BindingModule}
import totemPoles.domain.framework._

import scalaz._
import framework.Validation._

class World(implicit val bindingModule: BindingModule) extends Injectable {
  val actions = inject[Actions]
  implicit val objs = inject[Objs]

  def action(action: Action): VE[Unit] = {
    actions.event(action).flatMap(onEvent)
  }

  def enabledActions(personId: UUID): List[Action] = actions.enabled(personId)

  def onEvent(event: Event): VE[Unit] = {
    event.vote.flatMap({ _ => event.doIt })
  }
  def obj(id:UUID) = objs.getObj(id)

}