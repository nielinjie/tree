package totemPoles.domain
import scalaz._
import std.option._
import org.json4s.JsonAST.{JString, JValue}
import org.json4s._
import scalaz.Validation
import org.joda.time.DateTime
import nielinjie.util.data.LookUp._
import org.json4s.JsonAST.JString

object Chart {
  implicit def projectionFunction(jv:JValue, key:String):Option[Any] ={
    jv.asInstanceOf[JObject].values.get(key)
  }
  def validate(chart: JValue): Validation[String, JValue] = {
    implicit val formats=DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all
    implicit def j2d(j:String):DateTime={
      JString(j).extract[DateTime]
    }
    val v= for {
      name <- lookUp("name").required
      date <- lookUp("date").required.as[String].to[DateTime]
      user <- lookUp("user").required
      dsl <- lookUp("dsl").required
    } yield (chart)
    v.apply(chart)
  }

}