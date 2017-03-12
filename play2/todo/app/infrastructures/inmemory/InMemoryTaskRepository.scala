package infrastructures.inmemory

import models.{Task, TaskId, TaskRepository}

/**
  * タスクリポジトリのインメモリ実装
  */
class InMemoryTaskRepository extends InMemoryRepository[Task] with TaskRepository {
  /** ${inheritDoc} */
  override def find(taskIds: Seq[TaskId]): List[Task] = {
    entities
      .filter { case (id: TaskId, _: Task) => taskIds.contains(id) }
      .values.toList
  }
}
