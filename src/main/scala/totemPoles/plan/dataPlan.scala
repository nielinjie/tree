package totemPoles.plan

import unfiltered.request._
import unfiltered.response._
import totemPoles.domain._
import scala._
import org.json4s.native.JsonMethods._
import scalaz._
import scala.Some
import scala.util.control.Exception._

import unfiltered.response.ResponseString


import org.json4s.{JObject, DefaultFormats, JValue, JArray}
import scala.Some
import unfiltered.response.ResponseString
import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import totemPoles.repository.Repository
import totemPoles.config.BindingKeys.DataRepositoryId
import org.slf4j.LoggerFactory

//class BaiduDataPlan extends DataPlan

class DataPlan(implicit val bindingModule: BindingModule) extends JsonRestPlan with Injectable{
  val logger=LoggerFactory.getLogger(classOf[DataPlan])
  val repository = inject[Repository](DataRepositoryId)
  val collectionName="datas"
  val objName="data"
  def validate(obj: JValue):Validation[String,JValue] = Data.validate(obj)
}