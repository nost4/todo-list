package infrastructures.inmemory

import shared.{IOContext, IOContextHelper}


/**
  * インメモリのIOコンテキスト
  * NOTE: I/F対応のための定義なので情報なし
  */
case object InMemoryIOContext extends IOContext


/**
  * インメモリのIOコンテキストヘルパー
  */
case class InMemoryIOContextHelper() extends IOContextHelper {
  /** ${inheritDoc} */
  override def withReadOnlyContext[A](f: (IOContext) => A): A = f(InMemoryIOContext)

  /** ${inheritDoc} */
  override def withTransactionContext[A](f: (IOContext) => A): A = f(InMemoryIOContext)
}
