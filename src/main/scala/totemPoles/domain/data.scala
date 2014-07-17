package totemPoles.domain

import org.json4s.JsonAST.{JString, JValue}
import org.json4s.{DefaultFormats, JObject}
import scalaz._
import std.option._

import nielinjie.util.data.LookUp._
import java.util.Date
import org.joda.time.DateTime
import org.json4s.ext.DateTimeSerializer

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 14-1-17
 * Time: PM5:34
 * To change this template use File | Settings | File Templates.
 */
object Data {
  implicit def projectionFunction(jv:JValue, key:String):Option[Any] ={
    jv.asInstanceOf[JObject].values.get(key)
  }
  def validate(data: JValue): Validation[String, JValue] = {
    implicit val formats=DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all
    implicit def j2d(j:String):DateTime={
      JString(j).extract[DateTime]
    }
    val v= for {
      name <- lookUp("name").required
      date <- lookUp("date").required.as[String].to[DateTime]
      user <- lookUp("user").required
    } yield (data)
    v.apply(data)
  }
}