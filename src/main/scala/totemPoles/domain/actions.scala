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


class Grow(implicit val bindingModule: BindingModule)  extends ActionType {
  import Objs._
  override def id: UUID = "8d8e973d-5362-457c-b65e-33664010c20d"

  override def name: String = "Grow"

  override def apply(act: Action): ValidationNel[String, Affected] = {
    for {
      person <- objWithType(act.obj, "Person")
      treeId <- act.field[UUID]("sub")
      tree <- objWithType(treeId, "Tree")
      power <- person.field[BigInt]("power")
      amount <- act.paraField[BigInt]("amount")
      _ <- sure(power > amount, "pow not enough")
      oldScore <- tree.field[BigInt]("score")
      newScore <- (oldScore + amount).successNel
      newPower <- (power - amount).successNel

    } yield List(
      person.id -> prop("power" -> newPower),
      tree.id -> prop("score" -> newScore)
    )
  }


  override def enabled(obj: UUID): List[Action] = {
    val find=(for {
      o <- objWithType(obj, Person.id).toOption
      sub <- objs.getByOwner(obj, Tree.id).headOption
      pow <- field[BigInt]("pow")(o.properties).toOption
    } yield List(Action(UUID.randomUUID(),
        "Grow",
        prop("sub" -> sub.id),
        prop("amount" -> Range(1, pow.toInt)), obj)))
     find.getOrElse(Nil)
  }
}