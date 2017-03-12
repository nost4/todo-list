package infrastructures.inmemory

import models.{Task, TaskId}
import org.joda.time.DateTime
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec

class InMemoryTaskRepositoryTest extends PlaySpec with MockitoSugar {
  def mockTask(id: TaskId): Task = {
    // NOTE: StringとDateTimeはmockできないのでダミーの値を設定する
    Task(id, "", mock[Option[String]], mock[Option[DateTime]], new DateTime(1, 1, 1, 0, 0, 0), mock[Option[DateTime]])
  }

  "InMemoryTaskRepository#find" should {
    "returns specific tasks" in {
      val repository = new InMemoryTaskRepository()
      repository.store(mockTask(TaskId(1)))
      repository.store(mockTask(TaskId(2)))
      repository.store(mockTask(TaskId(3)))

      val tasks = repository.find(List(1, 3).map(TaskId)).sortBy(_.id.value)
      tasks.length mustBe 2
      tasks.drop(0).head.id mustBe TaskId(1)
      tasks.drop(1).head.id mustBe TaskId(3)
    }
  }
}
