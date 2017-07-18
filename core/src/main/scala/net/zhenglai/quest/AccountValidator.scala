package net.zhenglai.quest

import cats.Cartesian
import cats.data.Validated
import cats.instances.list._
import cats.syntax.cartesian._
import cats.syntax.either._

case class Account(name: String, age: Int)

object AccountValidator {
  type FormData = Map[String, String]
  type ErrorsOr[A] = Either[List[String], A]
  type AllErrorsOr[A] = Validated[List[String], A]

  def getValue(name: String)(data: FormData): ErrorsOr[String] =
    data.get(name)
      .toRight(List(s"$name field not specified"))

  def parseInt(name: String)(data: String): ErrorsOr[Int] =
    Right(data)
      .flatMap(s => Either.catchOnly[NumberFormatException](s.toInt))
      .leftMap(_ => List(s"$name must be an integer"))

  def nonBlank(name: String)(data: String): ErrorsOr[String] =
    data.asRight[List[String]]
      .ensure(List(s"$name cannot be blank"))(_.nonEmpty)

  def nonNegative(name: String)(data: Int): ErrorsOr[Int] =
    data.asRight[List[String]]
      .ensure(List(s"$name cannot be non-negative"))(_ >= 0)

  val getName = getValue("name") _

  def readName(data: FormData): ErrorsOr[String] =
    getValue("name")(data)
      .flatMap(nonBlank("name"))

  def readAge(data: FormData): ErrorsOr[Int] =
    getValue("age")(data)
      .flatMap(nonBlank("age"))
      .flatMap(parseInt("age"))
      .flatMap(nonNegative("age"))

  def readUser(data: FormData): AllErrorsOr[Account] =
    Cartesian[AllErrorsOr].product(
      readName(data).toValidated,
      readAge(data).toValidated
    ).map(Account.tupled)

  def readUserV2(data: FormData): AllErrorsOr[Account] =
    (
      readName(data).toValidated |@|
      readAge(data).toValidated
    ).map(Account.apply)
}
