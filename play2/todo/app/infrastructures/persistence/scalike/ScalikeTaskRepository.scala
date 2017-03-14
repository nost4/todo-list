package infrastructures.persistence.scalike

import models.{Task, TaskId, TaskRepository}
import org.joda.time.{DateTime, DateTimeZone}
import scalikejdbc._


class ScalikeTaskRepository extends ScalikeEntityRepository[Task] with TaskRepository {

  private def toTask(rs: WrappedResultSet): Task = {
    Task(
      TaskId(rs.int("id")),
      rs.string("title"),
      rs.stringOpt("content"),
      rs.jodaDateTimeOpt("deadline_at"),
      rs.jodaDateTime("created_at"),
      rs.jodaDateTimeOpt("completed_at")
    )
  }

  /** ${inheritDoc} */
  override def find(taskIds: Seq[TaskId])(implicit context: Context): List[Task] = {
    withSession(context) { implicit session =>
      val ids = taskIds.map(_.value)
      sql"SELECT * FROM tasks WHERE id IN (${ids})".map(toTask).list().apply()
    }
  }

  /** ${inheritDoc} */
  override def create(entity: Task)(implicit context: Context): Task = {
    withSession(context) { implicit session =>
      val title = entity.title
      val content = entity.content
      val deadlineAt = entity.deadlineAt
      val completedAt = entity.completedAt
      val createdAt = entity.createdAt
      val updatedAt = createdAt
      val id = sql"""
          INSERT INTO tasks (title, content, deadline_at, completed_at, created_at, updated_at)
               VALUES (${title}, ${content}, ${deadlineAt}, ${completedAt}, ${createdAt}, ${updatedAt})
         """.updateAndReturnGeneratedKey.apply()

      entity.copy(id = TaskId(id.toInt))
    }
  }

  /** ${inheritDoc} */
  override def find(key: Key)(implicit context: Context): Option[Value] = {
    withSession(context) { implicit session =>
      val id = key.value
      sql"SELECT * FROM tasks WHERE id = ${id}".map(toTask).headOption.apply()
    }
  }

  /** ${inheritDoc} */
  override def store(key: Key, value: Value)(implicit context: Context): Unit = {
    // NOTE: createdAtは呼び出し元が管理してるのに対して、updatedAtはリポジトリ内で意識している
    // TODO: updatedAtは(現状は)RDBMS固有の情報なので問題ないと思うが、このような差異がでるときの一般的な対処を調査すること
    withSession(context) { implicit session =>
      val id = key.value
      val title = value.title
      val content = value.content
      val deadlineAt = value.deadlineAt
      val completedAt = value.completedAt
      val createdAt = value.createdAt
      // TODO: JavaのTimestamp型になるのでTZの意識は不要になるはずだが、念のため時間がずれないか確認すること
      val updatedAt = DateTime.now(DateTimeZone.UTC)

      // TODO: 更新した項目だけUPDATEしたほうが良いかも？要検討

      sql"""UPDATE tasks
             SET title = ${title},
                 content = ${content},
                 deadline_at = ${deadlineAt},
                 completed_at = ${completedAt},
                 created_at = ${createdAt},
                 updated_at = ${updatedAt}
           WHERE id = ${id}
      """.update.apply()
    }
  }

  /** ${inheritDoc} */
  override def remove(key: Key)(implicit context: Context): Unit = {
    val id = key.value
    sql"DELETE FROM tasks WHERE id = ${id}".update()
  }
}
