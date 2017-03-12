package services

import infrastructures.inmemory.InMemoryUserTaskRepository
import models.{Task, TaskId, User, UserId}
import org.joda.time.DateTime
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec


class UserTaskServiceTest extends PlaySpec with MockitoSugar {
  def mockUser(userId: UserId): User = User(userId, "")
  def mockTask(taskId: TaskId): Task = {
    Task(taskId, "", mock[Option[String]], mock[Option[DateTime]], new DateTime(1, 1, 1, 0, 0, 0), mock[Option[DateTime]])
  }

  "UserTaskService#assignTaskToUser" should {
    "store user-task relationship into repository" in {
      val repository = new InMemoryUserTaskRepository()
      val service = new UserTaskServiceImpl(repository)

      val user = mockUser(UserId(1))
      val task = mockTask(TaskId(1))
      val userTask = service.assignTaskToUser(user, task)

      userTask.user.id mustBe user.id
      userTask.task.id mustBe task.id
      repository.find((user.id, task.id)) mustBe Some(userTask)
    }
  }
}
