package totemPoles.domain.framework

import java.util.UUID


import org.json4s.JsonAST.JObject
import org.json4s.scalaz.JsonScalaz

import scalaz._
import scalaz.Scalaz._
import Validation._

case class Event(val willAffect: List[Affected]) {
  def vote(implicit objs: Objs): VE[Unit] = {
    willAffect.map({
      aff:Affected =>
        objs.getObj(aff.id)
          .toSuccessE(s"not find objid- ${aff.id}").flatMap({
          obj: Obj =>
            ObjTypes.findByObj(obj).toSuccessE(s"not find type for obj - ${obj.`type`}")
              .flatMap({
              objType: ObjType =>
                objType.validate(obj,aff.properties)
            })
        })
    }).seqV
  }


  def doIt(implicit objs: Objs): VE[Unit] = {
    willAffect.map({
      aff:Affected =>
        objs.getObj(aff.id)
          .toSuccessE(s"not find objid- ${aff.id}").flatMap({
          obj: Obj =>
            //TODO snapshot?
            //objs.snapshot(obj)
            objs.updateObj(aff.id, aff.properties)
        })
    }).seqV
  }


}

case class Affected(id: UUID, properties: List[AffectPro[_]])

case class AffectPro[T:JsonScalaz.JSONR](prop: Pro[T], func: T => T) {

  import Properties._

  def applyToObj(obj: Obj): VE[JObject] = {
    val name = prop.name
    obj.proToVE[T](name).map({
      v: T =>
        prop.withValue(func.apply(v))
    })
  }
}

//trait Computer[T] {
//  def add(a: T, b: T): T
//
//  def sub(a: T, b: T): T
//
//  def mul(a: T, b: T): T
//
//  def div(a: T, b: T): T
//}
//
//case class Operation[T: Computer](operator: String, operant: T) {
//  def apply(a: T): T = {
//    operator match {
//      case "+" => implicitly[Computer[T]].add(a, operant)
//      case "-" => implicitly[Computer[T]].sub(a, operant)
//      case "*" => implicitly[Computer[T]].mul(a, operant)
//      case "/" => implicitly[Computer[T]].div(a, operant)
//    }
//  }
//}

//object Computers {
//  implicit val cb = new Computer[BigInt] {
//    def add(a: BigInt, b: BigInt): BigInt = a + b
//
//    def sub(a: BigInt, b: BigInt): BigInt = a - b
//
//    def mul(a: BigInt, b: BigInt): BigInt = a * b
//
//    def div(a: BigInt, b: BigInt): BigInt = a / b
//  }
//}
