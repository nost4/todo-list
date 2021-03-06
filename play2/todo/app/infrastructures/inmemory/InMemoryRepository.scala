package infrastructures.inmemory

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import scala.collection.convert.decorateAsScala._
import shared.{Entity, EntityRepository, Repository}


/**
  * (動作確認用) インメモリリポジトリの基底クラス
  * @tparam K キー値の型
  * @tparam V 値の型
  */
class InMemoryRepository[K, V] extends Repository[K, V] {
  /** エンティティのマップ、エンティティの識別子をキーとして管理 */
  private[this] val _entries = new ConcurrentHashMap[K, V]().asScala

  /**
    * エンティティのマップを取得する、 _entitiesの直更新を避けるためimmutable化
    * @return エンティティのマップ
    */
  def entries: Map[Key, Value] = _entries.toMap

  /** ${inheritDoc} */
  override def find(key: Key)(implicit context: Context): Option[Value] = {
    _entries.find(_._1 == key).map(_._2)
  }

  /** ${inheritDoc} */
  override def store(key: Key, value: Value)(implicit context: Context): Unit = {
    _entries.put(key, value)
  }

  /** ${inheritDoc} */
  override def remove(key: Key)(implicit context: Context): Unit = {
    _entries.remove(key)
  }
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
    * エンティティ識別子の管理カウンタ
    */
  private[this] val _counter = new AtomicInteger()

  /**
    * エンティティのマップを取得する、 _entitiesの直更新を避けるためimmutable化
    * @return エンティティのマップ
    */
  protected def entities: Map[E#ID, E] = _repository.entries

  /**
    * 新規インスタンスを作成する
    * @param f エンティティ作成関数
    * @return エンティティ
    */
  protected def createNew(f: Int => E)(implicit context: Context): E = {
    _repository.synchronized {
      val idHint = _counter.incrementAndGet()
      val entity = f(idHint)
      _repository.store(entity.id, entity)

      entity
    }
  }

  /** ${inheritDoc} */
  override def find(id: E#ID)(implicit context: Context): Option[E] = _repository.find(id)

  /** ${inheritDoc} */
  override def store(id: E#ID, entity: E)(implicit context: Context): Unit = {
    _repository.synchronized {
      if (!_repository.entries.contains(id)) {
        throw new IllegalStateException("cannot store new entity")
      }
      _repository.store(id, entity)
    }
  }

  /** ${inheritDoc} */
  override def remove(key: Key)(implicit context: Context): Unit = {
    _repository.remove(key)
  }
}
