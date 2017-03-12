package shared


/**
  * リポジトリ
  * @tparam K キー値の型
  * @tparam V 値の型
  */
trait Repository[K, V] {
  /**
    * 指定したキーの値を検索する
    * @param key キー
    * @return 値
    */
  def find(key: K): Option[V]

  /**
    * 指定したキーで値を保存する
    * @param key キー
    * @param value 値
    */
  def store(key: K, value: V): Unit
}


/**
  * エンティティのリポジトリ
 *
  * @tparam E エンティティの型
  */
abstract class EntityRepository[E<: Entity] extends Repository[E#ID, E] {
  /**
    * エンティティを保存する
    * @param entity エンティティ
    */
  def store(entity: E): Unit = store(entity.id, entity)
}
