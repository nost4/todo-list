package infrastructures.inmemory

import models.{User, UserRepository}


/**
  * (動作確認用) ユーザリポジトリのインメモリ実装
  */
class InMemoryUserRepository extends InMemoryEntityRepository[User] with UserRepository {
  /** ${inheritDoc} */
  override def findAll(): List[User] = entities.values.toList
}
