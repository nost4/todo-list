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
) extends TodoControllerBase(userRepository) with UserConverter {

  // ----------------------------------------------------------------------------------------------
  // ユーザ関連のAPI
  // ----------------------------------------------------------------------------------------------

  case class AddUserRequest(name: String)
  case class PatchUserRequest(name: Option[String])

  private implicit val addUserRequestReads = Json.reads[AddUserRequest]
  private implicit val patchUserRequestReads = Json.reads[PatchUserRequest]

  def userList() = Action {
    val users = userRepository.findAll()
    Ok(JsObject(Map(
      "total" -> JsNumber(users.length),
      "users" -> Json.toJson(users)
    )))
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
