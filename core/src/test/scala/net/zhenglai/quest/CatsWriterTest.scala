package net.zhenglai.quest

import scala.concurrent.{Await, Future}

import cats.Id
import cats.data.WriterT
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}

class CatsWriterTest extends FunSuite with Matchers with ScalaFutures {

  test("WriterT monad transformer") {
    import cats.data.Writer
    // type Writer[L, V] = cats.data.WriterT[cats.Id, L, V]
    val w: WriterT[Id, Vector[String], Int] = Writer(Vector(
      "Best of times",
      "Worst of times"
    ), 123)
    val (log, result) = w.run
    result should be(123)
    log should contain theSameElementsAs Vector(
      "Best of times",
      "Worst of times"
    )

    w.value should be(123)
    (w.written: Vector[String]) should contain theSameElementsAs Vector(
      "Best of times",
      "Worst of times"
    )
  }

  test("Writer syntax") {
    import cats.instances.vector._
    import cats.syntax.applicative._
    import cats.syntax.writer._
    val w = 123.pure[Logged]
    w.value should be(123)
    w.written should be(Vector.empty)
    val v = Vector("msg1", "msg2").tell
    v.value should be(())
    v.written should be(Vector("msg1", "msg2"))
  }

  test("map/flatMap over writer") {
    import cats.instances.vector._
    import cats.syntax.applicative._
    import cats.syntax.writer._
    val w1 = for {
      a <- 123.pure[Logged]
      _ <- Vector("a", "b").tell
      b <- 32.writer(Vector("x", "y"))
    } yield a + b
    val (r1, l1) = w1.run
    r1 should be(Vector("a", "b", "x", "y"))
    l1 should be(155)
    w1.mapWritten(_.map(_.toUpperCase)).run._1 should be(Vector("A", "B", "X", "Y"))

    // mapBoth ...
    val w2 = w1.bimap(
      log => log.map(_.toUpperCase),
      res => res * 100
    )

    w2.run should be(Vector("A", "B", "X", "Y") -> 15500)

    w2.reset.run should be(Vector.empty -> 15500)
  }

  test("Writer for multi-threading logging") {
    def factorial(n: Int): Int = {
      val res = slowly(if (n == 0) 1 else n * factorial(n - 1))
      println(s"fact: $n\t=>\t $res")
      res
    }

    // sequenced logging
    factorial(5)

    println("-" * 20)
    // interleaved logging
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    val _ = Await.result(Future.sequence(List(
      Future(factorial(3)),
      Future(factorial(3))
    )), 5 seconds)

    // TODO: fix with WriterT
  }
}
