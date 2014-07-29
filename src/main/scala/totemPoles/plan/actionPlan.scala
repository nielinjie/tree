package totemPoles.plan

import org.slf4j.LoggerFactory
import totemPoles.repository.Repository
import org.json4s._
import scalaz.{ Validation}
import totemPoles.domain._
import java.util.UUID
import totemPoles.domain.Create
import scala.util.Failure
import unfiltered.request.{POST, Path}
import scala.util.control.Exception._

import unfiltered.response._
import org.json4s.JsonAST.{JString, JField}
import scala.util.Failure
import scala.util.Success
import org.json4s.JsonAST.JString
import scala.util.Failure
import totemPoles.domain.Create
import totemPoles.domain.Bless
import unfiltered.response.ResponseString

class ActionPlan extends JsonRestPlan {
  implicit val formats=DefaultFormats
  val logger = LoggerFactory.getLogger(classOf[ActionPlan])
  val repository =  null


  val collectionName = "actions"
  val objName = "action"

  def validate(obj: JValue): Validation[String, JValue] = scalaz.Success(obj)
  override def intent ={
    case req@(POST(Path(`collectionPath`))) => {
      checkContentJson(req) {
        req match {
          case JsonBody(c) => {
            validate(c) match {
              case scalaz.Success(d) => {
                Actions.push(Actions(c)) match {
                  case Success((result,_))=> Created ~> Json(JObject(JField("result", JString(result))))
                  case Failure(t) => InternalServerError ~> ResponseString(t.getMessage)
                }
              }
              case scalaz.Failure(f) => BadRequest ~> ResponseString(f)
            }
          }
          case _ => BadRequest ~> ResponseString("Invalid json data")
        }
      }
    }
  }



}