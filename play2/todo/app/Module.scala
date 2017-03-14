import com.google.inject.AbstractModule
import java.time.Clock

import infrastructures.inmemory.{InMemoryIOContext, InMemoryTaskRepository, InMemoryUserRepository, InMemoryUserTaskRepository}
import models.{TaskRepository, UserRepository, UserTaskRepository}
import services._
import shared.IOContext

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])

    // ユーザ関連の依存
    bind(classOf[UserRepository]).to(classOf[InMemoryUserRepository])
    bind(classOf[UserServiceFactory]).to(classOf[UserServiceFactoryImpl])

    // タスク・ユーザタスク関連の依存
    bind(classOf[TaskRepository]).to(classOf[InMemoryTaskRepository])
    bind(classOf[UserTaskRepository]).to(classOf[InMemoryUserTaskRepository])
    bind(classOf[UserTaskServiceFactory]).to(classOf[UserTaskServiceFactoryImpl])
  }
}

