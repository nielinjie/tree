package totemPoles.domain.framework

import scalaz._
import Scalaz._

object Validation{
  type ErrorMessage = String
  implicit  class V[T,E](val opt:Option[T]){
    def toSuccessNel(string:E):ValidationNel[E,T]=opt.toSuccess(string).toValidationNel
  }
}

