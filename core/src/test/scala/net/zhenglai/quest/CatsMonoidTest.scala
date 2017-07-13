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

  test("Cats.Monoid") {
    import cats.Monoid
    import cats.instances.string._
    assert(Monoid[String].combine("a", "b") == "ab")
    assert(Monoid.apply[String].combine("a", "b") == "ab")
    assert(Monoid.empty[String] == "")
  }

  test("Cats.Monoid option[string]") {
    import cats.Monoid
    import cats.instances.option._
    import cats.instances.string._
    assert(Monoid[Option[String]].combine(Some("1"), None).contains("1"))
    assert(Monoid[Option[String]].combine(Some("1"), Some("2")).contains("12"))
  }

  test("Cats.Monoid[List]") {
    import cats.Monoid
    import cats.instances.list._
    assert(Monoid[List[String]].combine(List("a", "b"), List("c")) == List("a", "b", "c"))
  }

  test("Cats.Semigroup") {
    import cats.Monoid
    import cats.Semigroup
    import cats.instances.string._
    assert(Semigroup[String].combine("a", "b") == "ab")

    import cats.syntax.semigroup._
    assert(("a" |+| "b" |+| Monoid[String].empty) == "ab")
  }

}
