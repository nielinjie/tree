package totemPoles.domain

import java.util.{Date, UUID}

import org.json4s.JsonDSL._
import org.junit.runner.Computer
import org.specs2.mutable._
import totemPoles.domain.framework.{AffectPro, Obj}


class ObjsSpec extends Specification {
  import ObjT._

  "objs" should {
    "create" in new EmptyObjs {


      val person = obj(Person.id, "Jason", ("pow" -> 100))
      objs.getObj(person.id) must beNone
      objs.create(person)
      objs.getObj(person.id) must beSome(beEqualTo(person))
    }
    "update" in new ObjsWithAPerson {


      objs.getObj(person.id) must beSome(beEqualTo(person))
      objs.updateObj(person.id, AffectPro(Person.pow, (BigInt(20).+ _)) :: Nil)
      objs.getObj(person.id) must beSome(not(beEqualTo(person)))
      val newPerson = Obj(person.id, Person.id, "Jason", ("pow" -> 120), new Date())
      objs.getObj(person.id) must beLike {
        case Some(p: Obj) =>
          p.properties must beEqualTo(newPerson.properties)
          p.timestamp must not equalTo (newPerson.timestamp)
      }
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