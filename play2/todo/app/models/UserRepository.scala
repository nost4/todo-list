package models

import shared.EntityRepository


/**
  * ユーザエンティティのリポジトリ
  */
trait UserRepository extends EntityRepository[User] {

  /**
    * 新規ユーザを作成する
    * @param user ユーザ(仮)
    * @return ユーザ(確定)
    */
  def create(user: User): User

  /**
    * すべてのユーザを検索する
    *
    * @return すべてのユーザ
    */
  def findAll(): List[User]
}
