package models

import shared.EntityRepository


/**
  * タスクのリポジトリ
  */
trait TaskRepository extends EntityRepository[Task] {
  /**
    *  指定したIDと一致するタスクを取得する
    */
  def find(taskIds: Seq[TaskId]): List[Task]
}
