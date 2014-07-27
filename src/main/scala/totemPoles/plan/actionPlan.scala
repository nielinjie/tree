package totemPoles.plan

import org.slf4j.LoggerFactory
import totemPoles.repository.Repository
import org.json4s._
import scalaz.{Success, Validation}
import totemPoles.domain._
import java.util.UUID
import totemPoles.domain.Create

class ActionPlan extends JsonRestPlan {
  implicit val formats=DefaultFormats
  val logger = LoggerFactory.getLogger(classOf[DataPlan])
  val repository = new Repository {
    override def clear(): Unit = ???

    override def add(obj: JObject): UUID = {
      logger.debug(s"action pushed - ${obj}")
      val ac: Action = if (obj.values("type") == "create") {
        obj.extract[Create]
      } else {
        obj.extract[Bless]
      }
      Actions.push(ac).map {
        UUID.fromString(_)
      }.getOrElse(???)
    }

    override def query(query: Option[JObject]): List[JObject] = ???

  }
  val collectionName = "actions"
  val objName = "action"

  def validate(obj: JValue): Validation[String, JValue] = Success(obj)
}