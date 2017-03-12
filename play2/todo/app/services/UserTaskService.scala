package services

import models._
import org.joda.time.DateTime

/**
  * ユーザ-タスク間のサービス
  */
trait UserTaskService {
  /**
    * ユーザのタスクを作成する
    * @param user 対象のユーザ
    * @param title タイトル
    * @param content 内容
    * @param deadlineAt 期日
    * @return ユーザタスク
    */
  def createNewTask(user: User, title: String, content: Option[String], deadlineAt: Option[DateTime]): UserTask

  /**
    * ユーザのタスクを検索する
    * @param userId 検索対象のユーザ識別子
    * @return タスク一覧
    */
  def findTasks(userId: UserId): List[Task]

  /**
    * ユーザのタスクを更新する
    * @param userId ユーザ識別子
    * @param task タスク
    */
  def updateTask(userId: UserId, task: Task): Unit

  /**
    * ユーザのタスクを削除する
    * @param userId ユーザ識別子
    * @param taskId タスク識別子
    */
  def deleteTask(userId: UserId, taskId: TaskId): Unit
}


/**
  * ユーザ-タスク間のサービスの実装
  */
class UserTaskServiceImpl(taskRepository: TaskRepository, userTaskRepository: UserTaskRepository) extends UserTaskService {
  import infrastructures.chrono.TzAsiaTokyo

  /** ユーザタスクの簡易実装 */
  case class UserTaskImpl(user: User, task: Task) extends UserTask

  /** ${inheritDoc} */
  override def createNewTask(user: User, title: String, content: Option[String], deadlineAt: Option[DateTime]): UserTask = {
    // タスクを追加する
    val task = Task(TaskId(0), title, content, deadlineAt, DateTime.now(TzAsiaTokyo), None)
    val actualTask = taskRepository.create(task)

    // 関連のリポジトリに保存する
    val userTask = UserTaskImpl(user, actualTask)
    userTaskRepository.store(userTask)

    // 関連を返す
    userTask
  }

  /** ${inheritDoc} */
  override def updateTask(userId: UserId, task: Task): Unit = {
    taskRepository.update(task)
  }

  /** ${inheritDoc} */
  override def findTasks(userId: UserId): List[Task] = {
    // ユーザのタスク識別子一覧を検索する
    val userTaskIds = userTaskRepository.findTaskIds(userId)

    // タスク一覧を取得
    taskRepository.find(userTaskIds)
  }

  /** ${inheritDoc} */
  override def deleteTask(userId: UserId, taskId: TaskId): Unit = {
    // 関連 => タスク の順で削除する
    userTaskRepository.delete(UserTaskRelation(userId, taskId))
    taskRepository.delete(taskId)
  }
}


/**
  * ユーザタスクサービスのファクトリ
  */
trait UserTaskServiceFactory {
  def create(taskRepository: TaskRepository, userTaskRepository: UserTaskRepository): UserTaskService
}


/**
  * ユーザタスクサービスのファクトリの実装
  */
class UserTaskServiceFactoryImpl extends UserTaskServiceFactory {
  def create(taskRepository: TaskRepository, userTaskRepository: UserTaskRepository): UserTaskService = {
    new UserTaskServiceImpl(taskRepository, userTaskRepository)
  }
}
