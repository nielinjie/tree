package totemPoles.domain

import java.util.UUID

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import totemPoles.domain.ObjT._
import totemPoles.domain.framework.Validation.VE
import totemPoles.domain.framework._

import scalaz._
import Scalaz._

class WorldSpec extends Specification {
  "world" should {
    "scenario" in new ObjsWithPersonAndTree {
      //display actions
      val acs = world.enabledActions(person.id)
      acs.size must beEqualTo(1)
      //set action para
      val act = acs.head
      val newa = act.copy(properties = act.properties.merge(Grow.amount.withValue(30)))
      //post action to world
      world.action(newa)
      //check world
      world.obj(person.id) must beSome {
        ob: Obj =>
          Person.pow.getter(ob) must beSuccessAndEquals(BigInt(70))
      }
      world.obj(tree.id) must beSome {
        ob: Obj =>
          Tree.score.getter(ob) must beSuccessAndEquals(BigInt(130))
      }
    }
    "scenarioMonkey" in new ObjsWithPersonAndTree {
      //display actions
      val acs = world.enabledActions(person.id)
      acs.size must beEqualTo(1)
      //set action para
      val act = acs.head
      val newa = act.copy(properties = act.properties.merge(Grow.amount.withValue(30)))
      //post action to world
      world.action(newa)
      //check world
      world.obj(person.id) must beSome {
        ob: Obj =>
          Person.pow.getter(ob) must beSuccessAndEquals(BigInt(70))
      }
      world.obj(tree.id) must beSome {
        ob: Obj =>
          Tree.score.getter(ob) must beSuccessAndEquals(BigInt(130))
      }
    }
  }
}