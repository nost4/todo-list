package models

import java.util.UUID

import shared.Entity


/**
  * ユーザ識別子
  *
  * @param value 識別子の値
  */
case class UserId(value: String)


/**
  * ユーザエンティティ
  * @param id ユーザ識別子
  * @param name ユーザ名
  */
case class User(id: UserId, name: String) extends Entity {

  /** ${inheritDoc} */
  override type ID = UserId

  /**
    * ユーザ名を変更する
 *
    * @param name 変更後の名前
    * @return 変更後のエンティティ
    */
  def rename(name: String): User = copy(name=name)
}
