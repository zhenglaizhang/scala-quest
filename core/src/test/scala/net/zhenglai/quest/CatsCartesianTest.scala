package net.zhenglai.quest

import cats.Cartesian
import cats.instances.option._
import cats.syntax.cartesian._
import org.scalatest.{FunSuite, Matchers}

/*
Cartesian defines product function, which produces a pair of (A, B) wrapped in effect F[_] out of F[A] and F[B].
The symbolic alias for product is |@| also known as the applicative style.

Cartesian has a single law called associativity:
(F.product(fa, F.product(fb, fc)), F.product(F.product(fa, fb), fc))

 * [[Cartesian]] captures the idea of composing independent effectful values.
 * It is of particular interest when taken together with [[Functor]] - where [[Functor]]
 * captures the idea of applying a unary pure function to an effectful value,
 * calling `product` with `map` allows one to apply a function of arbitrary arity to multiple
 * independent effectful values.
 *
 * That same idea is also manifested in the form of [[Apply]], and indeed [[Apply]] extends both
 * [[Cartesian]] and [[Functor]] to illustrate this.
 */
class CatsCartesianTest extends FunSuite with Matchers {
  test("use Cartesian to join context") {
    Cartesian[Option].product(Some(1), Some(3)) should be(Some(1, 3))
    Cartesian[Option].product(None, Some(1)) should be(None)
  }

  test("join 3 or more contexts...") {
    Cartesian.tuple3(Option(1), Option(2), Option(3)) should be(Some((1, 2, 3)))
    Cartesian.tuple3(Option(1), None, Option(3)) should be(None)
  }

  test("mapN") {
    Cartesian.map4(Option(1), Option(2), Option(3), Option(4))(_ + _ + _ + _) should be(Some(10))

    Cartesian.map4(Option(1), Option(2), Option(3), Option.empty[Int])(_ + _ + _ + _) should be(None)
  }

  test("tie fighter with cartesian builder") {
    (Option(123) |@|
      Option(124) |@|
      Option(true) |@|
      Option("hello")
      ).tupled should be(Some((123, 124, true, "hello")))

    (
      Option(123) |@|
        Option(124) |@|
        Option(true) |@|
        Option("hello") |@|
        None
      ).tupled should be(None)

    (
      Option("Zhenglai") |@|
        Option("zhenglaizhang@hotmail.com")
      ).map(Person.apply) should be(Some(Person("Zhenglai", "zhenglaizhang@hotmail.com")))

    (
      Option("Zhenglai") |@|
        Option.empty[String]
      ).map(Person.apply) should be(None)
  }

  test("Cartesian for future") {
    // The two Futures start executing the moment we create them,
    // so they are already calculating results by the time we call product.
    // flatMap provides sequenঞal ordering, so product provides the same.
    // The only reason we get parallel execuঞon is because
    // our consঞtuent Futures start running before we call product.
    // This is equivalent to the classic create-thenflatmap paern

    //    val futurePair = Cartesian[Future].product(Future("hello"), Future(123))
    //    Await.result(futurePair, 1.second)
  }

  test("Cartesian applied to monads") {
    // like Option, List and Either are both monads.
    // To ensure consistent semanঞcs, Cats’ Monad (which extends Cartesian)
    // provides a standard definiঞon of product in terms of map and flatMap
    import cats.instances.list._
    Cartesian[List].product(List(1, 2), List(3, 4)) should be(List((1, 3), (1, 4), (2, 3), (2, 4)))

    import cats.instances.either._
    type ErrorOr[A] = Either[Vector[String], A]
    // still fail fast
    Cartesian[ErrorOr].product(
                                Left(Vector("error 1")),
                                Left(Vector("error 2"))
                              ) should be(Left(Vector("error 1")))
  }

  test("more") {
    import cats.instances.list._
    import cats.syntax.cartesian._
    import cats.syntax.option._
    (3.some |@| 5.some) map {
      _ - _
    } should be(Some(-2))
    (3.some |@| none[Int]) map {
      _ - _
    } should be(none[Int])
    (none[Int] |@| 3.some) map {
      _ - _
    } should be(none[Int])

    (List("ha", "heh", "hmm") |@| List("?", "!", ".")) map {
      _ + _
    }

    // Cartesian enables two operators, <* and *>, which are special cases of Apply[F].product

    1.some <* 2.some should be(1.some)
    none[Int] <* 2.some should be(none[Int])
    none[Int] *> 2.some should be(none[Int])
  }
}
