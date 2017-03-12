package infrastructures.inmemory

import models._


/**
  * ユーザタスクのリポジトリ、インメモリ実装
  */
class InMemoryUserTaskRepository extends InMemoryRepository[UserTaskRelation, UserTaskRelation] with UserTaskRepository {
  /** ${inheritDoc} */
  override def findTaskIds(userId: UserId): List[TaskId] = {
    entries.filter(_._1.userId == userId).map(_._1.taskId).toList
  }

  /** ${inheritDoc} */
  override def delete(userTaskRelation: UserTaskRelation): Unit = {
    remove(userTaskRelation)
  }
}
