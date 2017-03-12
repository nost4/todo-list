package infrastructures.convert

import play.api.libs.json._

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


trait DateTimeConverter {
  implicit val dateTimeReads: Reads[DateTime] = DateTimeConverter.DateTimeReads
  implicit val dateTimeWrites: Writes[DateTime] = DateTimeConverter.DateTimeWrites
}


object DateTimeConverter {
  private val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
  private val DateTimeReads = Reads[DateTime](js =>
    js.validate[String].map[DateTime](datetime =>
      DateTime.parse(datetime, DateTimeFormat.forPattern(dateFormat))
    )
  )

  private val DateTimeWrites = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = JsString(d.toString())
  }
}
