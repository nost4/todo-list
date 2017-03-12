package models

import shared.EntityRepository


/**
  * タスクのリポジトリ
  */
trait TaskRepository extends EntityRepository[Task] {
  /**
    * 次の識別子を割り当てる
    *
    * @return 次の識別子
    */
  override def nextIdentity(): TaskId = TaskId(generateUUID().toString)

  /**
    *  指定したIDと一致するタスクを取得する
    */
  def find(taskIds: Seq[TaskId]): List[Task]
}
