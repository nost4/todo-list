package services

import models.{User, UserId, UserRepository}
import shared.IOContext


/**
  * ユーザ関連サービス
  */
trait UserService {

  /**
    * 新規ユーザを作成する
    * @param name ユーザ名
    * @return 新規ユーザ
    */
  def createUser(name: String): User
}


/**
  * ユーザ関連サービスの実装
  * @param userRepository ユーザのリポジトリ
  */
class UserServiceImpl(userRepository: UserRepository)(implicit context: IOContext) extends UserService {

  /** ${inheritDoc} */
  override def createUser(name: String): User = {
    userRepository.create(User(UserId(0), name))
  }
}


// TODO: 引数付きサービスのインジェクト方法を確認すること
trait UserServiceFactory {
  def create(userRepository: UserRepository)(implicit context: IOContext): UserService
}


class UserServiceFactoryImpl extends UserServiceFactory {
  def create(userRepository: UserRepository)(implicit context: IOContext): UserService = {
    new UserServiceImpl(userRepository)
  }
}

