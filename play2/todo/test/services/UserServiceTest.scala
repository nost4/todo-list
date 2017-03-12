package services

import infrastructures.inmemory.InMemoryUserRepository
import models.{User, UserId}
import org.scalatestplus.play.PlaySpec


class UserServiceTest extends PlaySpec {
  "UserService#createUser" should {
    "create new user" in {
      val repository = new InMemoryUserRepository()
      val taro = User(UserId("1"), "Taro")
      repository.store(taro)

      val service = new UserServiceImpl(userRepository = repository)
      val jiro = service.createUser("Jiro")

      val allUsers = repository.findAll()
      allUsers.length mustBe 2
      allUsers.sortBy(_.id.value) mustBe List(taro, jiro).sortBy(_.id.value)
    }
  }
}
