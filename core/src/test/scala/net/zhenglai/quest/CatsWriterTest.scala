package net.zhenglai.quest

import cats.Id
import cats.data.WriterT
import org.scalatest.{FunSuite, Matchers}

class CatsWriterTest extends FunSuite with Matchers {

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
    w.written.asInstanceOf[Vector[String]] should contain theSameElementsAs Vector(
      "Best of times",
      "Worst of times"
    )
  }
}
