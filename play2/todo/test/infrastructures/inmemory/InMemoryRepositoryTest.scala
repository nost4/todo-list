package infrastructures.inmemory

import org.scalatestplus.play.PlaySpec
import shared.Entity

class InMemoryRepositoryTest extends PlaySpec {
  case class PersonId(value: Int)
  case class Person(id: PersonId) extends Entity { type ID = PersonId }
  class InMemoryPersonRepository extends InMemoryRepository[Person] {
  }

  "InMemoryRepositoryTest#find" should {
    "return none if repository is empty" in {
      new InMemoryPersonRepository().find(PersonId(1)) mustBe None
    }

    "can store person" in {
      val person = Person(PersonId(1))
      val repository = new InMemoryPersonRepository()
      repository.store(Person(PersonId(1)))

      repository.find(PersonId(1)) mustBe Some(person)
    }
  }
}
