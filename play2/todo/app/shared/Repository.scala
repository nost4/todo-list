package shared

import java.util.UUID


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
    * 指定したキーで値を保存する
    * @param key キー
    * @param value 値
    */
  def store(key: Key, value: Value): Unit
}


/**
  * エンティティのリポジトリ
 *
  * @tparam E エンティティの型
  */
abstract class EntityRepository[E<: Entity] extends Repository[E#ID, E] {

  /**
    * 次の識別子を割り当てる
    * @return 次の識別子
    */
  def nextIdentity(): E#ID

  /**
    * エンティティを保存する
    * @param entity エンティティ
    */
  def store(entity: E): Unit = store(entity.id, entity)

  /**
    * UUIDを生成する
    * @return UUID
    */
  protected def generateUUID(): UUID = UUID.randomUUID()
}
