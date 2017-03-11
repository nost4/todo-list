package models

import shared.Repository


/**
  * ユーザエンティティのリポジトリ
  */
trait UserRepository extends Repository[User] {
  /**
    * すべてのユーザを検索する
    * @return すべてのユーザ
    */
  def findAll(): List[User]
}
