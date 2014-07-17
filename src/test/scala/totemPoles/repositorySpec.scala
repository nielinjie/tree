package totemPoles

import org.specs2.mutable.{After, Before, Specification}
import com.github.athieriot.{CleanAfterExample, EmbedConnection}
import totemPoles.repository.{Repository, MongoConfig, MongoRepository}
import org.json4s.JsonDSL._
import nielinjie.util.data.Helper._
import totemPoles.config.BindingKeys.DataRepositoryId

object RepositorySpec extends Specification  with EmbedConnection with CleanAfterExample{
  sequential
  skipAll
  trait clear extends Before {
    def before={
      repo.clear
    }
  }
  implicit val bindingModule =  TestWithMongoConfiguration
  val repo=bindingModule.inject[Repository](Some(DataRepositoryId.bindingName))
  "mongo repository" should {
    "list " in new clear {
      repo.query(None) must_== List()
    }
    "put one " in new clear {
      repo.add ("name"->"test")
      repo.query(None) .doto {
        case list =>
          list.size must_==(1)
          list.head .doto {
            case job =>
              job.values.get("name") must some("test")
          }
      }
      repo.query(Some(("name"->"test"))) .doto {
        case list =>
          list.size must_==(1)
          list.head .doto {
            case job =>
              job.values.get("name") must some("test")
          }
      }
      repo.query(Some(("name"->"not test"))) .doto {
        case list =>
          list.size must_==(0)

      }
    }
  }
}
