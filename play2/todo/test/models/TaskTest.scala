package models

import org.joda.time.DateTime
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.mockito.Mockito._

class TaskTest extends PlaySpec with MockitoSugar {
  def mockTask(): Task = {
    // NOTE: StringとDatetimeがmockできない
    Task(mock[TaskId], "", mock[Option[String]],
      mock[Option[DateTime]], new DateTime(0), mock[Option[DateTime]])
  }

  "Task#withTitle" should {
    "be immutable" in {
      val task = mockTask().copy(title = "initial title")
      task.withTitle("replaced title").title mustBe "replaced title"
      task.title mustBe "initial title"
    }
  }

  "Task#content" should {
    "be immutable" in {
      val task = mockTask().copy(content = Some("content"))
      task.withContent("replaced content").content mustBe Some("replaced content")
      task.content mustBe Some("content")
    }
  }

  "Task#deadlineAt" should {
    "be immutable" in {
      val deadline = new DateTime(1, 1, 1, 0, 0, 0, 0)
      val task = mockTask().copy(deadlineAt = Some(deadline))
      task.withDeadline(new DateTime(2017, 3, 12, 0, 0, 0)).deadlineAt mustBe Some(new DateTime(2017, 3, 12, 0, 0, 0))
      task.deadlineAt mustBe Some(deadline)

      task.removeDeadline().deadlineAt mustBe None
      task.deadlineAt mustBe Some(deadline)
    }
  }

  "Task#completedAt" should {
    "be immutable" in {
      val completedAt = new DateTime(2017, 3, 12, 0, 0, 0)
      val task = mockTask().copy(completedAt = Some(completedAt))

      task.removeCompleted().completedAt mustBe None
      task.completedAt mustBe Some(completedAt)

      val fixedCompletedAt = new DateTime(2017, 3, 1, 0, 0, 0)
      task.removeCompleted().withCompleted(fixedCompletedAt).completedAt mustBe Some(fixedCompletedAt)
      task.completedAt mustBe Some(completedAt)
    }
  }
}
