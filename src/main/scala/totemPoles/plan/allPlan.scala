package totemPoles.plan

import unfiltered.filter.{Planify, Plan}
import com.escalatesoft.subcut.inject.{BindingModule, Injectable}

class AllPlan(implicit val bindingModule:BindingModule) extends Plan with Injectable{
  val plans:List[Plan]=inject[List[Plan]]
  def intent =  plans.reduce({
    (planA:Plan,planB:Plan)=>
      Planify(planA.intent.orElse(planB.intent))
  }).intent
}
