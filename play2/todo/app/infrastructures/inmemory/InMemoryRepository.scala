package infrastructures.inmemory

import shared.{Entity, Repository}

import scala.collection.mutable


/**
  * (動作確認用) インメモリリポジトリの基底クラス
  * @tparam E エンティティの型
  */
abstract class InMemoryRepository[E <: Entity] extends Repository[E] {
  protected[this] val _entities = mutable.HashMap.empty[E#ID, E]

  /** ${inheritDoc} */
  override def find(id: E#ID): Option[E] = _entities.find(_._1 == id).map(_._2)

  /** ${inheritDoc} */
  override def store(entity: E): Unit = _entities.put(entity.id, entity)
}
