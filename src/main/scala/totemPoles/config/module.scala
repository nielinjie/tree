package totemPoles.config

import com.escalatesoft.subcut.inject.{MutableBindingModule, NewBindingModule, BindingId}
import totemPoles.auth.Auth

object BindingKeys {   // in some other file?
object DataRepositoryId extends BindingId
  object ChartRepositoryId extends BindingId
  object DataPlanId extends BindingId
  object AllPlanId extends BindingId
}

