package services

import models._

/**
  * ユーザ-タスク間のサービス
  */
trait UserTaskService {
  /**
    * ユーザにタスクを割り当てる
    * @param user 割当先のユーザ
    * @param task タスク
    * @return ユーザタスク
    */
  def assignTaskToUser(user: User, task: Task): UserTask
}


/**
  * ユーザ-タスク間のサービスの実装
  */
class UserTaskServiceImpl(userTaskRepository: UserTaskRepository) extends UserTaskService {

  /** ユーザタスクの簡易実装 */
  case class UserTaskImpl(user: User, task: Task) extends UserTask

  /** ${inheritDoc} */
  override def assignTaskToUser(user: User, task: Task): UserTask = {
    val userTask = UserTaskImpl(user, task)

    // 関連のリポジトリに保存する
    userTaskRepository.store(userTask)

    // 関連を返す
    userTask
  }
}