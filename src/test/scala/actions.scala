package totemPoles.domain

import java.util.UUID

import org.specs2.matcher.{MatchResult, Matcher}
import org.specs2.mutable.Specification

import scalaz.Success

class ActionsSpec extends  Specification{
  object ObjsTest{
    def successAnd[T](fun: T=>MatchResult[T])={
      beLike[T]{
        case Success(it:T)=> fun(it)
      }
    }
    def successAndEquals[T](t:T)=successAnd({
      it:Any =>
        it must_== t
    })
  }
  import Objs._
  import ObjsTest._
  "actions" should {
    "enabled" in new ObjsWithPersonAndTree {
      actions.enabled(person.id) must beLike{
        case (action:Action)::Nil=>
          action.`type` must(beEqualTo(Grow.id))
          action.field[UUID]("sub") must successAndEquals(tree.id)
          action.obj must be_==(person.id)
          action.paraField[Range]("amount") must successAndEquals(Range(1,100))
      }

    }
    "affected" in new ObjsWithPersonAndTree {
      val act=actions.enabled(person.id).head
      val newAct=act.copy(properties=act.properties.merge(Grow.amount.value(30)))
      actions
    }
  }
}