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
  import Computers._
  val amount = prop[BigInt]("amount")
  val amountPara = para[Range]("amount")

  override def apply(act: Action)(implicit objs: Objs): VE[Event] = {
    for {
      person <- objs.objWithType(act.obj, Person.id)
      treeId <- subject.validation(act)
      tree <- objs.objWithType(treeId, Tree.id)
      power <- Person.pow.validation(person)
      amount <- amount.validation(act)
      _ <- sure(power > amount, "pow not enough")


    } yield Event(List(
      Affected(person.id, AffectPro(Person.pow, Operation[BigInt]("-",amount)) :: Nil),
      Affected(tree.id, AffectPro(Tree.score, Operation[BigInt]("+",amount)) :: Nil)
    ))
  }


  override def enabled(obj: UUID)(implicit objs: Objs): List[Action] = {
    val find = for {
      o <- objs.objWithType(obj, Person.id).toOption
      s <- objs.getByOwner(obj, Tree.id).headOption
      powI <- Person.pow.validation(o).toOption
    } yield List(Action(UUID.randomUUID(),
        this.id,
        subject.value(s.id),
        amountPara.value(Range(1, powI.toInt)), obj))
    find.getOrElse(Nil)
  }
}