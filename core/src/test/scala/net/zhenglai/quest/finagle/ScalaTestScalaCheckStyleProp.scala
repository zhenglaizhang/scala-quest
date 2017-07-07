package net.zhenglai.quest.finagle

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck.Prop.BooleanOperators
import org.scalatest.{ FunSuite, Matchers }
import org.scalatest.prop.{ Checkers, GeneratorDrivenPropertyChecks }

/*
ScalaTest supports two styles of property-based testing:
  ScalaTest style and ScalaCheck style.
  Both approaches use ScalaCheck to actually check properties when tests are run.

Property-based tests can, therefore, give you a lot more testing for a lot less code than assertion-based tests.

Checkers facilitates the traditional ScalaCheck style of writing properties,
whereas GeneratorDrivenPropertyChecks facilitates a ScalaTest style of writing properties that takes advantage of ScalaTest's assertions
and matchers.
 */

// scala check style
// For the ScalaCheck style, you mix in trait Checkers and express properties as boolean expressions,
// using ScalaCheck's native operators such as ==>, :|, |:, and so on, as needed.
class ScalaTestScalaCheckStyleProp extends FunSuite with Checkers {

  override implicit val generatorDrivenConfig = PropertyCheckConfig(minSize = 10, maxSize = 20)

  test("concat") {
    check((a: List[Int], b: List[Int]) => a.size + b.size == (a ::: b).size)
  }

  test("meow") {
    check((n: Int) => n + 0 == n, minSuccessful(500), maxDiscardedFactor(0.3))
  }

  test("foo") {
    check { (n: Int) =>
      // implication operator ==>
      n > 1 ==> (n / 2 > 0)
    }
  }
}

// scala test style
// For the ScalaTest style, you mix in trait GeneratorDrivenPropertyChecks and express properties
// with whenever clauses and matcher expressions.
class ScalaTestScalaChecksStyleProp2 extends FunSuite with Matchers with GeneratorDrivenPropertyChecks {

  test("foo") {
    forAll { (n: Int) =>
      whenever(n > 1) {
        n / 2 should be > 0
      }
    }
  }

  def myMagicFunction(i: Int, i1: Int) = i + i1
  test("bar") {
    forAll { (m: Int, n: Int) =>
      val res = myMagicFunction(n, m)
      res should be >= m
      res should be >= n
      res should be < m + n
    }
  }
}
