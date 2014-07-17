package totemPoles.auth

import unfiltered.request._
import totemPoles.domain.User
import unfiltered.{Cookie, Cycle}
import unfiltered.response._
import com.escalatesoft.subcut.inject._
import scala.collection.mutable
import unfiltered.filter.Plan.Intent
import org.slf4j.LoggerFactory
import totemPoles.domain.User
import unfiltered.Cookie
import scala.Some
import unfiltered.response.ResponseString
import unfiltered.response.Redirect
import totemPoles.domain.User
import scala.Some
import unfiltered.response.ResponseString


trait Author {
  def auth(req: HttpRequest[_]): Option[User]
}

object NonEmptyUName extends Params.Extract(
  "uname",
  Params.first ~> Params.nonempty
)

object NonEmptyCode extends Params.Extract(
  "code",
  Params.first ~> Params.nonempty
)

object BasicAuth extends Author {
  val users = Map("nielinjie" -> User("nielinjie@fake", "nielinjie"))

  override def auth(req: HttpRequest[_]): Option[User] = {
    req match {
      case Params(NonEmptyUName(un)) =>
        users.get(un)
      case _ => {
        None
      }
    }
  }
}

object NoneAuth extends Author {
  override def auth(req: HttpRequest[_]): Option[User] = {
    Some(User("nielinjie@fake", "nielinjie"))
  }
}

object BaiduAuth extends Author {
  //TODO How about when token timeout?
  // access token
  val tokens: mutable.MutableList[String] = mutable.MutableList()
  // token -> user
  val users: mutable.Map[String, User] = mutable.Map()

  override def auth(req: HttpRequest[_]): Option[User] = {
    req match {
      case Params(NonEmptyCode(token)) =>
        if (tokens.contains(token)) {
          users.get(token) match {
            case None =>
              //TODO 401? without any try?
              None
            case u => u
          }
        }
        else
          None
      case _ => {
        None
      }
    }
  }
}


class Auth()(implicit val bindingModule: BindingModule) extends Injectable {
  println(s"module in auth - ${bindingModule}")

  val author: Author = inject[Author]

  def apply[A, B](intent: Cycle.Intent[A, B]) =
    Cycle.Intent[A, B] {
      case req if (author.auth(req).isDefined) =>
        Cycle.Intent.complete(intent)(req)
      case _ =>
        Unauthorized

    }
}

class AuthCallBackPlan(implicit val bindingModule: BindingModule) extends unfiltered.filter.Plan with Injectable {
  val logger = LoggerFactory.getLogger(classOf[AuthCallBackPlan])
  val author: BaiduAuth.type = inject[Author].asInstanceOf[BaiduAuth.type]

  override def intent: Intent = {
    case r@Path(Seg("auth" :: "callback" :: Nil)) => {
      r match {
        case Params(NonEmptyCode(code)) => {
          //1. lookup token
          //2. pass to client
          //3. save token to cache?
          BaiduSocial.lookup(code) match {
            case Some((token, user)) => {
              author.tokens.+=(token)
              author.users.+=(token -> user)
              Ok ~> SetCookies(Cookie("access_token", token)) ~> Redirect("http://lifethread.duapp.com/public/index.html")
            }
            case _ => {
              logger.error("can't get token")
              BadRequest ~> ResponseString("can't get token")
            }
          }
        }
        case _ => {
          logger.error(s"unknown callback")
          BadRequest ~> ResponseString("no code find")
        }
      }
    }

  }

}