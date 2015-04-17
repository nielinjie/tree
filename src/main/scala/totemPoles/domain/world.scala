package totemPoles.domain

import java.util.UUID

import com.escalatesoft.subcut.inject.{Injectable, BindingModule}
import totemPoles.domain.framework._

import scalaz._
import framework.Validation._

class World(implicit val bindingModule: BindingModule) extends Injectable {
  val actions = inject[Actions]
  implicit val objs = inject[Objs]

  def action(action: Action): ValidationNel[ErrorMessage, Unit] = {
    actions.affected(action).flatMap(onEvent)
  }

  def enabledActions(personId: UUID): List[Action] = actions.enabled(personId)

  def onEvent(event: Event): ValidationNel[ErrorMessage, Unit] = {
    event.vote.flatMap({ _ => event.doIt })
  }

}