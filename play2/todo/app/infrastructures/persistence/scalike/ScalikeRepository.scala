package infrastructures.persistence.scalike


import shared.{Entity, EntityRepository, Repository}


/**
  * ScalikeJDBCリポジトリの基底クラス
  * @tparam K キー値の型
  * @tparam V 値の型
  */
abstract class ScalikeRepository[K, V] extends Repository[K, V] with ScalikeIOContextMixin {
}


/**
  * ScalikeJDBCエンティティリポジトリの基底クラス
  * @tparam E エンティティの型
  */
abstract class ScalikeEntityRepository[E <: Entity] extends EntityRepository[E] with ScalikeIOContextMixin {
}

