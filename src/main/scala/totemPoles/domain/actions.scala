package totemPoles.domain

import java.util.UUID

import org.json4s.scalaz.JsonScalaz._
import totemPoles.domain.framework.Validation.{VE, ErrorMessage}
import totemPoles.domain.framework._

import scalaz.Scalaz._
import scalaz.ValidationNel

//object Pick extends ActionType {
//  override def id: UUID = "abd0abc7-b5ec-4a78-9004-1c5eb10fa8b0"
//
//  override def name: String = "Pick"
//
//  override def apply(act: Action): Validation[String, Affected] = ??? // {
//  //    for {
//  //     _ <- objType(act, "Person");
//  //     pack <-
//  //    } yield ()
//  //}
//
//
//  override def enabled(obj: UUID): List[Action] = ???
//}


@Name("Grow")
object Grow extends ActionType with HasSub {
  import framework.Validation._

  val amount = prop[BigInt]("amount")
  val amountPara = para[Range]("amount")

  override def apply(act: Action)(implicit objs: Objs): VE[Event] = {
    for {
      person <- objs.objWithType(act.obj, Person.id)
      tree <- subject.getter(act).flatMap(objs.objWithType(_, Tree.id))
      owner <- Tree.owner.getter(tree)
      _ <- sure(owner == person.id, "owner wrong")
      power <- Person.pow.getter(person)
      amount <- amount.getter(act)
      _ <- sure(power > amount, "pow not enough")
    } yield Event(List(
      Person.pow.affected(person, _ - amount),
      Tree.score.affected(tree, _ + amount)
    ))
  }


  override def enabled(obj: UUID)(implicit objs: Objs): List[Action] = {
    val find = for {
      person <- objs.objWithType(obj, Person.id).toOption
      tree <- objs.getByOwner(obj, Tree.id).headOption
      powI <- Person.pow.getter(person).toOption
    } yield List(Action(UUID.randomUUID(),
        this.id, obj,
        subject.withValue(tree.id),
        amountPara.withValue(Range(1, powI.toInt))))
    find.getOrElse(Nil)
  }
}