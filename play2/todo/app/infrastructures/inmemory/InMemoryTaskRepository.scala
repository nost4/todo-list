package infrastructures.inmemory

import models.{Task, TaskId, TaskRepository}

/**
  * タスクリポジトリのインメモリ実装
  */
class InMemoryTaskRepository extends InMemoryEntityRepository[Task] with TaskRepository {

  /** ${inheritDoc} */
  override def create(task: Task): Task = createNew(idHint => task.copy(id = TaskId(idHint)))

  /** ${inheritDoc} */
  override def find(taskIds: Seq[TaskId]): List[Task] = {
    entities
      .filter { case (id: TaskId, _: Task) => taskIds.contains(id) }
      .values.toList
  }
}
