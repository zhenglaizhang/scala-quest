package net.zhenglai.quest

import org.scalatest.{ FunSuite, Matchers }
import org.scalatest.prop.{ GeneratorDrivenPropertyChecks, PropertyChecks }

class CatsMonadTest extends FunSuite with Matchers with GeneratorDrivenPropertyChecks {

  test("map over functions") {
    import cats.instances.function._
    import cats.syntax.functor._
    val len = (x: String) => x.length
    val double = (_: Int) * 2
    val doubleStr: String => Int = len.map(double)

    assert(doubleStr("1234") == 8)
    forAll {(s: String) =>
      doubleStr(s) should === (s.length * 2)
      doubleStr(s) should === (double(len(s)))
    }
  }
}
