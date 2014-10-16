package totemPoles

import totemPoles.domain.{Bless, Position, Create}


import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}
import java.util.Date

object JsonExample {
  implicit val formats = DefaultFormats

  def main = {
    val create = Create("pole_id", "person_id", 
      "the pole", Position(13.13, 12.12), 
      Position(11.11, 10.10), new Date().getTime, "action")
    println("create  - ")
    println(write(create))
    val bless = Bless("pole_id", "person_id",
      Position(13.13, 12.12),
      new Date().getTime, "action")
    println("bless  - ")
    println(write(bless))
  }
}