package totemPoles.domain.framework

import org.json4s.Extraction
import org.json4s.JsonAST.JObject
import org.json4s.scalaz.JsonScalaz

import scalaz._


import org.json4s.JsonDSL._

/**
 * Created by nielinjie on 4/16/15.
 */
object Params extends Params
trait Params extends JSON{

  type P =  {def parameters: JObject}

  class ProP[T:JsonScalaz.JSONR](val name:String){

    def validation:P => ValidationNel[String, T]={
      obj: P =>
        obj.paraField[T](name)
    }
    def value(t:T): JObject = {
      (name -> Extraction.decompose(t)):JObject
    }
  }
  implicit class PF(jo: {def parameters: JObject}) {
    def paraField[T: JsonScalaz.JSONR](name: String): ValidationNel[String, T] = {
      JsonScalaz.field[T](name)(jo.parameters).leftMap(_.map(_.toString()))
    }
  }
  def para[T:JsonScalaz.JSONR](name: String)=new ProP[T](name)
}
