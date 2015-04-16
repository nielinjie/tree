package totemPoles.domain.framework

import scala.reflect.runtime.universe._

/**
 * Created by nielinjie on 4/16/15.
 */
object AnnotationUtil {
  def findClassName(tree: Tree): Option[String] = {
    tree match {
      case Apply(Select(New(ty: TypeTree), _), _) =>
        Some(ty.symbol.name.toString)
      case _ => None
    }
  }

  def onlyArg[T](tree: Tree): Option[T] = {
    tree match {
      case Apply(_, List(Literal(Constant(s: T)))) =>
        Some(s)
      case _ => None
    }
  }

  def name(objType: TypeHelper): Option[String] = {
    val rm = runtimeMirror(getClass.getClassLoader)
    rm.reflect(objType).symbol.annotations.find({
      a: Annotation =>
        findClassName(a.tree).contains("Name")
    }).map({ a: Annotation =>
      onlyArg[String](a.tree)
    }).flatten
  }


}
