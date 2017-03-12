package infrastructures.inmemory

import shared.{Entity, EntityRepository, Repository}

import scala.collection.mutable


/**
  * (動作確認用) インメモリリポジトリの基底クラス
  * @tparam K キー値の型
  * @tparam V 値の型
  */
class InMemoryRepository[K, V] extends Repository[K, V] {
  /** エンティティのマップ、エンティティの識別子をキーとして管理 */
  private[this] val _entries = mutable.HashMap.empty[Key, Value]

  /**
    * エンティティのマップを取得する、 _entitiesの直更新を避けるためimmutable化
    * @return エンティティのマップ
    */
  def entries: Map[Key, Value] = _entries.toMap

  /** ${inheritDoc} */
  override def find(key: Key): Option[Value] = _entries.find(_._1 == key).map(_._2)

  /** ${inheritDoc} */
  override def store(key: Key, value: Value): Unit = _entries.put(key, value)
}


/**
  * (動作確認用) インメモリのエンティティリポジトリ基底クラス
  * @tparam E エンティティの型
  */
abstract class InMemoryEntityRepository[E <: Entity] extends EntityRepository[E] {
  /**
    * リポジトリ、実際の管理は汎用リポジトリに委譲する
    */
  private[this] val _repository = new InMemoryRepository[E#ID, E]()

  /**
    * エンティティのマップを取得する、 _entitiesの直更新を避けるためimmutable化
    * @return エンティティのマップ
    */
  protected def entities: Map[E#ID, E] = _repository.entries

  /** ${inheritDoc} */
  override def find(id: E#ID): Option[E] = _repository.find(id)

  /** ${inheritDoc} */
  override def store(id: E#ID, entity: E): Unit = _repository.store(id, entity)
}
