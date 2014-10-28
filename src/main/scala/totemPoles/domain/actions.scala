package totemPoles.domain

import java.util.UUID


import com.escalatesoft.subcut.inject.BindingModule
import totemPoles.domain.Objs.Affected

import scalaz._
import Scalaz._

import org.json4s.{Extraction, JObject}


import org.json4s.scalaz.JsonScalaz._

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

object Grow{
  import Objs._
  val id: UUID = "8d8e973d-5362-457c-b65e-33664010c20d".toUUID
}

class Grow(implicit val bindingModule: BindingModule)  extends ActionType with HasSub{
  import Objs._
  override def id: UUID = Grow.id

  override def name: String = "Grow"

  val amount=prop[BigInt]("amount")

  val amountPara=para[Range]("amount")


  override def apply(act: Action): ValidationNel[String, Affected] = {
    for {
      person <- objWithType(act.obj, Person.id)
      treeId <- this.sub.validation(act)
      tree <- objWithType(treeId, Tree.id)
      power <- Person.pow.validation(person)
      amount <- this.amount.validation(act)
      _ <- sure(power > amount, "pow not enough")
      oldScore <- Tree.score.validation(tree)
      newScore <- (oldScore + amount).successNel
      newPower <- (power - amount).successNel

    } yield List(
      person.id -> Person.pow.value(newPower),
      tree.id -> Tree.score.value(newScore)
    )
  }


  override def enabled(obj: UUID): List[Action] = {
    val find=(for {
      o <- objWithType(obj, Person.id).toOption
      sub <- objs.getByOwner(obj, Tree.id).headOption
      powI <- Person.pow.validation(o).toOption
    } yield List(Action(UUID.randomUUID(),
        this.id,
        this.sub.value(sub.id),
        this.amountPara.value(Range(1, powI.toInt)), obj)))
     find.getOrElse(Nil)
  }
}