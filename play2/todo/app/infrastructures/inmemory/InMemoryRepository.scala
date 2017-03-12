package infrastructures.inmemory

import shared.{Entity, Repository}

import scala.collection.mutable


/**
  * (動作確認用) インメモリリポジトリの基底クラス
  * @tparam E エンティティの型
  */
abstract class InMemoryRepository[E <: Entity] extends Repository[E] {
  /** エンティティのマップ、エンティティの識別子をキーとして管理 */
  private[this] val _entities = mutable.HashMap.empty[E#ID, E]

  /**
    * エンティティのマップを取得する、 _entitiesの直更新を避けるためimmutable化
    * @return エンティティのマップ
    */
  protected def entities: Map[E#ID, E] = _entities.toMap

  /** ${inheritDoc} */
  override def find(id: E#ID): Option[E] = _entities.find(_._1 == id).map(_._2)

  /** ${inheritDoc} */
  override def store(entity: E): Unit = _entities.put(entity.id, entity)
}
