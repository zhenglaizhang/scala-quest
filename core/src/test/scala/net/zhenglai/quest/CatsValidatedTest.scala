package net.zhenglai.quest

import cats.Cartesian
import cats.data.Validated
import cats.data.Validated.Invalid
import org.scalatest.{FunSuite, Matchers}

class CatsValidatedTest extends FunSuite with Matchers {

  test("Validated as cartesian but not monad") {
    import cats.instances.list._ // semigroup for list
    Cartesian[AllErrorsOr].product(
      Validated.invalid(List("error 1")),
      Validated.invalid(List("error 2"))
    ) should be(Invalid(List("error 1", "error 2")))

    // with apply
    val valid: Validated[Nothing, Int] = Validated.Valid(12)
    // with smart constructor
    val valid2: Validated[String, Int] = Validated.valid[String, Int](12)

    // with extension syntax
    import cats.syntax.validated._
    123.valid[String]
    "badness".invalid[Int]

    // from other sources
    val foo: Validated[NumberFormatException, Int] = Validated.catchOnly[NumberFormatException]("foo".toInt)
    val bar: Validated[Throwable, Nothing] = Validated.catchNonFatal(sys.error("Badness"))
    // fromTry
    // fromEither
    // fromOption

    import cats.instances.vector._
    import cats.syntax.cartesian._

    (
      Vector(404).invalid[Int] |@|
        Vector(500).invalid[Int]
      ).tupled should be(Validated.invalid(Vector(404, 500)))

    import cats.data.NonEmptyList
    (
      NonEmptyList.of("error 1").invalid[Int] |@|
        NonEmptyList.of("error 2").invalid[Int]
      ).tupled should be(NonEmptyList.of("error 1", "error 2").invalid[Int])

    // extract values
    "fail".invalid[Int].getOrElse(0) should be(0)
    "fail".invalid[Int].fold(_ + "!!!", _.toString) should be("fail!!!")
    123.valid[String].ensure("Negative!")(_ >= 0) should be(123.valid[String])
    -2.valid[String].ensure("Negative!")(_ >= 0) should be("Negative!".invalid[String])
  }

  test("Validate Account") {
    import AccountValidator._
    readUserV2(Map("name" -> "zhenglai", "age" -> "12")) should be(Validated.valid(Account("zhenglai", 12)))
  }
}
