package totemPoles.domain

import java.util.UUID

import com.escalatesoft.subcut.inject.NewBindingModule
import org.json4s.JObject
import org.json4s.JsonDSL._
import org.specs2.mutable._
import org.specs2.specification.Scope


class ObjsSpec extends Specification {
  "objs" should {
    "create" in new EmptyObjs {
      val person = Obj(UUID.randomUUID(), Person.id, "Jason", ("pow" -> 100))
      objs.getObj(person.id) must beNone
      objs.create(person)
      objs.getObj(person.id) must beSome(beEqualTo(person))
    }
    "update" in new ObjsWithAPerson {
      objs.getObj(person.id) must beSome(beEqualTo(person))
      objs.updateObj(person.id, Person.pow.value(120))
      objs.getObj(person.id) must beSome(not(beEqualTo(person)))
      val newPerson = Obj(person.id, Person.id, "Jason", ("pow" -> 120))
      objs.getObj(person.id) must beSome(beEqualTo(newPerson))
    }
    "owner" in new ObjsWithPersonAndTree {
      objs.getByOwner(person.id, Tree.id) must beLike {
        case (obj: Obj) :: Nil =>
          obj must be_==(tree)
      }
    }
    "owner2" in new ObjsWithPersonAndSomeTrees {
      objs.getByOwner(person.id, Tree.id) must beLike {
        case (obj: Obj) :: Nil =>
          obj must be_==(tree)
      }
    }
  }

}