package services

import infrastructures.inmemory.{InMemoryIOContext, InMemoryTaskRepository, InMemoryUserTaskRepository}
import models._
import org.joda.time.DateTime
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec


class UserTaskServiceTest extends PlaySpec with MockitoSugar {
  def mockUser(userId: UserId): User = User(userId, "")
  def mockTask(taskId: TaskId): Task = {
    Task(taskId, "", mock[Option[String]], mock[Option[DateTime]], new DateTime(1, 1, 1, 0, 0, 0), mock[Option[DateTime]])
  }

  implicit val context = InMemoryIOContext

  "UserTaskService#assignTaskToUser" should {
    "create new user-task" in {
      val taskRepository = new InMemoryTaskRepository()
      val userTaskRepository = new InMemoryUserTaskRepository()
      val service = new UserTaskServiceImpl(taskRepository, userTaskRepository)

      val user = mockUser(UserId(1))

      // タスク1
      taskRepository.find(TaskId(1)) mustBe None
      userTaskRepository.find(UserTaskRelation(UserId(1), TaskId(1))) mustBe None

      val firstUserTask = service.createNewTask(user, "title", Some("content"), None)
      firstUserTask.user.id mustBe UserId(1)
      firstUserTask.task.id mustBe TaskId(1)
      firstUserTask.task.title mustBe "title"
      firstUserTask.task.content mustBe Some("content")
      firstUserTask.task.deadlineAt mustBe None
      firstUserTask.task.completedAt mustBe None
      userTaskRepository.find(UserTaskRelation(UserId(1), TaskId(1))) mustBe Some(firstUserTask.relation)

      // タスク2
      taskRepository.find(TaskId(2)) mustBe None
      userTaskRepository.find(UserTaskRelation(UserId(1), TaskId(2))) mustBe None

      val secondUserTask = service.createNewTask(user, "task2", None, Some(new DateTime(1, 1, 1, 0, 0, 0)))
      secondUserTask.user.id mustBe UserId(1)
      secondUserTask.task.id mustBe TaskId(2)
      secondUserTask.task.title mustBe "task2"
      secondUserTask.task.content mustBe None
      secondUserTask.task.deadlineAt mustBe Some(new DateTime(1, 1, 1, 0, 0 ,0))
      secondUserTask.task.completedAt mustBe None
      userTaskRepository.find(UserTaskRelation(UserId(1), TaskId(2))) mustBe Some(secondUserTask.relation)
    }

    "update user-task" in {
      val taskRepository = new InMemoryTaskRepository()
      val userTaskRepository = new InMemoryUserTaskRepository()
      val service = new UserTaskServiceImpl(taskRepository, userTaskRepository)

      val user = mockUser(UserId(1))
      val userTask = service.createNewTask(user, "title", None, None)

      taskRepository.find(userTask.task.id).map(_.title) mustBe Some("title")
      taskRepository.find(userTask.task.id).flatMap(_.content) mustBe None
      taskRepository.find(userTask.task.id).flatMap(_.deadlineAt) mustBe None

      val updatedTask = userTask.task.withContent("content").withDeadline(new DateTime(2017, 3, 16, 0, 0, 0))
      service.updateTask(userTask.user.id, updatedTask)

      taskRepository.find(userTask.task.id).map(_.title) mustBe Some("title")
      taskRepository.find(userTask.task.id).flatMap(_.content) mustBe Some("content")
      taskRepository.find(userTask.task.id).flatMap(_.deadlineAt) mustBe Some(new DateTime(2017, 3, 16, 0, 0, 0))
    }

    "find user-tasks" in {
      val taskRepository = new InMemoryTaskRepository()
      val userTaskRepository = new InMemoryUserTaskRepository()
      val service = new UserTaskServiceImpl(taskRepository, userTaskRepository)

      val user = mockUser(UserId(1))

      // タスク1
      val firstUserTask = service.createNewTask(user, "title", Some("content"), None)
      // タスク2
      val secondUserTask = service.createNewTask(user, "task2", None, Some(new DateTime(1, 1, 1, 0, 0, 0)))

      // ID指定で検索
      val tasks = service.findTasks(UserId(1))
      tasks.sortBy(_.id.value) mustBe List(firstUserTask, secondUserTask).map(_.task).sortBy(_.id.value)
    }

    "delete user-task" in {
      val taskRepository = new InMemoryTaskRepository()
      val userTaskRepository = new InMemoryUserTaskRepository()
      val service = new UserTaskServiceImpl(taskRepository, userTaskRepository)

      val user = mockUser(UserId(1))
      val userTask = service.createNewTask(user, "title", None, None)

      taskRepository.find(userTask.task.id) mustBe Some(userTask.task)
      userTaskRepository.find(userTask.relation) mustBe Some(userTask.relation)

      service.deleteTask(userTask.user.id, userTask.task.id)

      taskRepository.find(userTask.task.id) mustBe None
      userTaskRepository.find(userTask.relation) mustBe None
    }
  }
}
