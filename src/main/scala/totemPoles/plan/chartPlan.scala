package totemPoles.plan

import unfiltered.request._
import unfiltered.response._
import scala._
import org.json4s.native.JsonMethods._
import scalaz._
import scala.Some
import scala.util.control.Exception._

import unfiltered.response.ResponseString


import org.json4s.{JObject, DefaultFormats, JValue, JArray}
import scala.Some
import unfiltered.response.ResponseString
import totemPoles.domain._
import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import totemPoles.repository.Repository
import totemPoles.config.BindingKeys.ChartRepositoryId

//class BaiduChartPlan extends ChartPlan

//class LocalChartPlan extends ChartPlan(new MongoRepository("charts"))

class ChartPlan(implicit val bindingModule: BindingModule) extends JsonRestPlan with Injectable{
  val repository = inject[Repository] (ChartRepositoryId)
  val collectionName="charts"
  val objName="chart"
  def validate(obj: JValue):Validation[String,JValue] = Chart.validate(obj)
}

