package totemPoles.domain

import totemPoles.domain.framework.{HasOwner, Name, ObjType}



@Name("Person")
object Person extends ObjType {
  val pow=prop[BigInt]("pow")
}

@Name("Tree")
object Tree extends ObjType with HasOwner{
  val score=prop[BigInt]("score")
}