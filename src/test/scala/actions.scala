package totemPoles.domain

import java.util.UUID

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import totemPoles.domain.framework._

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
  import Properties._
  import Params._
  import ObjsTest._
  import JSON._
  "actions" should {
    "enabled" in new ObjsWithPersonAndTree {
      actions.enabled(person.id) must beLike{
        case (action:Action)::Nil=>
          action.`type` must(beEqualTo(Grow.id))
          action.field[UUID]("subject") must successAndEquals(tree.id)
          action.obj must be_==(person.id)
          action.paraField[Range]("amount") must successAndEquals(framework.Range(1,100))
      }

    }
//    "affected" in new ObjsWithPersonAndTree {
//      val act=actions.enabled(person.id).head
////      val newAct=act.copy(properties=act.properties.merge(Grow.amount.value(30)))
////      actions
//    }
  }
}