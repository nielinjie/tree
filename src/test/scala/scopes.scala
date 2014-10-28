package totemPoles.domain

import java.util.UUID

import com.escalatesoft.subcut.inject.NewBindingModule
import org.specs2.specification.Scope


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
  import Objs._
  val person = Obj(UUID.randomUUID(), Person.id, "Jason", prop("pow"->100))
  objs.create(person)
}
trait ObjsWithPersonAndTree extends ObjsWithAPerson{
  import Objs._
  val tree=Obj(UUID.randomUUID(), Tree.id, "Jason", prop("score" -> 100,"owner"->person.id))
  objs.create(tree)
}
trait ObjsWithPersonAndSomeTrees extends ObjsWithPersonAndTree{
  import Objs._
  val tree2=Obj(UUID.randomUUID(), Tree.id, "Jason", prop("score" -> 100,"owner"->UUID.randomUUID()))
  objs.create(tree2)
}