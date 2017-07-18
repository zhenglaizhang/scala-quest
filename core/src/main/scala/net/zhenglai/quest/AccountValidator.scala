package net.zhenglai.quest

import scala.util.Try

import cats.data.Validated
import cats.syntax.either._

case class Account(name: String, age: Int)

object AccountValidator {
  type FormData = Map[String, String]
  type ErrorsOr[A] = Either[List[String], A]
  type AllErrorsOr[A] = Validated[List[String], A]

  def getValue(name: String)(data: FormData): ErrorsOr[String] =
    data.get(name)
      .toRight(List(s"$name field not specified"))

  def parseInt(str: String): ErrorsOr[Int] =
    Try(str.toInt).toEither.leftMap(_ => List(s"$str can not be parsed as an int"))

  val getName = getValue("name") _

  def readName(data: FormData): ErrorsOr[String] = getName(data)
}
