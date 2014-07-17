package totemPoles.config

import com.escalatesoft.subcut.inject.NewBindingModule
import totemPoles.auth.{AuthCallBackPlan, Auth, BaiduAuth, Author}

import totemPoles.plan.{AllPlan, PassPlan, DataPlan}
import totemPoles.repository.{MongoConfig, MongoRepository, Repository}
import unfiltered.filter.{Planify, Plan}


object BaiduMongo extends MongoConfig("mongo.duapp.com", 8908, "RFDKVYDKvgfRiuthPvWO", Some(("FqfGOgMrlc72Ovc9yYNeUO9i", "3t7C3qiiS18ZGKP2oWg2QS28WDG8Fz7z")))


object ProductionConfig extends NewBindingModule({
  implicit module =>
    import module._
    import BindingKeys._
    bind[Repository] idBy DataRepositoryId toProvider new MongoRepository("datas")
    bind[MongoConfig] toSingle BaiduMongo

    bind[Author] toModuleSingle {
      case m => BaiduAuth
    }
    bind[Auth] toProvider new Auth()(module)
    bind[Plan] idBy DataPlanId toProvider {
      implicit module =>
        Planify(module.inject[Auth](None).apply(new DataPlan().intent))
    }
    bind[List[Plan]] toModuleSingle {
      case m =>
        List(new PassPlan, inject[Plan](Some(DataPlanId)), new AuthCallBackPlan())
    }
    bind[Plan] idBy AllPlanId toModuleSingle {
      implicit m =>
        new AllPlan()
    }
})