package infrastructures.persistence.scalike

import models.{TaskId, UserId, UserTaskRelation, UserTaskRepository}

import scalikejdbc._


class ScalikeUserTaskRepository extends ScalikeRepository[UserTaskRelation, UserTaskRelation] with UserTaskRepository {
  /** ${inheritDoc} */
  override def findTaskIds(userId: UserId)(implicit context: Context): List[TaskId] = {
    withSession(context) { implicit session =>
      val id = userId.value
      sql"SELECT task_id FROM user_tasks WHERE user_id = ${id}".map(rs => TaskId(rs.int("task_id"))).list.apply()
    }
  }

  /** ${inheritDoc} */
  override def find(key: Key)(implicit context: Context): Option[Value] = {
    def toUserTaskRelation(rs: WrappedResultSet): UserTaskRelation = {
      UserTaskRelation(UserId(rs.int("user_id")), TaskId(rs.int("task_id")))
    }

    withSession(context) { implicit session =>
      val userId = key.userId.value
      val taskId = key.taskId.value

      val query = sql"SELECT * FROM user_tasks WHERE user_id = ${userId} AND task_id = ${taskId}"
      query.map(toUserTaskRelation).headOption.apply()
    }
  }

  /** ${inheritDoc} */
  override def store(key: Key, value: Value)(implicit context: Context): Unit = {
    withSession(context) { implicit session =>
      val userId = key.userId.value
      val taskId = key.taskId.value
      sql"INSERT INTO user_tasks (user_id, task_is) VALUES (${userId}, ${taskId})"
    }
  }

  /** ${inheritDoc} */
  override def remove(key: Key)(implicit context: Context): Unit = {
    withSession(context) { implicit session =>
      val userId = key.userId.value
      val taskId = key.taskId.value

      sql"DELETE FROM user_tasks WHERE user_id = ${userId} AND task_id = ${taskId}".update.apply()
    }
  }
}
