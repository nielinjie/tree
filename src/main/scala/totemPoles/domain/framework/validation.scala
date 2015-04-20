package totemPoles.domain.framework


import org.json4s.JsonAST.JObject

import scalaz._
import Scalaz._

object Validation {
  type ErrorMessage = String
  type VE[T] = \/[ErrorMessage, T]

  implicit class OV[T](val opt: Option[T]) {
    def toSuccessE(string: ErrorMessage): VE[T] = opt match {
      case None => -\/(string)
      case Some(t) => \/-(t)
    }
  }

  implicit class LVU(val l: List[VE[Unit]]) {
    def seqV: VE[Unit] = {
      l.foldLeft(().right[ErrorMessage])({
        (r: VE[Unit], v: VE[Unit]) =>
          r.flatMap({ _ => v })
      })
    }
  }
  implicit class LV[T](val l: List[VE[T]]) {
    def toVEL: VE[List[T]] = {
      l.foldLeftM[VE,List[T]](List.empty[T])({
        (l:List[T],b:VE[T])=>
          b.map(_ :: l)
      })
    }
  }

  implicit def vte[T](v: ValidationNel[ErrorMessage, T]): VE[T] = {
    v match {
      case Success(t) => (t).right
      case Failure(l) => (l.toList.toString).left
    }
  }

  implicit def v2te[T](v: Validation[ErrorMessage, T]): VE[T] = {
    v match {
      case Success(t) => (t).right
      case Failure(l) => (l).left
    }
  }
  def sure(validate: => Boolean, message: String): VE[Unit] = {
    if (validate)
      ().right
    else
      message.left
  }

}

