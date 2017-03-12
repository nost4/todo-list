package exceptions


case class ResourceNotFoundException(resourceType: String, id: Int)
      extends Exception("resource not found, type='%s', id=%d".format(resourceType, id)) {
}
