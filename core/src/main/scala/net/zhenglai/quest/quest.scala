package net.zhenglai

package object quest {

  // TODO: how to add static method to String 
  implicit class RichString(val str: String) extends AnyVal {
    def empty: String = ""
  }

  object Str {
    val empty = ""
  }

}
