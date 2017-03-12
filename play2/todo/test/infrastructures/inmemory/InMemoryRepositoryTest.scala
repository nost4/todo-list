package infrastructures.inmemory

import org.scalatestplus.play.PlaySpec
import shared.Entity

class InMemoryRepositoryTest extends PlaySpec {
  // 確認用のダミーエンティティとリポジトリ
  case class PersonId(value: Int)
  case class Person(id: PersonId, name: String) extends Entity { type ID = PersonId }
  class InMemoryPersonRepository extends InMemoryEntityRepository[Person] {
  }

  "InMemoryRepositoryTest#find" should {
    "return none if repository is empty" in {
      new InMemoryPersonRepository().find(PersonId(1)) mustBe None
    }
  }

  "InMemoryRepositoryTest#store" should {
    "store new person" in {
      val person = Person(PersonId(1), "Taro")
      val repository = new InMemoryPersonRepository()
      repository.store(person)

      repository.find(PersonId(1)) mustBe Some(person)
    }

    "overwrite stored person" in {
      val taro = Person(PersonId(1), "Taro")
      val repository = new InMemoryPersonRepository()
      repository.store(taro)

      val jiro = taro.copy(name="Jiro")
      repository.store(jiro)
      repository.find(PersonId(1)) mustBe Some(jiro)
    }
  }
}
