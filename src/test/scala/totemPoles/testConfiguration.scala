package totemPoles

import com.escalatesoft.subcut.inject.NewBindingModule
import totemPoles.repository.{MongoRepository, MongoConfig, MapRepository, Repository}
import totemPoles.config.BindingKeys.DataRepositoryId


object LocalTestMongon extends MongoConfig("localhost",12345,"dingdingding",None)


object TestConfiguration extends NewBindingModule({ implicit module =>
  import module._   // can now use bind directly
  bind [Repository] idBy DataRepositoryId toModuleSingle   { case m=>new  MapRepository}
})
object TestWithMongoConfiguration extends NewBindingModule({ implicit module =>
  import module._   // can now use bind directly
  bind [Repository] idBy DataRepositoryId toModuleSingle { case m => new  MongoRepository("test")}
  bind [MongoConfig] toSingle  LocalTestMongon
})

