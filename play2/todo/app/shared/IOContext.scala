package shared


/**
  * IOコンテキスト
  */
trait IOContext


/**
  * IOコンテキストのヘルパー
  */
trait IOContextHelper {
  /**
    * 読み取り専用のコンテキストで処理を実行する
    */
  def withReadOnlyContext[A](f: IOContext => A): A

  /**
    * トランザクションのコンテキストで処理を実行する
    */
  def withTransactionContext[A](f: IOContext => A): A
}
