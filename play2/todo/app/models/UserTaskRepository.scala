package models

import shared.Repository


/**
  * ユーザタスクのリポジトリ
  */
trait UserTaskRepository extends Repository[UserTask#Key, UserTask] {

  /**
    * 指定したユーザタスクを保存する
    * @param userTask ユーザタスク
    */
  def store(userTask: UserTask): Unit = store((userTask.user.id, userTask.task.id), userTask)
}
