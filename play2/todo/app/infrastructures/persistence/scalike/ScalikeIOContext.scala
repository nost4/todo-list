package infrastructures.persistence.scalike

import scalikejdbc.DBSession
import shared.IOContext


/**
  * ScalikeJDBC用のIOコンテキスト
  * @param session DBセッション
  */
case class ScalikeIOContext(session: DBSession) extends IOContext
