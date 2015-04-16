package totemPoles.domain.framework



trait TypeHelper extends Properties{

  val id: String = {
    AnnotationUtil.name(this) match {
      case Some(n) => n
      case _ => throw new IllegalArgumentException()
    }
  }


}