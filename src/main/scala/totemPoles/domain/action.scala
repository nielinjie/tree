package totemPoles.domain

import java.util.{Date, UUID}

import org.json4s.JsonAST.JValue
import org.json4s._

import scala.util.{Failure, Success, Try}

object Utils {
  implicit def s2t(msg: String): Throwable = {
    new IllegalArgumentException(msg)
  }
}

trait Action extends Product {
  def obj: String

  def sub: String

  def affect(): Try[String] = ???

  def time: Long

  def validate(): Try[Unit] = ???

  def id: String

  def `type`: String

  def withId: Action
}


case class Create(obj: String, sub: String, name: String, poObj: Position, position: Position, time: Long, id: String, `type`: String = "create") extends Action {

  import totemPoles.domain.Utils._

  override def affect(): Try[String] = {
    Pole.create(name, poObj).map(x => "Created.")
  }

  override def validate() = {
    Try {
      Objs.getObj(sub).getOrElse(throw s"can not find person ${sub}").asInstanceOf[Person]
    }.flatMap {
      person: Person =>
        if (poObj.distance(position) < person.visualRange) {
          Success(())
        } else {
          Failure("too far")
        }
    }
  }

  override def withId = this.copy(id = UUID.randomUUID().toString)
}

case class Bless(obj: String, sub: String, position: Position, time: Long, id: String, `type`: String = "bless") extends Action {

  import totemPoles.domain.Utils._

  val costPower = 2
  val getScore = 10
  val returnGift = 10

  override def validate(): Try[Unit] = {
    (Objs.getObj(sub), Objs.getObj(obj)) match {
      case (Some(s: Person), Some(o: Pole)) =>
        if (s.power < costPower)
          Failure(new IllegalArgumentException("no enough power"))
        else
          Success(())
      case (None, _) => Failure(s"can not find person ${sub}")
      case (_, None) => Failure(s"can not find pole ${obj}")
      case _ => Failure(s"only person can bless pole.")
    }

  }

  override def affect(): Try[String] = {
    val pole = Objs.getObj(obj).get.asInstanceOf[Pole]
    Objs.updateObj(pole.copy(score = pole.score + getScore))
    val person = Objs.getObj(sub).get.asInstanceOf[Person]
    Objs.updateObj(person.copy(
      lastPower = person.power - costPower,
      lastUpdate = new Date().getTime,
      gift = person.gift + returnGift
    ))
    Success("Blessed")
    //TODO 记下log，以便必要时收回影响。
  }

  override def withId = this.copy(id = UUID.randomUUID().toString)

}


object Actions {
  implicit val formats=DefaultFormats

  //TODO need to be sync?
  def push(action: Action): Try[(String, String)] = {
    action.validate() match {
      case Success(_) => {
        val aid = if (action.id == null) {
          action.withId
        } else {
          action
        }
        aid.affect.flatMap {
          result =>
            Success(result, aid.id)
        }
      }
      case Failure(err) => Failure(err)
    }

  }
  def apply(j:JValue):Action={
    val obj=j.asInstanceOf[JObject]
    if (obj.values("type") == "create") {
      obj.extract[Create]
    } else {
      obj.extract[Bless]
    }
  }

}