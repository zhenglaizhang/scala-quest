package net.zhenglai.quest

import cats.Foldable
import org.scalatest.{FunSuite, Matchers}

class CatsFoldableTest extends FunSuite with Matchers {

  test("foldLeft vs. foldRight") {
    List(1, 2).foldLeft(0)(_ + _) should be(List(1, 2).foldRight(0)(_ + _))

    // non-commutative operator's order matters
    List(1, 2, 3).foldLeft(0)(_ - _) should be(-6)
    List(1, 2, 3).foldRight(0)(_ - _) should be(2)

    List(1, 2, 3).foldLeft(List.empty[Int])((a, i) => i :: a) should be(List(3, 2, 1))
    List(1, 2, 3).foldRight(List.empty[Int])((i, a) => i :: a) should be(List(1, 2, 3))
    List(1, 2, 3).fold(0)(_ + _) should be(List(1, 2, 3).foldLeft(0)(_ + _))
  }

  test("scalfolding other methods") {
    import cats.instances.int._
    fun.sumWithMonoid(List(1, 2, 3)) should be(6)

    fun.sumWithNumeric(List(1, 2, 3)) should be(6)
  }

  test("Foldable") {
    import cats.instances.int._
    import cats.instances.list._
    import cats.instances.option._
    import cats.instances.string._
    Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) should be(6)
    Foldable[Option].foldLeft(Some(1), 12)(_ + _) should be(13)
    Foldable[Option].foldLeft(Option.empty[Int], 12)(_ + _) should be(12)
    Foldable[Option].nonEmpty(Option(42)) should be(true)
    // option - a sequence of 0 or 1 element
    Option.empty[Int].foldLeft(2)(_ + _) should be(2)

    Foldable[List].fold(List(1, 2, 3)) should be(6)
    // convert integer as string & concatenate them
    Foldable[List].foldMap(List(1, 2, 3))(_.toString) should be("123")

    // convert string as ints & sum them
    Foldable[List].foldMap(List("1", "2", "3"))(_.toInt) should be(6)

    import cats.instances.map._
    type StringMap[A] = Map[String, A]
    val stringMap = Map("a" -> "b", "c" -> "d")
    Foldable[StringMap].foldLeft(stringMap, "nil")(_ + "," + _) should be("nil,b,d")
  }

  test("stack safe foldRight") {
    def bigData = (1 to 100000).toStream

    // Stream.foldRight is not stack safe...
    a[StackOverflowError] should be thrownBy bigData.foldRight(0)(_ + _)

    // Using Foldable forces us to use stack safe operations, which fixes the overflow exception
    import cats.Eval
    import cats.instances.stream._
    val eval = Foldable[Stream]
      .foldRight(bigData, Eval.now(0)) { (num, eval) =>
        eval.map(_ + num)
      }
    eval.value should be(705082704)
  }

  test("compose Foldables") {
    import cats.instances.int._
    import cats.instances.list._
    import cats.instances.vector._
    val ints = List(Vector(Vector(1, 2, 3)), Vector(Vector(4, 5, 6)))
    (Foldable[List] compose Foldable[Vector] compose Foldable[Vector]).combineAll(ints) should be(21)
  }
}
