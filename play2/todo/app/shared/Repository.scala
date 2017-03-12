package shared


/**
  * リポジトリ
  *
  * @tparam K キー値の型
  * @tparam V 値の型
  */
trait Repository[K, V] {
  type Key = K
  type Value = V

  /**
    * 指定したキーの値を検索する
    * @param key キー
    * @return 値
    */
  def find(key: Key): Option[Value]

  /**
    * 指定したキーで値を更新する
    * @param key キー
    * @param value 値
    */
  def store(key: Key, value: Value): Unit

  /**
    * 指定したキーの値を削除する
    * @param key キー
    */
  def remove(key: Key): Unit
}


/**
  * エンティティのリポジトリ
 *
  * @tparam E エンティティの型
  */
abstract class EntityRepository[E<: Entity] extends Repository[E#ID, E] {

  /**
    * エンティティを作成する
    * @param entity エンティティ(仮)
    * @return エンティティ(確定)
    */
  def create(entity: E): E

  /**
    * エンティティを更新する
    * @param entity エンティティ
    */
  def update(entity: E): Unit = store(entity.id, entity)
}
