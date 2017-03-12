package models

import shared.EntityRepository


/**
  * タスクのリポジトリ
  */
trait TaskRepository extends EntityRepository[Task] {
  /**
    * 新規タスクを作成する
    * @param task タスク(仮)
    * @return タスク(確定)
    */
  def create(task: Task): Task

  /**
    *  指定したIDと一致するタスクを取得する
    */
  def find(taskIds: Seq[TaskId]): List[Task]
}
