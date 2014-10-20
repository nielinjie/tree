package totemPoles.plan

import name.nielinjie.common.plan.JsonRestPlan
import org.json4s.JsonAST.{JField, JString}
import org.json4s._
import org.slf4j.LoggerFactory
import totemPoles.domain._
import unfiltered.request.{POST, Path}
import unfiltered.response.{ResponseString, _}

import scala.util.{Failure, Success}
import scalaz.Validation

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