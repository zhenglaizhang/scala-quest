package net.zhenglai.quest

import net.zhenglai.quest.fun.{Branch, Leaf, Tree}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FunSuite, Matchers}

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

    // appending transformation to a chain...
    Option(1).map(_ * 2).map(_ * 100).map(_ / 10)
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

  test("Tree.Functor") {
    import Tree._
    import cats.Functor
    val tree: Tree[Int] = Branch(
      Branch(Leaf(3), Leaf(1)),
      Leaf(12)
    )
    val mapped = Functor[Tree].map(tree)(_ * 12)
    mapped should ===(Branch(
      Branch(Leaf(36), Leaf(12)),
      Leaf(144)
    ))

    import cats.syntax.functor._
    tree.map((_: Int) * 2) should ===(Branch(
      Branch(Leaf(6), Leaf(2)),
      Leaf(24)
    ))
  }

  test("Contravariant Functor") {
    import cats.Show
    import cats.functor.Contravariant
    import cats.instances.string._
    val showStr = Show[String]
    val showSymbol: Show[Symbol] = Contravariant[Show].contramap(showStr)((s: Symbol) => s"'${s.name}'")
    showSymbol.show('Dave) should ===("'Dave'")

    import cats.instances.function._
    import cats.syntax.contravariant._
    val div2: (Int) => Double = (_: Int) / 2.0
    val add1: (Int) => Int = (_: Int) + 1
    div2.contramap(add1)(2) should ===(1.5)
    (add1 andThen div2) (2) should ===(1.5)
    (div2 compose add1)(2) should ===(1.5)
    import cats.syntax.functor._
    add1.map(div2)(2) should ===(1.5)
  }

  test("Invariant Functor") {
    import cats.Semigroup
    import cats.instances.string._
    import cats.syntax.invariant._
    import cats.syntax.semigroup._
    implicit val symbolSemigroup: Semigroup[Symbol] = Semigroup[String].imap(Symbol.apply)(_.name)
    'a |+| 'few |+| 'words should ===('afewwords)
  }
}
