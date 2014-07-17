package totemPoles

import totemPoles.domain.Data

import totemPoles.auth.{Author, Auth, NoneAuth}
import totemPoles.config.BindingKeys.{DataRepositoryId, DataPlanId}
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



trait TestData extends Served{
  object TestPlanConfig2 extends NewBindingModule({implicit module=>
    import module._
    bind [Author] toModuleSingle {case m=> NoneAuth}
    bind [Auth] toProvider new Auth()(module)
    bind [Plan] idBy DataPlanId toProvider({implicit module =>
      Planify(module.inject[Auth](None).apply(new DataPlan()(module).intent))
    })
  })
  override def logHttpRequests = true
  implicit val bindingModule =  TestConfiguration ~  TestPlanConfig2
  val rep: Repository = bindingModule.inject[Repository](Some(DataRepositoryId.bindingName))

  def setup = {

    _.filter(     bindingModule.inject[Plan](Some(DataPlanId))
    )
  }

  trait clear extends Before {
    def before = {
      rep.clear
    }
  }

}

object DataPlanSpec extends Specification with TestData{
  sequential
//  skipAll
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

  "datas" should {
    val datas = host / "datas"
    val dataCreated = "The data has been successfully created"
    "validate" in {
      val j = testData()
      Data.validate(j).isSuccess must beTrue
      val b= testDataBadDate()
      Data.validate(b).isSuccess must beFalse
    }
    "list when empty" in new clear {
      http(datas <:< json as_str) must_==
        "[]"
    }
    "put will cool" in new clear {
      http(datas <:< json << testString as_str)
      val result = http(datas <:< json as_str)
      parse(result).asInstanceOf[JArray].doto {
        case a =>
          a.values.size must_== 1
          a.apply(0).asInstanceOf[JObject].values.doto {
            case v =>
              v.get("name") must some("testName")
              v.get("user") must some("nielinjie")
          }
      }
      success
    }
    "put2 will cool" in new clear {
      http(datas <:< json << testString as_str)
      http(datas <:< json << testString as_str)
      val result = http(datas <:< json as_str)
      parse(result).asInstanceOf[JArray].doto {
        case a =>
          a.values.size must_== 2
          a.apply(0).asInstanceOf[JObject].values.doto {
            case v =>
              v.get("name") must some("testName")
              v.get("user") must some("nielinjie")
          }
      }
      success
    }
    "query" in new clear {
      http(datas <:< json << testString as_str)
      http(datas <:< json << testString2 as_str)
      val query = Map("query" -> "test")
      xhttp(datas <:< json <<? query as_str {
        case (code, _, _, what) =>
          code must_== 400
      })
      val query2 = Map("query" -> """{ "name": {"$eq":"testName"} }""")
      val result = http(datas <:< json <<? query2 as_str)
      parse(result).asInstanceOf[JArray].doto {
        case a =>
          a.values.size must_== 1
      }
      success
    }
  }
}
