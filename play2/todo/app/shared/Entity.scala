package shared


/**
  * エンティティ
  */
trait Entity {
  /**
    * エンティティ識別子の型
    */
  type ID

  /**
    * エンティティ識別子を取得する
    * @return エンティティ識別子
    */
  def id: ID
}
