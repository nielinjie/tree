package totemPoles.domain

import java.util.UUID

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import totemPoles.domain.framework.Validation.VE
import totemPoles.domain.framework._

import scalaz._
import Scalaz._

class ActionsSpec extends Specification {



  import Properties._
  import Params._
  import ObjT._

  "actions" should {
    "enabled" in new ObjsWithPersonAndTree {

      import JSON._

      actions.enabled(person.id) must beLike {
        case (action: Action) :: Nil =>
          action.`type` must (beEqualTo(Grow.id))
          action.proToVE[UUID]("subject") must beSuccessAndEquals(tree.id)
          action.obj must be_==(person.id)
          action.paraToVE[Range]("amount") must beSuccessAndEquals(framework.Range(1, 100))
      }
    }
    "affected" in new ObjsWithPersonAndTree {
      val act = actions.enabled(person.id).head
      val newAct = act.copy(properties = act.properties.merge(Grow.amount.withValue(30)))
      actions.event(newAct) must beSuccessAnd[Event]({e:Event => e must not beNull})
      val newAct2 = act.copy(properties = act.properties.merge(Grow.amount.withValue(130)))
      actions.event(newAct2) must not(beSuccess)

    }
  }
}
