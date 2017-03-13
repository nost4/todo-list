package controllers

import javax.inject._

import exceptions.ResourceNotFoundException
import infrastructures.convert.{DateTimeConverter, TaskConverter, UserConverter}
import models._
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json
import play.api.mvc._
import services.UserTaskServiceFactory
import shared.IOContext


@Singleton
class TaskController @Inject()(
  userRepository: UserRepository,
  taskRepository: TaskRepository,
  userTaskRepository: UserTaskRepository,
  userTaskServiceFactory: UserTaskServiceFactory,
  dbConfigProvider: DatabaseConfigProvider
) extends TodoControllerBase(userRepository, H2IOContextHelper(dbConfigProvider))
    with UserConverter with TaskConverter with DateTimeConverter {

  // ----------------------------------------------------------------------------------------------
  // タスク関連のAPI
  // ----------------------------------------------------------------------------------------------

  case class ListUserTaskResponse(total: Int, tasks: List[Task])

  case class CreateUserTaskRequest(
    title: String,
    content: Option[String],
    deadlineAt: Option[DateTime]
  )

  case class PatchUserTaskRequest(
    title: Option[String],
    content: Option[String],
    deadlineAt: Option[DateTime],
    completedAt: Option[DateTime],
    removeDeadline: Option[Boolean],
    removeCompleted: Option[Boolean]
  )

  private implicit val userTaskResponseWrites = Json.writes[ListUserTaskResponse]
  private implicit val createUserTaskRequestReads = Json.reads[CreateUserTaskRequest]
  private implicit val patchUserTaskRequestReads = Json.reads[PatchUserTaskRequest]

  private[this] def withUserTask(userId: UserId, taskId: TaskId)(action: Task => Result)(implicit context: IOContext): Result = {
    userTaskRepository.find(UserTaskRelation(userId, taskId)).flatMap(_ => {
      taskRepository.find(taskId).map(task =>
        action(task)
      )
    }).getOrElse(NotFound(Json.toJson(ResourceNotFoundException("task", taskId.value))))
  }

  def listUserTasks(userId: Int) = Action {
    withContext { implicit context =>
      withUser(userId) { user =>
        val userTaskService = userTaskServiceFactory.create(taskRepository, userTaskRepository)
        val userTasks = userTaskService.findTasks(user.id)

        Ok(Json.toJson(ListUserTaskResponse(userTasks.length, userTasks)))
      }
    }
  }

  def createUserTasks(userId: Int) = Action { request =>
    jsonAction[CreateUserTaskRequest](request, createUserTaskRequestReads) { requestData =>
      withContext { implicit context =>
        withUser(userId) { user =>
          val userTaskService = userTaskServiceFactory.create(taskRepository, userTaskRepository)
          val userTask = userTaskService.createNewTask(user, requestData.title, requestData.content, requestData.deadlineAt)

          Created(Json.toJson(userTask.task))
        }
      }
    }
  }

  def getUserTask(userId: Int, taskId: Int) = Action {
    withContext { implicit context =>
      withUser(userId) { _ => // ユーザの存在確認でアクセスする
        withUserTask(UserId(userId), TaskId(taskId)) { task =>
          Ok(Json.toJson(task))
        }
      }
    }
  }

  private[this] def applyPatch(task: Task, requestData: PatchUserTaskRequest): Task = {
    def patchSome[T](t: Task, opt: Option[T], patcher: T => Task): Task = opt match {
      case Some(v) => patcher(v)
      case None => t
    }

    def patchIf(t: Task, opt: Option[Boolean], patcher: () => Task): Task = opt match {
      case Some(true) => patcher()
      case _ => t
    }

    @annotation.tailrec
    def patch(t: Task, patchers: List[Task => Task]): Task = {
      if (patchers.isEmpty) t
      else patch(patchers.head(t), patchers.drop(1))
    }

    // TODO: 実行時のパフォーマンスを確認すること
    def withTitle(t: Task) = patchSome[String](t, requestData.title, title => t.withTitle(title))
    def withContent(t: Task) = patchSome[String](t, requestData.content, content => t.withContent(content))
    def withDeadlineAt(t: Task) = patchSome[DateTime](t, requestData.deadlineAt, deadlineAt => t.withDeadline(deadlineAt))
    def withCompletedAt(t: Task) = patchSome[DateTime](t, requestData.completedAt, completedAt => t.withCompleted(completedAt))
    def removeDeadlineAt(t: Task) = patchIf(t, requestData.removeDeadline, () => t.removeDeadline())
    def removeCompletedAt(t: Task) = patchIf(t, requestData.removeCompleted, () => t.removeCompleted())
    val taskHandlers: List[Task => Task] = List(withTitle, withContent, withDeadlineAt, withCompletedAt, removeDeadlineAt, removeCompletedAt)
    patch(task, taskHandlers)
  }

  def patchUserTask(userId: Int, taskId: Int) = Action { request =>
    jsonAction[PatchUserTaskRequest](request, patchUserTaskRequestReads) { requestData =>
      withContext { implicit context =>
        withUser(userId) { user =>
          withUserTask(user.id, TaskId(taskId)) { task =>
            val patchedTask = applyPatch(task, requestData)
            val userTaskService = userTaskServiceFactory.create(taskRepository, userTaskRepository)
            userTaskService.updateTask(user.id, patchedTask)
            Ok(Json.toJson(patchedTask))
          }
        }
      }
    }
  }

  def deleteUserTask(userId: Int, taskId: Int) = Action {
    withContext { implicit context =>
      withUser(userId) { user => // ユーザの存在確認でアクセスする
        withUserTask(user.id, TaskId(taskId)) { task =>  // タスクの存在確認でアクセスする
          val userTaskService = userTaskServiceFactory.create(taskRepository, userTaskRepository)
          userTaskService.deleteTask(user.id, task.id)

          NoContent
        }
      }
    }
  }
}
