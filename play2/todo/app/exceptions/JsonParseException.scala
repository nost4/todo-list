package exceptions


case class JsonParseException(msg: String = "failed to parse json") extends Exception(msg) {
}


