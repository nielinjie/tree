package totemPoles.server

import java.io.File
import totemPoles.config.IntegrateConfiguration
import unfiltered.filter.Plan.Intent
import unfiltered.filter.Plan
import totemPoles.config.BindingKeys.AllPlanId



class JettyFilter extends unfiltered.filter.Plan {
  implicit val bindingModule =  IntegrateConfiguration
  override def intent: Intent =      bindingModule.inject[Plan](Some(AllPlanId)).intent
}