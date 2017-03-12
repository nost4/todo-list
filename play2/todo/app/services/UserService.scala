package services

import models.{User, UserRepository}


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
class UserServiceImpl(userRepository: UserRepository) extends UserService {

  /** ${inheritDoc} */
  override def createUser(name: String): User = {
    val userId = userRepository.nextIdentity()
    val user = User(userId, name)

    // リポジトリに保存
    userRepository.store(user)

    // 追加したユーザを返す
    user
  }
}
