package controllers

import play.api.libs.json.{Json, Reads}
import play.api.mvc._

import exceptions.{JsonParseException, ResourceNotFoundException}
import models.{User, UserId, UserRepository}
import shared.{IOContext, IOContextHelper}


// TODO: コンテキスト周りの処理、実装方針を再検討すること


abstract class TodoControllerBase(userRepository: UserRepository, contextHelper: IOContextHelper) extends Controller {

  protected implicit val jsonParseExceptionWrites = Json.writes[JsonParseException]
  protected implicit val resourceNotFoundExceptionWrites = Json.writes[ResourceNotFoundException]

  protected def jsonAction[TRequest](request: Request[AnyContent], reads: Reads[TRequest])(action: TRequest => Result): Result = {
    request.body.asJson.map { json =>
      Json.fromJson[TRequest](json)(reads).map(requestData => {
        action(requestData)
      }).getOrElse(BadRequest(Json.toJson(JsonParseException())))
    }.getOrElse(BadRequest(Json.toJson(JsonParseException())))
  }

  protected def withReadOnlyContext[T](action: IOContext => T): T = {
    contextHelper.withReadOnlyContext(action)
  }

  protected def withTransactionContext[T](action: IOContext => T): T = {
    contextHelper.withTransactionContext(action)
  }

  protected[this] def withUser(id: Int)(action: User => Result)(implicit context: IOContext): Result = {
    userRepository.find(UserId(id)).map(user =>
      action(user)
    ).getOrElse(NotFound(Json.toJson(ResourceNotFoundException("user", id))))
  }
}
