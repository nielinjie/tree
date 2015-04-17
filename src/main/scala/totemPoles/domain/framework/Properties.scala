package totemPoles.domain.framework

import java.util.UUID

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
trait Properties extends JSON{

  implicit class U(s: String) {
    def toUUID: UUID = UUID.fromString(s)
  }



  implicit class F(jo: O) {
    def field[T: JsonScalaz.JSONR](name: String): ValidationNel[ErrorMessage, T] = {
      JsonScalaz.field[T](name)(jo.properties).leftMap(_.map(_.toString()))
    }
  }




  type O =  {def properties: JObject}


  implicit def s2u(s: String): UUID = UUID.fromString(s)

  def prop[T:JsonScalaz.JSONR](name: String)=new Pro[T](name)

  class Pro[T:JsonScalaz.JSONR](val name:String){
    def validation:O => ValidationNel[ErrorMessage, T]={
      obj: O =>
        obj.field[T](name)
    }
    def value(t:T): JObject = {
      (name -> Extraction.decompose(t)):JObject
    }

  }

}
