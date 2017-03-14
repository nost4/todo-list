package infrastructures.persistence.scalike

import scalikejdbc._
import shared.{IOContext, IOContextHelper}


/**
  * ScalikeJDBC用のIOコンテキスト
  * @param session DBセッション
  */
case class ScalikeIOContext(session: DBSession) extends IOContext


/**
  * ScalikeJDBC用のIOコンテキストヘルパー
  */
case class ScalikeIOContextHelper() extends IOContextHelper with ScalikeIOContextMixin {
  /** ${inheritDoc} */
  override def withReadOnlyContext[A](f: (IOContext) => A): A = {
    DB readOnly { session =>
      f(ScalikeIOContext(session))
    }
  }

  /** ${inheritDoc} */
  override def withTransactionContext[A](f: (IOContext) => A): A = {
    DB localTx { session =>
      f(ScalikeIOContext(session))
    }
  }
}
