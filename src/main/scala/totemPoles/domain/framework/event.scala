package totemPoles.domain.framework

import java.util.UUID

import org.json4s.JsonAST.JObject

import scalaz._
import scalaz.Scalaz._
import Validation._

case class Event(val willAffect: List[Affected]) {
  def vote(implicit objs: Objs): ValidationNel[ErrorMessage, Unit] = {
    willAffect.foldLeft(().successNel[ErrorMessage]) {
      (r: ValidationNel[ErrorMessage, Unit], aff: Affected) =>
        val thisAffected: ValidationNel[ErrorMessage, Unit] = objs.getObj(aff.id)
          .toSuccess(s"not find objid- ${aff.id}").toValidationNel.flatMap({
          obj: Obj =>
            ObjTypes.findByObj(obj).toSuccess(s"not find type for obj - ${obj.`type`}").toValidationNel
              .flatMap({
              objType: ObjType =>
                objType.validate(aff.properties)
            })
        })
        r.flatMap({ _ => thisAffected })
    }
  }


  def doIt(implicit objs: Objs): ValidationNel[ErrorMessage, Unit] = {
    willAffect.foldLeft(().successNel[ErrorMessage]) {
      (r: ValidationNel[ErrorMessage, Unit], aff: Affected) =>
        val thisAffected: ValidationNel[ErrorMessage, Unit] = objs.getObj(aff.id)
          .toSuccess(s"not find objid- ${aff.id}").toValidationNel.flatMap({
          obj: Obj =>
            //TODO snapshot
            //objs.snapshot(obj)
            objs.updateObj(aff.id,aff.properties)
        })
        r.flatMap({ _ => thisAffected })
    }
  }


}

case class Affected(id: UUID, properties: JObject)
