package net.zhenglai.quest

import org.scalatest.{FunSuite, Matchers}

class FunctionalUtilTest extends FunSuite with Matchers {
  test("countPositive") {
    fun.countPositive(List(1, 2)) should be(Right(2))
    fun.countPositive(List(1, -2, 3)) should be(Left("Negative, stopping"))
  }
}
