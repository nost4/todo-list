package controllers

import javax.inject._

import play.api._
import play.api.libs.json._
import play.api.mvc._
import exceptions.{JsonParseException, ResourceNotFoundException}
import infrastructures.convert._
import models.{User, UserId, UserRepository}
import services.UserServiceFactory


@Singleton
class UserController @Inject()(
  userRepository: UserRepository,
  userServiceFactory: UserServiceFactory
) extends Controller with UserConverter {

  // ----------------------------------------------------------------------------------------------
  // ユーザ関連のAPI
  // ----------------------------------------------------------------------------------------------

  case class AddUserRequest(name: String)
  case class PatchUserRequest(name: Option[String])

  private implicit val addUserRequestReads = Json.reads[AddUserRequest]
  private implicit val patchUserRequestReads = Json.reads[PatchUserRequest]

  private implicit val jsonParseExceptionWrites = Json.writes[JsonParseException]
  private implicit val resourceNotFoundExceptionWrites = Json.writes[ResourceNotFoundException]

  def userList() = Action {
    val users = userRepository.findAll()
    Ok(JsObject(Map(
      "total" -> JsNumber(users.length),
      "users" -> Json.toJson(users)
    )))
  }

  // TODO: getOrElse あたりの書き方が汚いので、良い書き方がないか確認すること
  private[this] def jsonAction[TRequest](request: Request[AnyContent], reads: Reads[TRequest])(action: TRequest => Result): Result = {
    request.body.asJson.map { json =>
      Json.fromJson[TRequest](json)(reads).map(requestData => {
        action(requestData)
      }).getOrElse(BadRequest(Json.toJson(JsonParseException())))
    }.getOrElse(BadRequest(Json.toJson(JsonParseException())))
  }

  private[this] def withUser(id: Int)(action: User => Result): Result = {
    userRepository.find(UserId(id)).map(user =>
      action(user)
    ).getOrElse(NotFound(Json.toJson(ResourceNotFoundException("user", id))))
  }

  def addUser() = Action { request =>
    jsonAction[AddUserRequest](request, addUserRequestReads) { addUserRequest =>
      val service = userServiceFactory.create(userRepository)
      val user = service.createUser(addUserRequest.name)
      Ok(Json.toJson(user))
    }
  }

  def getUser(id: Int) = Action {
    withUser(id) { user => Ok(Json.toJson(user)) }
  }

  def patchUser(id: Int) = Action { request =>
    def applyPatch(user: User, patchUserRequest: PatchUserRequest): User = {
      def rename(name: Option[String]): User = name match {
        case Some(n) => user.rename(n)
        case None => user
      }

      // NOTE: 項目が増えたらここでチェーンする
      rename(patchUserRequest.name)
    }

    jsonAction[PatchUserRequest](request, patchUserRequestReads) { patchUserRequest =>
      withUser(id) { user =>
        val patchedUser = applyPatch(user, patchUserRequest)
        userRepository.update(patchedUser)
        Ok(Json.toJson(patchedUser))
      }
    }
  }
}
