package infrastructures.persistence.scalike

import models.{User, UserId, UserRepository}
import scalikejdbc._

/**
  * ユーザリポジトリのScalikeJDBC実装
  */
class ScalikeUserRepository extends ScalikeEntityRepository[User] with UserRepository {
  private def toUser(rs: WrappedResultSet): User = {
    User(UserId(rs.int("id")), rs.string("name"))
  }

  /** ${inheritDoc} */
  override def findAll()(implicit context: Context): List[User] = {
    withSession(context) { implicit session =>
      sql"SELECT * FROM users".map(toUser).list.apply()
    }
  }

  /** ${inheritDoc} */
  override def create(entity: User)(implicit context: Context): User = {
    withSession(context) { implicit session =>
      val name = entity.name
      val id = sql"INSERT INTO users VALUES (${name})".updateAndReturnGeneratedKey.apply()

      entity.copy(id = UserId(id.toInt))
    }
  }

  /** ${inheritDoc} */
  override def find(key: Key)(implicit context: Context): Option[Value] = {
    withSession(context) { implicit session =>
      val id = key.value
      sql"SELECT * FROM users WHERE id = ${id}".map(toUser).headOption.apply()
    }
  }

  /** ${inheritDoc} */
  override def store(key: Key, value: Value)(implicit context: Context): Unit = {
    withSession(context) { implicit session =>
      val id = key.value
      val name = value.name
      sql"UPDATE users SET name = ${name} WHERE id = ${id}".update.apply()
    }
  }

  /** ${inheritDoc} */
  override def remove(key: Key)(implicit context: Context): Unit = {
    withSession(context) { implicit session =>
      val id = key.value
      sql"DELETE FROM users WHERE id = ${id}".update.apply()
    }
  }
}
