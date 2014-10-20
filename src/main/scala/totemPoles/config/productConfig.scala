package totemPoles.config

import com.escalatesoft.subcut.inject.NewBindingModule
import name.nielinjie.common.baidu.auth.{Author, BaiduAuth}
import name.nielinjie.common.domain.Users
import name.nielinjie.common.plan.{AllPlan, PassPlan, UserInfoPlan, WelcomePlan}
import name.nielinjie.common.repository.{Mongo, MongoConfig}
import unfiltered.filter.Plan


object BaiduMongo extends MongoConfig("mongo.duapp.com", 8908, "RFDKVYDKvgfRiuthPvWO", Some(("FqfGOgMrlc72Ovc9yYNeUO9i", "3t7C3qiiS18ZGKP2oWg2QS28WDG8Fz7z")))


object ProductConfiguration extends NewBindingModule({
  implicit module =>
    import module._
    import totemPoles.config.BindingKeys._

    bind[Author] toProvider  new BaiduAuth


    bind[Mongo] toModuleSingle (_ => new Mongo)



    bind[Users] toProvider new Users

    bind[List[Plan]] toModuleSingle {
      implicit m =>
        List(
          new WelcomePlan("./public/front.html"),
          new PassPlan,
          new UserInfoPlan
        )
    }
    bind[Plan] idBy AllPlanId toModuleSingle {
      implicit m =>
        new AllPlan()
    }
})