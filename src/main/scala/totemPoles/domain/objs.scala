package totemPoles.domain

import java.util.{Date, UUID}

import org.json4s.JsonAST.JObject

import scalaz.Validation

case class PersonProperties(pack: Pack, fullName: String)

case class Pack(items: List[Obj])

object Person extends ObjType {

  override def id: UUID = "7a85e038-93e4-4402-beda-61b358ef49f6"

  override def update(affected: JObject): Validation[String, Unit] = ???

  override def name: String = "Person"

  override def validate(affected: JObject): Validation[String, Unit] = ???

}

object Tree extends ObjType {
  override def id: UUID = "209ed829-f6af-459d-bfa6-ccd4bb5bbabc"

  override def update(affected: JObject): Validation[String, Unit] = ???

  override def name: String = "Tree"

  override def validate(affected: JObject): Validation[String, Unit] = ???
}