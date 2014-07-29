package totemPoles.plan

import org.slf4j.LoggerFactory
import totemPoles.repository.Repository
import org.json4s._
import scalaz.{ Validation}
import totemPoles.domain._
import java.util.UUID
import totemPoles.domain.Create
import scala.util.{Success, Failure}

class ActionPlan extends JsonRestPlan {
  implicit val formats=DefaultFormats
  val logger = LoggerFactory.getLogger(classOf[ActionPlan])
  val repository = new Repository {
    override def clear(): Unit = ???

    override def add(obj: JObject): UUID = {
      logger.debug(s"action pushed - ${obj}")
      val ac: Action = if (obj.values("type") == "create") {
        obj.extract[Create]
      } else {
        obj.extract[Bless]
      }
      Actions.push(ac) match {
        case Success(id)=>
          UUID.fromString(id)
        case Failure(t)=>
          throw t
      }
    }

    override def query(query: Option[JObject]): List[JObject] = ???

  }
  val collectionName = "actions"
  val objName = "action"

  def validate(obj: JValue): Validation[String, JValue] = scalaz.Success(obj)
}