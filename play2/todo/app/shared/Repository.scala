package shared


/**
  * エンティティのリポジトリ
 *
  * @tparam E エンティティの型
  */
trait Repository[E<: Entity] {
  /**
    * エンティティを検索する
    * @param id エンティティの識別子
    * @return エンティティ、未登録の場合はNone
    */
  def find(id: E#ID): Option[E]

  /**
    * エンティティを保存する
    * @param entity エンティティ
    */
  def store(entity: E): Unit
}
