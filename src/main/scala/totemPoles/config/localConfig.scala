package totemPoles.config
import com.escalatesoft.subcut.inject.{BindingId, NewBindingModule}
import name.nielinjie.common.baidu.auth.{Author, BaiduAuth}
import name.nielinjie.common.domain.Users
import name.nielinjie.common.plan.{AllPlan, PassPlan, UserInfoPlan, WelcomePlan}
import name.nielinjie.common.repository.{Mongo, MongoConfig}
import unfiltered.filter.Plan

object MogoConfigId extends BindingId
object LocalMongo extends MongoConfig("localhost", 27017, "totemPoles", None)

object IntegrateConfiguration extends NewBindingModule({
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


