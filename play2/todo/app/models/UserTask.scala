package models


/**
  * ユーザとタスクの関連
  * @param userId ユーザ識別子
  * @param taskId タスク識別子
  */
case class UserTaskRelation(userId: UserId, taskId: TaskId)


/**
  * ユーザタスク
  */
trait UserTask {
  /** ユーザを取得する */
  def user: User

  /** タスクを取得する */
  def task: Task
}
