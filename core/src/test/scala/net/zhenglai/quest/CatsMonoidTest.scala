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
    import cats.instances.string._
    import cats.{ Monoid, Semigroup }
    assert(Semigroup[String].combine("a", "b") == "ab")

    import cats.syntax.semigroup._
    assert(("a" |+| "b" |+| Monoid[String].empty) == "ab")
  }

  test("sum(List[Int]) with Monoid[Int]") {
    def add(items: List[Int]): Int = {
      import cats.Monoid
      import cats.instances.int._
      items.fold(Monoid[Int].empty)(Monoid[Int].combine)
    }

    assert(add(Nil) == 0)
    assert(add(List(1)) == 1)
    assert(add(List(1, 2, 3)) == 6)
  }

  test("sum(List[Option[Int]) with Monoid[Option[Int]]") {
    def sum(items: List[Option[Int]]): Option[Int] = {
      import cats.Monoid
      import cats.instances.int._
      import cats.instances.option._
      items.fold(Monoid[Option[Int]].empty)(Monoid[Option[Int]].combine)
    }

    assert(sum(Nil).isEmpty)
    assert(sum(List(Some(1))).contains(1))
    assert(sum(List(Some(1), None, Some(10))).contains(11))
  }

  test("subtype instance used") {
    class Base {
      def foo = "base"
    }

    class A extends Base {
      override def foo = "A"
    }

    def bar(implicit ev: Base) = ev.foo

    implicit val a = new A
    assert(bar == "A")
  }

  test("smart constructors") {
    // cats prefers to invariant type class
    // use smart ctors...
    import cats.syntax.option._
    val a: Some[Int] = Some(1)
    val b: Option[Int] = 1.some
    val c: None.type = None
    val d: Option[Int] = none[Int]
    assert(a == b)
    assert(c == d)
  }

  test("local type class instance wins") {
    import cats.Monoid
    import cats.syntax.semigroup._

    implicit val intMultiMonoid = new Monoid[Int] {
      override def empty = 1
      override def combine(x: Int, y: Int) = x * y
    }
    assert((1 |+| 2) == 2)
  }
}
