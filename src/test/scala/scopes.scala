package totemPoles.domain

import java.util.{Date, UUID}

import com.escalatesoft.subcut.inject.NewBindingModule
import dispatch.classic.json.JsNull.T
import org.json4s.JsonAST.JObject
import org.specs2.matcher.{MustThrownMatchers, Matcher, MatchResult}
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import org.json4s.JsonDSL._
import totemPoles.domain.framework.Validation.VE
import totemPoles.domain.framework.{Objs, Obj, Actions, ActionTypes}

import scalaz.\/-

object ObjT extends MustThrownMatchers{
  def obj(`type`: String, name: String, properties: JObject): Obj = {
    Obj(UUID.randomUUID(), `type`, name, properties, new Date())
  }
  def beSuccess[T] :Matcher[VE[T]] ={
    beSuccessAnd(_ must not beNull)
  }
  def beSuccessAnd[T](fun: T => MatchResult[_]) : Matcher[VE[T]] = {
    beLike[VE[T]] {
      case \/-(it: T) => fun(it)
    }
  }

  def beSuccessAndEquals[T](t: T) : Matcher[VE[T]]= beSuccessAnd(
    _ must beEqualTo[T]( t)
  )
}

trait EmptyObjs extends Scope {

  class Config extends NewBindingModule(module => {
    import module._
    bind[Objs] toModuleSingle { _ => new Objs() }
    bind[Actions] toModuleSingle { implicit m => new Actions() }
    bind[ActionTypes] toModuleSingle { implicit m => new ActionTypes() }
    bind[World] toModuleSingle{ implicit m => new World() }
  })

  val config = new Config
  val objs: Objs = config.inject[Objs](None)
  val actions: Actions = config.inject[Actions](None)
  val world:World = config.inject[World](None)
}

trait ObjsWithAPerson extends EmptyObjs {

  import ObjT._

  val person = obj(Person.id, "Jason", Person.pow.withValue(100))
  objs.create(person)
}

trait ObjsWithPersonAndTree extends ObjsWithAPerson {

  import ObjT._

  val tree = obj(Tree.id, "Jason", Tree.score.withValue(100) ~ Tree.owner.withValue(person.id))
  objs.create(tree)
}

trait ObjsWithPersonAndSomeTrees extends ObjsWithPersonAndTree {

  import ObjT._

  val tree2 = obj(Tree.id, "Jason", Tree.score.withValue(100) ~ Tree.owner.withValue(UUID.randomUUID()))
  objs.create(tree2)
}
