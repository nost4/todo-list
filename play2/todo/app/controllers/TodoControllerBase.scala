package controllers

import scala.util.{Failure, Success, Try}
import exceptions.{JsonParseException, ResourceNotFoundException}
import infrastructures.inmemory.InMemoryIOContext
import models.{User, UserId, UserRepository}
import play.api.libs.json.{Json, Reads}
import play.api.mvc._
import shared.IOContext


// TODO: コンテキスト周りの処理、実装方針を再検討すること


trait IOContextHelper {
  def create(): IOContext
  def success(context: IOContext)
  def failure(context: IOContext)
}


object InMemoryContextHelper extends IOContextHelper {
  override def create(): IOContext = InMemoryIOContext
  override def success(context: IOContext): Unit = {}
  override def failure(context: IOContext): Unit = {}
}


abstract class TodoControllerBase(userRepository: UserRepository, contextHelper: IOContextHelper) extends Controller {
  type Test

  protected implicit val jsonParseExceptionWrites = Json.writes[JsonParseException]
  protected implicit val resourceNotFoundExceptionWrites = Json.writes[ResourceNotFoundException]

  protected def jsonAction[TRequest](request: Request[AnyContent], reads: Reads[TRequest])(action: TRequest => Result): Result = {
    request.body.asJson.map { json =>
      Json.fromJson[TRequest](json)(reads).map(requestData => {
        action(requestData)
      }).getOrElse(BadRequest(Json.toJson(JsonParseException())))
    }.getOrElse(BadRequest(Json.toJson(JsonParseException())))
  }

  protected def withContext[T](action: IOContext => T): T = {
    val context = contextHelper.create()
    Try(action(context)) match {
      case Success(r) =>
        contextHelper.success(context)
        r
      case Failure(e) =>
        contextHelper.failure(context)
        throw e
    }
  }

  protected[this] def withUser(id: Int)(action: User => Result)(implicit context: IOContext): Result = {
    userRepository.find(UserId(id)).map(user =>
      action(user)
    ).getOrElse(NotFound(Json.toJson(ResourceNotFoundException("user", id))))
  }
}
