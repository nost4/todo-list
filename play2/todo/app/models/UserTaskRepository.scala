package models

import shared.Repository


/**
  * ユーザタスクのリポジトリ
  */
trait UserTaskRepository extends Repository[UserTaskRelation, UserTaskRelation] {
  /**
    * 指定したユーザのタスク識別子の一覧を取得する
    * @param userId ユーザ識別子
    */
  def findTaskIds(userId: UserId)(implicit context: Context): List[TaskId]

  /**
    * 指定したユーザタスクを保存する
    * @param userTaskRelation ユーザとタスクの関連
    */
  def store(userTaskRelation: UserTaskRelation)(implicit context: Context): Unit = {
    store(userTaskRelation, userTaskRelation)
  }

  /**
    * 指定したユーザタスクを保存する
    * @param userTask ユーザタスク
    */
  def store(userTask: UserTask)(implicit context: Context): Unit = {
    store(UserTaskRelation(userTask.user.id, userTask.task.id))
  }

  /**
    * 指定したユーザとタスクの関連を削除する
    * @param userTaskRelation ユーザとタスクの関連
    */
  def delete(userTaskRelation: UserTaskRelation)(implicit context: Context): Unit
}
