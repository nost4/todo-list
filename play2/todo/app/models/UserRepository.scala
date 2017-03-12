package models

import shared.EntityRepository


/**
  * ユーザエンティティのリポジトリ
  */
trait UserRepository extends EntityRepository[User] {

  /**
    * 次の識別子を割り当てる
    *
    * @return 次の識別子
    */
  override def nextIdentity(): UserId = UserId(generateUUID().toString)

  /**
    * すべてのユーザを検索する
 *
    * @return すべてのユーザ
    */
  def findAll(): List[User]
}
