package totemPoles.domain.framework

import java.util.{Date, UUID}

import name.nielinjie.common.UUIDSerializer
import org.json4s.JsonAST.{JNull, JObject, JString}
import org.json4s.JsonDSL._
import org.json4s.ext.JodaTimeSerializers
import totemPoles.domain.framework.Validation._

import scalaz.Scalaz._
import org.json4s.scalaz.JsonScalaz
import org.json4s.{DefaultFormats, Extraction, _}
import totemPoles.domain.framework
import _root_.scalaz._

/**
 * Created by nielinjie on 4/16/15.
 */

object Properties extends Properties

trait Properties extends JSON {

  import Validation._

  implicit class U(s: String) {
    def toUUID: UUID = UUID.fromString(s)
  }


  implicit class F(jo: HasProperties) {
    def proToVE[T: JsonScalaz.JSONR](name: String): VE[T] = {
      JsonScalaz.field[T](name)(jo.properties).leftMap(_.toString)
    }
  }


  implicit def s2u(s: String): UUID = UUID.fromString(s)

  def prop[T: JsonScalaz.JSONR](name: String) = Pro[T](name)


}

trait HasProperties {
  def properties: JObject
}

case class Pro[T: JsonScalaz.JSONR](name: String) {

  import Properties._

  def getter: HasProperties => VE[T] = {
    obj: HasProperties =>
      obj.proToVE[T](name)
  }

  def getterP: JObject => VE[T] = {
    jo: JObject =>
      JsonScalaz.field[T](name)(jo).leftMap(_.toString)
  }

  def withValue(t: T): JObject = {
    (name -> Extraction.decompose(t)): JObject
  }


  def affected: (Obj, T => T) => Affected = {
    (obj: Obj, fun: T => T) =>
      Affected(obj.id, AffectPro(this, fun) :: Nil)
  }

}