package totemPoles.plan

import unfiltered.filter.Plan
import unfiltered.filter.Plan.Intent
import unfiltered.request.{Path, Seg}
import unfiltered.response.{Redirect, Ok, Pass}
import com.escalatesoft.subcut.inject.{BindingModule, Injectable}


class PassPlan extends Plan {
  override def intent: Intent = {
    case Path(Seg("public":: _ ) )=>
      Pass
  }
}
class WelcomePlan(val welcomeFile:String) extends Plan {
  override def intent: Intent = {
    case Path(Seg(Nil)) =>
      Ok~>Redirect(welcomeFile)
  }
}