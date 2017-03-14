package infrastructures.persistence.scalike

import scalikejdbc.DBSession
import shared.{Entity, EntityRepository, IOContext, Repository}


/**
  * ScalikeJDBCリポジトリ用のミックスイン
  */
trait ScalikeRepositoryMixin {

  /**
    * ScalikeJDBC用のIOコンテキストからDBセッションを取得する
    * @param context ScalikeJDBC用のIOコンテキスト
    * @param f セッションを使用する操作
    * @tparam A 戻り値の型
    * @return fの戻り値
    */
  protected def withSession[A](context: IOContext)(f: DBSession => A): A = context match {
    case ScalikeIOContext(session) => f(session)
    case _ => throw new IllegalStateException(s"unexpected context type, expected=ScalikeIOContext, actual=${context.getClass.getSimpleName}")
  }
}


/**
  * ScalikeJDBCリポジトリの基底クラス
  * @tparam K キー値の型
  * @tparam V 値の型
  */
abstract class ScalikeRepository[K, V] extends Repository[K, V] with ScalikeRepositoryMixin {
}


/**
  * ScalikeJDBCエンティティリポジトリの基底クラス
  * @tparam E エンティティの型
  */
abstract class ScalikeEntityRepository[E <: Entity] extends EntityRepository[E] with ScalikeRepositoryMixin {
}

