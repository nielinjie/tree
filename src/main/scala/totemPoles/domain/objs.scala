package totemPoles.domain

import org.json4s.JsonAST.JObject
import totemPoles.domain.framework.{HasOwner, ObjType, Name}
import totemPoles.domain.framework.Validation.VE

import scalaz._
import Scalaz._

@Name("Person")
object Person extends ObjType {

  import framework.Validation._

  val pow = prop[BigInt]("pow")

  override def validateOnNewProper(peroperties: JObject): VE[Unit] = {
    pow.getterP(peroperties).flatMap({
      case p =>
        sure(p.>(0), "must greater than 0")
    })
  }
}

@Name("Tree")
object Tree extends ObjType with HasOwner {
  val score = prop[BigInt]("score")

  override def validateOnNewProper(peroperties: JObject): VE[Unit] = {
    ().right
  }
}