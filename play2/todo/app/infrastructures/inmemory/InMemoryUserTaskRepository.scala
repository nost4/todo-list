package infrastructures.inmemory

import models.{UserTask, UserTaskRepository}


/**
  * ユーザタスクのリポジトリ、インメモリ実装
  */
class InMemoryUserTaskRepository extends InMemoryRepository[UserTask#Key, UserTask] with UserTaskRepository {

}
