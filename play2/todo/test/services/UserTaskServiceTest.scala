package services

import infrastructures.inmemory.{InMemoryTaskRepository, InMemoryUserTaskRepository}
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
      val taskRepository = new InMemoryTaskRepository()
      val userTaskRepository = new InMemoryUserTaskRepository()
      val service = new UserTaskServiceImpl(taskRepository, userTaskRepository)

      val user = mockUser(UserId("1"))

      // タスク1
      taskRepository.find(TaskId("1")) mustBe None
      userTaskRepository.find((UserId("1"), TaskId("1"))) mustBe None

      val firstUserTask = service.createNewTask(user, mockTask(TaskId("1")))
      firstUserTask.user.id mustBe UserId("1")
      firstUserTask.task.id mustBe TaskId("1")
      userTaskRepository.find((UserId("1"), TaskId("1"))) mustBe Some(firstUserTask)

      // タスク2
      taskRepository.find(TaskId("2")) mustBe None
      userTaskRepository.find((UserId("1"), TaskId("2"))) mustBe None

      val secondUserTask = service.createNewTask(user, mockTask(TaskId("2")))
      secondUserTask.user.id mustBe UserId("1")
      secondUserTask.task.id mustBe TaskId("2")
      userTaskRepository.find((UserId("1"), TaskId("2"))) mustBe Some(secondUserTask)
    }
  }
}