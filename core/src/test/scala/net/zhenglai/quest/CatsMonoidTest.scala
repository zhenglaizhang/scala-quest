package net.zhenglai.quest

import org.scalatest.FunSuite

class CatsMonoidTest extends FunSuite {
  test("monoid law for integer +") {
    // closed binary operation
    assert((2 + 1).getClass == classOf[Int])

    // identity element for plus
    assert(2 + 0 == 0 + 2)

    // associativity
    assert((2 + 1) + 3 == 2 + (1 + 3))
  }

  test("monoid law for integer *") {}

  test("monoid law for string & sequence concatenation") {}
}
