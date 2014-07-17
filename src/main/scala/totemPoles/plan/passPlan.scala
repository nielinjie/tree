package totemPoles.plan

import unfiltered.filter.Plan
import unfiltered.filter.Plan.Intent
import unfiltered.request.{Path, Seg}
import unfiltered.response.Pass


class PassPlan extends Plan {
  override def intent: Intent = {
    case Path(Seg("public":: _ ) )=>
      Pass
  }
}