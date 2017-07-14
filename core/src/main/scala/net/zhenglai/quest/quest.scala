package net.zhenglai

package object quest {

  // TODO: how to add static method to String 
  implicit class RichString(val str: String) extends AnyVal {
    def empty: String = ""
  }

  object Str {
    val empty = ""
  }

  // hmm, Throwable is an extremely broad supertype
  type Result[A] = Either[Throwable, A]

  type LoginResult = Either[LoginError, User]
}
