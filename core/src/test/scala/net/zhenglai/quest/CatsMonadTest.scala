package net.zhenglai.quest

import scala.concurrent.Future
import scala.util.Try

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
}
