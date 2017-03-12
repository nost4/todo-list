package models

import org.joda.time.DateTime
import shared.Entity


/**
  * タスク識別子
  *
  * @param value 識別子の値
  */
case class TaskId(value: String)


/**
  * タスク(TODO)
  * @param id タスク識別子
  * @param title タイトル
  * @param content 内容
  * @param deadlineAt 期日
  * @param createdAt 作成日
  * @param completedAt 完了日
  */
case class Task(
  id: TaskId,
  title: String,
  content: Option[String],
  deadlineAt: Option[DateTime],
  createdAt: DateTime,
  completedAt: Option[DateTime]
) extends Entity {

  /** ${inheritDoc} */
  override type ID = TaskId

  /** タイトルを変更する */
  def withTitle(title: String): Task = copy(title = title)

  /** 内容を変更する */
  def withContent(content: String): Task = copy(content = Some(content))

  /** 期日を変更する */
  def withDeadline(deadlineAt: DateTime): Task = copy(deadlineAt = Some(deadlineAt))

  /** 期日を削除する */
  def removeDeadline(): Task = copy(deadlineAt = None)

  /** 完了日を変更する */
  def withCompleted(completedAt: DateTime): Task = copy(completedAt = Some(completedAt))

  /** 完了日を削除する */
  def removeCompleted(): Task = copy(completedAt = None)
}
