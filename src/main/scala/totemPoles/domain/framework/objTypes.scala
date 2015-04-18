package totemPoles.domain.framework

import org.json4s.JsonAST.JObject
import totemPoles.domain.framework.Validation._
import totemPoles.domain.{Tree, Person}

import scala.annotation.StaticAnnotation

trait ObjType extends TypeHelper {

  def validate(affected: List[AffectPro[_]]): VE[Unit] = ???


}

case class Name(name: String) extends StaticAnnotation

object ObjTypes {
  val ots = List(Person,Tree) //++ dynamic ones
  //TODO index.
  def findByObj (obj:Obj)={
    ots.find(_.id == obj.`type`)
  }
}