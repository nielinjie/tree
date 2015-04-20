package totemPoles.domain

import java.util.UUID

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import totemPoles.domain.framework._

import scalaz._
import Scalaz._

class EventsSpec extends Specification {

  import Properties._
  import Params._
  import ObjT._

  "events" should {

    "vote" in new ObjsWithPersonAndTree {
      val act = actions.enabled(person.id).head
      val newAct = act.copy(properties = act.properties.merge(Grow.amount.withValue(30)))
      val event = actions.event(newAct)
      event must beSuccessAnd({
        e: Event =>
          e.vote(objs) must beSuccess
      })
    }
    "voteNo" in new ObjsWithPersonAndTree {
      val act = actions.enabled(person.id).head
      val newAct = act.copy(properties = act.properties.merge(Grow.amount.withValue(30)))
      val event = actions.event(newAct)

      event must beSuccessAnd({
        e: Event =>

          e.vote(objs) must (beSuccess)
      })
      //
    }
    "update" in new ObjsWithPersonAndTree {
      val act = actions.enabled(person.id).head
      val newAct = act.copy(properties = act.properties.merge(Grow.amount.withValue(30)))
      val event = actions.event(newAct)
      event must beSuccessAnd({
        e: Event =>
          e.vote(objs) must beSuccessAnd({
            _ =>
              e.doIt(objs)
              objs.getObj(person.id) must beSome {
                obj: Obj =>
                  Person.pow.getter(obj) must beSuccessAndEquals(BigInt(70))
              }
              objs.getObj(tree.id) must beSome {
                obj: Obj =>
                  Tree.score.getter(obj) must beSuccessAndEquals(BigInt(130))
              }
          })
      })
    }
  }
}