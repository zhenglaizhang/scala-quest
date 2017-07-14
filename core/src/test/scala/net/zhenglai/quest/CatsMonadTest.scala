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
}
