package totemPoles.plan

import totemPoles.repository.Repository
import org.json4s.{JObject, JValue}
import scalaz.{Success, Validation}
import java.util.UUID
import nielinjie.util.data.LookUp._
import totemPoles.domain.{Position, Person, Objs}
import org.json4s._


import scalaz._
import std.option._
import scala.util.Try

class VisualPlan extends JsonRestPlan {
  implicit val formats=DefaultFormats

  override val objName: String = "visual"

  implicit def projectionFunction(jv: JValue, key: String): Option[Any] = {
    jv.asInstanceOf[JObject].values.get(key)
  }

  override def validate(obj: JValue): Validation[String, JValue] = ???

  override val repository: Repository = new Repository {
    override def clear(): Unit = ???

    override def add(obj: JObject): UUID = ???

    override def query(query: Option[JObject]): List[JObject] = {
      query match {
        case Some(q) => {
          val idPosition = for {
            id <- lookUp("id").required.as[String]
            po <- lookUp("position").required.as[JObject]
          } yield (id, po)
          idPosition(q) match {
            case Success((id, po)) => {
              val person = Objs.getObj(id).get.asInstanceOf[Person]
              Try{

                Objs.findByPosition(po.extract[Position], person.visualRange).map {
                  Extraction.decompose(_).asInstanceOf[JObject]
                }
              } .get
            }
            case _ => throw new IllegalArgumentException("id and position required")
          }
        }
        case _ =>
          throw new IllegalArgumentException("id and position required")
      }

    }
  }
  override val collectionName: String = ???
}