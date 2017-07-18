package net.zhenglai

import cats.data.{OptionT, Validated, Writer}

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

  type Logged[A] = Writer[Vector[String], A]
  // import cats.syntax.applicative._

  // `OptionT` is a monad transformer for `Option`
  // Option[List[A]]
  type ListOption[A] = OptionT[List, A]


  type AllErrorsOr[A] = Validated[List[String], A]

  def slowly[A](body: => A) = {
    try body finally Thread.sleep(100)
  }

  def factorial(n: Int): Int = {
    if (n == 0) 1
    else n * factorial(n - 1)
  }
}
