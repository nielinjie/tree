package totemPoles.domain.framework


import dispatch.classic.json.JsNull.T

import scalaz._
import Scalaz._

object Validation {
  type ErrorMessage = String
  type VE[T] = \/[ErrorMessage, T]
  implicit class OV[T](val opt: Option[T]) {
    def toSuccessE(string: ErrorMessage): VE[T] = opt match{
      case None => -\/(string)
      case Some(t) => \/-(t)
    }
  }
  implicit def vte[T](v:ValidationNel[ErrorMessage,T]):VE[T]={
    v match {
      case Success(t) => \/-(t)
      case Failure(l) => -\/(l.toList.mkString)
    }
  }

  implicit def v2te[T](v:Validation[ErrorMessage,T]):VE[T]={
    v match {
      case Success(t) => \/-(t)
      case Failure(l) => -\/(l)
    }
  }
}

