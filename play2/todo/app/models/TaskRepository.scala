package models

import shared.EntityRepository


/**
  * タスクのリポジトリ
  */
trait TaskRepository extends EntityRepository[Task] {
  /**
    * 指定したタスク識別子と一致するタスクを取得する
    */
  def find(taskIds: Seq[TaskId]): List[Task]

  /**
    * 指定したタスク識別子のタスクを削除する
    */
  def delete(taskId: TaskId): Unit
}
