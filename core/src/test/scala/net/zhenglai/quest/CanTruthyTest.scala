package net.zhenglai.quest

import org.scalatest.{FunSuite, Matchers}

class CanTruthyTest extends FunSuite with Matchers {
  test("intCanTruthy") {
    implicit val intCanTruthy: CanTruthy[Int] = CanTruthy.fromTruthy {
      case 0 => false
      case _ => true
    }

    import CanTruthy.ops._
    10.truthy should be(true)
    0.truthy should be(false)
    0.asBoolean should be(false)
    10.truthy should be(true)
  }
}
