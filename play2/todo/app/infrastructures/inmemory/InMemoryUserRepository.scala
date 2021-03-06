package infrastructures.inmemory

import javax.inject.Singleton

import models.{User, UserId, UserRepository}


/**
  * (動作確認用) ユーザリポジトリのインメモリ実装
  */
@Singleton
class InMemoryUserRepository extends InMemoryEntityRepository[User] with UserRepository {
  /** ${inheritDoc} */
  override def create(user: User)(implicit context: Context): User = {
    createNew(idHint => user.copy(id = UserId(idHint)))
  }

  /** ${inheritDoc} */
  override def findAll()(implicit context: Context): List[User] = {
    entities.values.toList
  }
}
