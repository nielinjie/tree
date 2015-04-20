package totemPoles.domain.framework

import java.util.Date

import scalaz._
import Scalaz._

import org.json4s.JsonAST.JObject
import totemPoles.domain.framework.Validation._
import totemPoles.domain.{Tree, Person}

import scala.annotation.StaticAnnotation

trait ObjType extends TypeHelper {

  def validate(obj: Obj, affected: List[AffectPro[_]]): VE[Unit] = {
    val aff: VE[List[JObject]] = affected.map(_.applyToObj(obj)).toVEL
    aff.map({
      af: List[JObject] =>
        val newProperties: JObject = af.foldLeft(obj.properties)(_.merge(_))
        this.validateOnNewProper(newProperties)
    })
  }
  def update(obj:Obj, affected: List[AffectPro[_]]):VE[Obj] ={
    val aff: VE[List[JObject]] = affected.map(_.applyToObj(obj)).toVEL
    aff.map({
      af: List[JObject] =>
        val newProperties: JObject = af.foldLeft(obj.properties)(_.merge(_))
        obj.copy(properties = newProperties,timestamp = new Date())
    })
  }

  def validateOnNewProper(peroperties: JObject): VE[Unit]
}

case class Name(name: String) extends StaticAnnotation

object ObjTypes {
  val ots = List(Person, Tree)//++ dynamic ones
  //TODO index.
  def findByObj(obj: Obj) = {
    ots.find(_.id == obj.`type`)
  }
}