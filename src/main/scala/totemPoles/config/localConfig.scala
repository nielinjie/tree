package totemPoles.config

import com.escalatesoft.subcut.inject.NewBindingModule
import totemPoles.repository.{Repository, MongoRepository, MongoConfig}
import totemPoles.auth._
import unfiltered.filter.{Planify, Plan}
import totemPoles.plan.{WelcomePlan, AllPlan, PassPlan, DataPlan}
import totemPoles.repository.MongoConfig
import scala.Some



object LocalMongo extends MongoConfig("localhost", 27017, "totemPoles", None)

object IntegrateConfiguration extends NewBindingModule({
  implicit module =>
    import module._
    import BindingKeys._
    bind[Repository] idBy DataRepositoryId toProvider new MongoRepository("datas")
    bind[MongoConfig] toSingle LocalMongo
    bind[Author] toModuleSingle {
      case m => BasicAuth
    }
    bind[Auth] toProvider new Auth()(module)
    bind[Plan] idBy DataPlanId toProvider {
      implicit module =>
        Planify(module.inject[Auth](None).apply(new DataPlan().intent))
    }
    bind[List[Plan]] toModuleSingle {
      case m =>
        List(
          new WelcomePlan("./public/geo.html"),
          new PassPlan
          ,inject[Plan](Some(DataPlanId))
        )
    }
    bind[Plan] idBy AllPlanId toModuleSingle {
      implicit m =>
        new AllPlan()
    }
})


