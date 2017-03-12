package models

import shared.EntityRepository


/**
  * ユーザエンティティのリポジトリ
  */
trait UserRepository extends EntityRepository[User] {
  /**
    * すべてのユーザを検索する
    * @return すべてのユーザ
    */
  def findAll(): List[User]
}
