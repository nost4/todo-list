package infrastructures.inmemory

import javax.inject.Singleton

import models._


/**
  * ユーザタスクのリポジトリ、インメモリ実装
  */
@Singleton
class InMemoryUserTaskRepository extends InMemoryRepository[UserTaskRelation, UserTaskRelation] with UserTaskRepository {
  /** ${inheritDoc} */
  override def findTaskIds(userId: UserId)(implicit context: Context): List[TaskId] = {
    entries.filter(_._1.userId == userId).map(_._1.taskId).toList
  }

  /** ${inheritDoc} */
  override def delete(userTaskRelation: UserTaskRelation)(implicit context: Context): Unit = {
    remove(userTaskRelation)
  }
}
