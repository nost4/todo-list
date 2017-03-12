package models

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._

class UserTest extends PlaySpec with MockitoSugar {
  def mockUser(): User = User(mock[UserId], "")

  "User#rename" should {
    "has same user id" in {
      val srcUser = User(UserId("1"), "Taro")
      val renamedUser = srcUser.rename("Jiro")

      srcUser.id mustBe UserId("1")
      renamedUser.id mustBe srcUser.id
    }

    "be immutable" in {
      val srcUser = mockUser().copy(name = "Taro")
      val renamedUser = srcUser.rename("Jiro")

      srcUser.name mustBe "Taro"
      renamedUser.name mustBe "Jiro"
    }
  }
}
