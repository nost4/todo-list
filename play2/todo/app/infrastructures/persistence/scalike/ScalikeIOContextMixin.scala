package infrastructures.persistence.scalike

import scalikejdbc.DBSession
import shared.IOContext


/**
  * ScalikeJDBCのIOコンテキストミックスイン
  */
trait ScalikeIOContextMixin {

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

