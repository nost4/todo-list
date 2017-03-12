package models

import shared.Repository


/**
  * タスクのリポジトリ
  */
trait TaskRepository extends Repository[Task] {
  /**
    *  指定したIDと一致するタスクを取得する
    */
  def find(taskIds: Seq[TaskId]): List[Task]
}
