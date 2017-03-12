package infrastructures.convert

import models.{User, UserId}
import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._


trait UserConverter {
  implicit val userReads: Reads[User] = UserConverter.UserReads
  implicit val userWrites: Writes[User] = UserConverter.UserWrites
}


object UserConverter {
  private val UserReads: Reads[User] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String]
    )((id, name) => User(UserId(id), name))

  private val UserWrites: Writes[User] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String]
    )(user => (user.id.value, user.name))
}
