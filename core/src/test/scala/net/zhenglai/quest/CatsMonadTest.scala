package net.zhenglai.quest

import java.lang

import scala.concurrent.Future
import scala.util.{Success, Try}

import cats.data.Ior.Left
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}

class CatsMonadTest extends FunSuite with Matchers with ScalaFutures {

  // monads are truly about sequencing
  test("Option monad -> fail fast error handling") {
    def parseInt(s: String): Option[Int] = Try(s.toInt).toOption

    def divide(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)

    def stringDivide(aStr: String, bStr: String): Option[Int] =
      for {
        aNum <- parseInt(aStr)
        bNum <- parseInt(bStr)
        res <- divide(aNum, bNum)
      } yield res

    stringDivide("1", "3") should be(Some(0))
    stringDivide("str", "1") should be(None)
    stringDivide("1", "0") should be(None)
  }

  test("Cats.Monad") {
    import cats.Monad
    import cats.instances.list._
    import cats.instances.option._
    Monad[Option].pure(12) should be(Some(12))
    Monad[Option].flatMap(Some(1))(x => Some(x + 2)) should be(Some(3))
    Monad[List].flatMap(List(1, 2, 3))(x => List(x, x)) should be(List(1, 1, 2, 2, 3, 3))

    // monad is also functor
    Monad[List].map(List(1, 2))(_ * 2) should be(List(2, 4))

    import cats.instances.vector._
    Monad[Vector].flatMap(Vector(1, 2))(x => Vector(x * 2)) should be(Vector(2, 4))

    import scala.concurrent.ExecutionContext.Implicits.global

    import cats.instances.future._
    val fm = Monad[Future]
    fm.flatMap(fm.pure(1)) { x => fm.pure(x + 2) }.futureValue should be(3)
  }

  test("monad syntax") {
    import cats.Monad
    import cats.instances.list._
    import cats.instances.option._
    import cats.syntax.applicative._
    import cats.syntax.flatMap._
    import cats.syntax.functor._      // flatMap
    // import cats.implicits._
    1.pure[Option] should be(Some(1))
    1.pure[List] should be(List(1))

    def sumSquare[M[_] : Monad](a: M[Int], b: M[Int]): M[Int] =
      a.flatMap(x => b.map(y => x * x + y * y))

    sumSquare(List(1, 2), List(3, 4)) should be(List(10, 17, 13, 20))
    sumSquare(Option(1), Option(2)) should be(Some(5))

    import cats.Id
    sumSquare(2: Id[Int], 3: Id[Int]) should be(13)

    val a: Id[Int] = Monad[Id].pure(1)
    val b = Monad[Id].flatMap(a)(_ + 1)
    val c: Id[List[Int]] = Monad[Id].pure(List(1, 2))
    val d: Id[Int] = for {
      x <- a
      y <- b
    } yield x + y

    d should be(3)

    // in prod, async
    import scala.concurrent.ExecutionContext.Implicits.global
    import cats.instances.future._
    sumSquare(Future(1), Future(2)).futureValue should be(5)
    // in test, sync
    sumSquare(a, b) should be(5)
  }

  test("Either as Monad") {
    // scala 2.12
    Right(12).flatMap(x => Right(x + 2)) should be(Right(14))
    (for {
      a <- Right(12)
      b <- Right(14)
    } yield a + b) should be(Right(26))

    // scala 2.11
    val c = for {
      a <- Right(12).right
      b <- Right(13).right
    } yield a + b
    c should be(Right(25))

    import cats.syntax.either._
    // better type inference
    val foo: Either[String, Int] = 3.asRight[String]
    val bar: Either[Int, String] = "hello".asRight[Int]

    // TODO: fix it
//    Either.catchOnly[NumberFormatException]("foo".toInt) should be(Left(new NumberFormatException("For input string: \"foo\"")))
//    Either.catchNonFatal(sys.error("error")) should be(Left(new RuntimeException("error")))
//
//    Either.fromTry(Try("foo".toInt)) should be(Left())
//    Either.fromOption[String, Int](None, "badness")
    "error".asLeft[Int].orElse(2.asRight[String]) should be(Right(2))
    "error".asLeft[String].getOrElse(0) should be(0)
    (-1.asRight[String].ensure("Must be non-negative")(_ > 0)) should be("Must be non-negative".asLeft[String])
    "error".asLeft[String] recover {
      case str: String => s"Recovered from $str"
    } should be(Right("Recovered from error"))
    "error".asLeft[String].recoverWith{ case str: String => Right(s"Recovered from $str") } should be(Right("Recovered from error"))


    "foo".asLeft[Int].leftMap(_.reverse) should be("oof".asLeft[Int])
    6.asRight[String].bimap(_.reverse, _ * 2) should be(12.asRight[String])
    "bar".asLeft[Int].bimap(_.reverse, _ * 2) should be("rab".asLeft[Int])

    123.asRight[String].swap should be(123.asLeft[Int])

    123.asRight[String].toOption should be(Some(123))
    123.asRight[String].toSeq should be(Seq(123))
    123.asRight[Exception].toTry should be(Success(123))
  }

  test("either fail-fast sequencing") {
    import cats.syntax.either._
    val res = for {
      a <- 1.asRight[String]
      b <- 0.asRight[String]
      c <- if (b == 0) "DIV0".asLeft[Int] else (a / b).asRight[String]
    } yield c * 100
    res should be("DIV0".asLeft[Int])
  }

  test("LoginError handler") {
    import cats.syntax.either._
    import LoginError._
    val res1: LoginResult = User("Zhenglai", "pass0wrd").asRight
    val res2: LoginResult = UserNotFound("Zhenglai").asLeft
    res1.fold(handleError, user => println(s"hello $user"))
    res2.fold(handleError, user => println(s"hello $user"))
  }
}
