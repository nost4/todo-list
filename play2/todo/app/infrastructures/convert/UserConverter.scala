package infrastructures.convert

import models.{User, UserId}
import play.api.libs.json.{JsNumber, JsPath, Reads, Writes}
import play.api.libs.functional.syntax._


trait UserConverter {
  implicit val userIdReads: Reads[UserId] = UserConverter.UserIdReads
  implicit val userIdWrites: Writes[UserId] = UserConverter.UserIdWrites
  implicit val userReads: Reads[User] = UserConverter.UserReads
  implicit val userWrites: Writes[User] = UserConverter.UserWrites
}


object UserConverter {
  private val UserIdReads = Reads[UserId](js =>
    js.validate[Int].map[UserId](userId => UserId(userId))
  )

  private val UserIdWrites = new Writes[UserId] {
    override def writes(userId: UserId) = JsNumber(userId.value)
  }

  private val UserReads: Reads[User] = (
    (JsPath \ "id").read[UserId](UserIdReads) and
      (JsPath \ "name").read[String]
    )((id, name) => User(id, name))

  private val UserWrites: Writes[User] = (
    (JsPath \ "id").write[UserId](UserIdWrites) and
      (JsPath \ "name").write[String]
    )(user => (user.id, user.name))
}
