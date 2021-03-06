package infrastructures.inmemory

import models.{User, UserId}
import org.scalatestplus.play.PlaySpec

class InMemoryUserRepositoryTest extends PlaySpec {
  implicit val context = InMemoryIOContext

  "InMemoryUserRepository#findAll" should {
    "returns empty list if repository is empty" in {
      new InMemoryUserRepository().findAll() mustBe List.empty[User]
    }

    "returns all stored users" in {
      val repository = new InMemoryUserRepository()
      repository.create(User(UserId(0), "Taro"))
      repository.create(User(UserId(0), "Jiro"))
      repository.create(User(UserId(0), "Saburo"))

      val users = repository.findAll().sortBy(_.id.value)
      users.length mustBe 3

      users.drop(0).head.id mustBe UserId(1)
      users.drop(0).head.name mustBe "Taro"

      users.drop(1).head.id mustBe UserId(2)
      users.drop(1).head.name mustBe "Jiro"

      users.drop(2).head.id mustBe UserId(3)
      users.drop(2).head.name mustBe "Saburo"
    }
  }
}
