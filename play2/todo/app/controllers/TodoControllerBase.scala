package controllers

import exceptions.{JsonParseException, ResourceNotFoundException}
import models.{User, UserId, UserRepository}
import play.api.libs.json.{Json, Reads}
import play.api.mvc.{AnyContent, Controller, Request, Result}


abstract class TodoControllerBase(userRepository: UserRepository) extends Controller {
  protected implicit val jsonParseExceptionWrites = Json.writes[JsonParseException]
  protected implicit val resourceNotFoundExceptionWrites = Json.writes[ResourceNotFoundException]

  protected def jsonAction[TRequest](request: Request[AnyContent], reads: Reads[TRequest])(action: TRequest => Result): Result = {
    request.body.asJson.map { json =>
      Json.fromJson[TRequest](json)(reads).map(requestData => {
        action(requestData)
      }).getOrElse(BadRequest(Json.toJson(JsonParseException())))
    }.getOrElse(BadRequest(Json.toJson(JsonParseException())))
  }

  protected[this] def withUser(id: Int)(action: User => Result): Result = {
    userRepository.find(UserId(id)).map(user =>
      action(user)
    ).getOrElse(NotFound(Json.toJson(ResourceNotFoundException("user", id))))
  }
}
