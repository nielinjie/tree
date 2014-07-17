package totemPoles.auth

import totemPoles.domain.User

import dispatch.classic._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonAST.JValue
import nielinjie.util.data.LookUp._
import totemPoles.domain.User
import org.joda.time.DateTime
import scalaz._
import Scalaz._
import org.slf4j.{LoggerFactory, Logger}

case class Err(code:String,msg:String)
object BaiduSocial {
  val errorL=for {
    code <- lookUp("error_code").required.as[String]
    msg <- lookUp("error_msg").required.as[String]
  } yield (Err(code,msg))
  val logger = LoggerFactory.getLogger(BaiduSocial.getClass)
  implicit def projectionFunction(jv:JValue, key:String):Option[Any] ={
    jv.asInstanceOf[JObject].values.get(key)
  }
  def lookup(code:String):Option[(String,User)] ={
    /*
    https://openapi.baidu.com/social/oauth/2.0/token?
	  grant_type=authorization_code&
	  code=ANXxSNjwQDugOnqeikRMu2bKaXCdlLxn&
	  client_id=Va5yQRHlA4Fq4eR3LT0vuXV4&
	  client_secret=0rDSjzQ20XUj5itV7WRtznPQSzr5pVw2&
	  redirect_uri=http%3A%2F%2Fwww.example.com%2Fsocial_oauth_redirect
     */
    val baidu=url("https://openapi.baidu.com/social/oauth/2.0/token") <<? Map(
      "grant_type" -> "authorization_code",
      "code" -> code,
      "client_id" -> "FqfGOgMrlc72Ovc9yYNeUO9i",
      "client_secret" -> "3t7C3qiiS18ZGKP2oWg2QS28WDG8Fz7z",
      "redirect_uri" -> "http://lifethread.duapp.com/auth/callback"
    )
    val userL=for {
      name <- lookUp("name").required.as[String]
      id <- lookUp("social_uid").required.as[String]
      token <- lookUp("access_token").required.as[String]
    } yield ((token,User(id,name)))
    val handle = (baidu) >- {
      json:String =>
        val j = parse(json)
        userL(j) match {
          case Success(u) => Some(u)
          case Failure(_) =>
            errorL(j) match {
              case Success(e) => logger.error(e.toString)
              case Failure(_) => logger.error(s"unknown error - ${json}")
            }
            None
        }
    }
    new Http().apply(handle)
  }
  def getUser(token:String):Option[User]={
    val baidu = url("https://openapi.baidu.com/social/api/2.0/user/info")
    val userL=for {
      name <- lookUp("username").required.as[String]
      id <- lookUp("social_uid").required.as[String]
    } yield (User(id,name))

    val handle = (baidu <<? Map("access_token"->token)) >- {
      json:String =>
        val j = parse(json)
        userL(j) match {
          case Success(u) => Some(u)
          case Failure(_) =>
            errorL(j) match {
              case Success(e) => logger.error(e.toString)
              case Failure(_) => logger.error(s"unknown error - ${json}")
            }
            None
        }
    }
    new Http().apply(handle)
  }
}