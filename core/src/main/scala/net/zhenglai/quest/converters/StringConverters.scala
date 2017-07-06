package net.zhenglai.quest.converters

object StringConverters {

  implicit class StringAsBoolean(val string: String) extends AnyVal {
    def asBoolean = string.toUpperCase match {
      case "YES" => true
      case _ => false
    }
  }

}
