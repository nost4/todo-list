package controllers

import javax.inject._

import play.api._
import play.api.libs.json._
import play.api.mvc._
import exceptions.{JsonParseException, ResourceNotFoundException}
import infrastructures.convert._
import infrastructures.persistence.h2db.H2IOContext
import models.{User, UserId, UserRepository}
import play.api.db.slick.DatabaseConfigProvider
import services.UserServiceFactory
import shared.IOContext
import slick.driver.JdbcProfile


@Singleton
class UserController @Inject()(
  userRepository: UserRepository,
  userServiceFactory: UserServiceFactory,
  dbConfigProvider: DatabaseConfigProvider
) extends TodoControllerBase(userRepository, H2IOContextHelper(dbConfigProvider))
    with UserConverter {

  // ----------------------------------------------------------------------------------------------
  // ユーザ関連のAPI
  // ----------------------------------------------------------------------------------------------

  case class AddUserRequest(name: String)
  case class PatchUserRequest(name: Option[String])

  private implicit val addUserRequestReads = Json.reads[AddUserRequest]
  private implicit val patchUserRequestReads = Json.reads[PatchUserRequest]

  def listUsers() = Action {
    withContext { implicit context =>
      val users = userRepository.findAll()
      Ok(JsObject(Map(
        "total" -> JsNumber(users.length),
        "users" -> Json.toJson(users)
      )))
    }
  }

  def addUser() = Action { request =>
    withContext { implicit context =>
      jsonAction[AddUserRequest](request, addUserRequestReads) { addUserRequest =>
        val service = userServiceFactory.create(userRepository)
        val user = service.createUser(addUserRequest.name)
        Created(Json.toJson(user))
      }
    }
  }

  def getUser(id: Int) = Action {
    withContext { implicit context =>
      withUser(id) { user => Ok(Json.toJson(user)) }
    }
  }

  def patchUser(id: Int) = Action { request =>
    withContext { implicit context =>
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
}
