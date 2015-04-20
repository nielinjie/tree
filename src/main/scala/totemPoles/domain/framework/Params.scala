package totemPoles.domain.framework

import org.json4s.Extraction
import org.json4s.JsonAST.JObject
import org.json4s.scalaz.JsonScalaz
import totemPoles.domain.framework.Validation._

import scalaz._


import org.json4s.JsonDSL._

/**
 * Created by nielinjie on 4/16/15.
 */
object Params extends Params
trait Params extends JSON{
  implicit class PF(jo: HasParameters) {
    def paraToVE[T: JsonScalaz.JSONR](name: String): VE[ T] = {
      JsonScalaz.field[T](name)(jo.parameters).leftMap(_.map(_.toString()))
    }
  }
  def para[T:JsonScalaz.JSONR](name: String)= Param[T](name)
}
trait HasParameters {
  def parameters: JObject
}

case class Param[T:JsonScalaz.JSONR](name:String){
  import Params._
  def toVE:HasParameters => VE[ T]={
    obj: HasParameters =>
      obj.paraToVE[T](name)
  }
  def withValue(t:T): JObject = {
    (name -> Extraction.decompose(t)):JObject
  }
}