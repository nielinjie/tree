package totemPoles.domain

import java.util.UUID

import com.escalatesoft.subcut.inject.NewBindingModule
import org.specs2.specification.Scope

import org.json4s.JsonDSL._


trait EmptyObjs extends Scope{
  class Config extends NewBindingModule(module => {
    import module._
    bind[Objs] toModuleSingle { _ => new Objs()}
    bind[Actions] toModuleSingle {implicit m => new Actions()}
    bind[ActionTypes] toModuleSingle {implicit m => new ActionTypes()}
  })
  val config = new Config
  val objs: Objs = config.inject[Objs](None)
  val actions:Actions=config.inject[Actions](None)
}
trait ObjsWithAPerson extends EmptyObjs{
  val person = Obj(UUID.randomUUID(), Person.id, "Jason", Person.pow.value(100))
  objs.create(person)
}
trait ObjsWithPersonAndTree extends ObjsWithAPerson{
  val tree=Obj(UUID.randomUUID(), Tree.id, "Jason", Tree.score.value( 100) ~ Tree.owner.value(person.id))
  objs.create(tree)
}
trait ObjsWithPersonAndSomeTrees extends ObjsWithPersonAndTree{
  val tree2=Obj(UUID.randomUUID(), Tree.id, "Jason", Tree.score.value(100) ~Tree.owner.value(UUID.randomUUID()))
  objs.create(tree2)
}