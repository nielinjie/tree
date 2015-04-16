package totemPoles.domain.framework

import org.json4s.JsonAST.JObject

import scala.annotation.StaticAnnotation
import scalaz.Validation

trait ObjType extends TypeHelper {
  def update(affected: JObject): Validation[String, Unit] = ???

  def validate(affected: JObject): Validation[String, Unit] = ???


}

case class Name(name: String) extends StaticAnnotation

object ObjTypes {
  val ots = List() //++ dynamic ones
}