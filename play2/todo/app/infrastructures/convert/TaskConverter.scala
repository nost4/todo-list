package infrastructures.convert

import models.{Task, TaskId, User, UserId}
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._


trait TaskConverter {
  implicit val taskIdReads: Reads[TaskId] = TaskConverter.TaskIdReads
  implicit val taskIdWrites: Writes[TaskId] = TaskConverter.TaskIdWrites
  implicit val taskReads: Reads[Task] = TaskConverter.TaskReads
  implicit val taskWrites: Writes[Task] = TaskConverter.TaskWrites
}


object TaskConverter {
  private val TaskIdReads = Reads[TaskId](js =>
    js.validate[Int].map[TaskId](taskId => TaskId(taskId))
  )

  private val TaskIdWrites = new Writes[TaskId] {
    def writes(t: TaskId): JsValue = JsNumber(t.value)
  }

  private val TaskReads: Reads[Task] = (
    (JsPath \ "id").read[TaskId](TaskIdReads) and
    (JsPath \ "title").read[String] and
    (JsPath \ "content").readNullable[String] and
    (JsPath \ "deadlineAt").readNullable[DateTime] and
    (JsPath \ "createdAt").read[DateTime] and
    (JsPath \ "completedAt").readNullable[DateTime]
  )(Task)

  private val TaskWrites: Writes[Task] = (
    (JsPath \ "id").write[TaskId](TaskIdWrites) and
    (JsPath \ "title").write[String] and
    (JsPath \ "content").writeNullable[String] and
    (JsPath \ "deadlineAt").writeNullable[DateTime] and
    (JsPath \ "createdAt").write[DateTime] and
    (JsPath \ "completedAt").writeNullable[DateTime]
  )(unlift(Task.unapply))
}

