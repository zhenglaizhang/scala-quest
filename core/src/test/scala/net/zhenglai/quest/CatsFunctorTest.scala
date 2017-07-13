package net.zhenglai.quest

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{ FunSuite, Matchers }

class CatsFunctorTest extends FunSuite with Matchers with GeneratorDrivenPropertyChecks {

  test("Functor syntax -> map over functions") {
    import cats.instances.function._
    import cats.syntax.functor._
    val len = (x: String) => x.length
    val double = (_: Int) * 2
    val doubleStr: String => Int = len.map(double)

    assert(doubleStr("1234") == 8)
    forAll { (s: String) =>
      doubleStr(s) should ===(s.length * 2)
      doubleStr(s) should ===(double(len(s)))
    }
  }

  test("Cats.Functor") {
    import cats.Functor
    import cats.instances.list._
    forAll { (xs: List[Int]) =>
      Functor[List].map(xs)(_ * 2) should ===(xs.map(_ * 2))
    }

    import cats.instances.option._
    forAll { (xs: Option[Int]) =>
      Functor[Option].map(xs)(_ * 4) should ===(xs.map(_ * 4))
    }
  }

  test("Cats.Functor.lift") {
    import cats.Functor
    import cats.instances.option._
    forAll { (xs: Option[Int]) =>
      val lifted: (Option[Int]) => Option[Int] = Functor[Option].lift((_: Int) * 2)
      lifted(xs) should ===(xs.map(_ * 2))
    }
  }

  test("builtin overrides extension methods") {
    implicit class StrOps(v: String) {
      def length = "dummy"

      def length2 = "dummy"
    }

    "abc".length should ===(3)
    "abc".length2 should ===("dummy")
  }
}
