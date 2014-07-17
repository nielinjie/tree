package totemPoles


import totemPoles.auth.{Auth, BasicAuth, Author}
import totemPoles.config.BindingKeys
import totemPoles.config.BindingKeys.{DataPlanId, DataRepositoryId}
import totemPoles.plan.DataPlan
import totemPoles.repository.Repository
import unfiltered.specs2.jetty.Served

import org.specs2.mutable.{Before, Specification}
import org.json4s.JsonAST.JObject
import org.joda.time.DateTime
import org.json4s.ext.DateTimeSerializer
import org.json4s.{JArray, DefaultFormats}

import nielinjie.util.data.Helper._
import org.json4s.native.JsonMethods._
import com.escalatesoft.subcut.inject.{Injectable, NewBindingModule}
import unfiltered.filter.{Plan, Planify}
import scala.Some

import scala.util.control.Exception._
import dispatch.classic.StatusCode

trait TestAuth extends Served{
  override def logHttpRequests = true
  object TestPlanConfig extends NewBindingModule({implicit module=>
    import module._
    bind [Author] toModuleSingle {case m=> BasicAuth}
    bind [Auth] toProvider new Auth()(module)
    bind [Plan] idBy BindingKeys.DataPlanId toProvider({implicit module =>
      Planify(module.inject[Auth](None).apply(new DataPlan()(module).intent))
    })
  })
  implicit val bindingModule =  TestConfiguration ~  TestPlanConfig
  val rep: Repository = bindingModule.inject[Repository](Some(DataRepositoryId.bindingName))

  def setup = {

    _.filter(
     bindingModule.inject[Plan](Some(DataPlanId))
    )
  }

  trait clear extends Before {
    def before = {
      rep.clear
    }
  }

}

object AuthSpec extends Specification with TestAuth{
  sequential

  def testData(): JObject = {
    import org.json4s._
    import org.json4s.JsonDSL.WithBigDecimal._
    import org.json4s.DefaultFormats
    implicit val d = DefaultFormats
    implicit def date2jvalue(dt: DateTime): JValue = DateTimeSerializer.serialize(d)(dt)
    val json = ("date" -> DateTime.now()) ~ ("user" -> "nielinjie") ~ ("name" -> "testName")
    json
  }
  def testDataBadDate(): JObject = {
    import org.json4s._
    import org.json4s.JsonDSL.WithBigDecimal._
    import org.json4s.DefaultFormats
    implicit val d = DefaultFormats
    implicit def date2jvalue(dt: DateTime): JValue = DateTimeSerializer.serialize(d)(dt)
    val json = ("date" -> "2014-1-1") ~ ("user" -> "nielinjie") ~ ("name" -> "testName")
    json
  }

  def testString: String = {
    import org.json4s.native._
    compact(renderJValue(testData))
  }

  def testString2: String = {
    import org.json4s.native._
    import Printer._
    import org.json4s._
    import org.json4s.JsonDSL.WithBigDecimal._
    import org.json4s.DefaultFormats
    implicit val d = DefaultFormats
    implicit def date2jvalue(dt: DateTime): JValue = DateTimeSerializer.serialize(d)(dt)
    val json = ("date" -> DateTime.now()) ~ ("user" -> "nielinjie") ~ ("name" -> "testName2")
    compact(renderJValue(json))
  }

  val json = Map("Content-type" -> "application/json", "Accept" -> "application/json")

  "auths" should {
    val datas = host / "datas" <:< json

    "no auths" in new clear {
      try {
        http(datas as_str)
        failure
      }catch {
        case e:StatusCode=>
          e.code must_== 401
        case _ =>
          failure
      }
    }

    "auths" in new clear {
      try {
        http(datas <<? Map("uname"->"nielinjie") as_str)  must_== "[]"
      }catch {
        case _=>failure
      }
    }
  }
}
