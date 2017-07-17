package net.zhenglai.quest

import cats.data.OptionT
import cats.instances.list._
import cats.syntax.applicative._
import org.scalatest.{FunSuite, Matchers}

class CatsMonadTransformerTest extends FunSuite with Matchers {
  test("OptionT") {
    val a: ListOption[Int] = 42.pure[ListOption]
    a should be(OptionT(List(Option(42))))
    val b: OptionT[List, Int] = 10.pure[ListOption]
    val res = for {
      x <- a
      y <- b
      z <- OptionT.liftF(List(1, 2, 3))
    } yield x + y + z
    res.value should be(List(Some(53), Some(54), Some(55)))
  }
}
