package services

import models._

/**
  * ユーザ-タスク間のサービス
  */
trait UserTaskService {
  /**
    * ユーザのタスクを作成する
    * @param user 対象のユーザ
    * @param task 新規タスク
    * @return ユーザタスク
    */
  def createNewTask(user: User, task: Task): UserTask
}


/**
  * ユーザ-タスク間のサービスの実装
  */
class UserTaskServiceImpl(taskRepository: TaskRepository, userTaskRepository: UserTaskRepository) extends UserTaskService {

  /** ユーザタスクの簡易実装 */
  case class UserTaskImpl(user: User, task: Task) extends UserTask

  /** ${inheritDoc} */
  override def createNewTask(user: User, task: Task): UserTask = {
    val userTask = UserTaskImpl(user, task)

    // タスクを追加する
    taskRepository.store(task)

    // 関連のリポジトリに保存する
    userTaskRepository.store(userTask)

    // 関連を返す
    userTask
  }
}