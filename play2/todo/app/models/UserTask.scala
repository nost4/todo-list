package models


/**
  * ユーザとタスクの関連
  */
trait UserTask {
  /** ユーザタスクの識別キー */
  type Key = (UserId, TaskId)

  /** ユーザを取得する */
  def user: User

  /** タスクを取得する */
  def task: Task
}
