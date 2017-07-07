package net.zhenglai.quest.finagle

import org.scalacheck.Prop.{ BooleanOperators, all, forAll }
import org.scalacheck.{ Gen, Properties }

// TODO: http://www.scalatest.org/user_guide/writing_scalacheck_style_properties
/* A property is the testable unit in ScalaCheck, and is represented by the org.scalacheck.Prop class. */
/*
`forAll takes a function should return Boolean or another property,
 and can take parameters of any types, as long as there exist implicit Arbitrary instances for the types.


  todo:
 Gen vs Arbitrary
   Scala implicits are convenient, but they can also be troublesome if you're not sure what implicit values or conversions currently are in scope.
   By using a specific type (Arbitrary) for doing implicit lookups, ScalaCheck tries to constrain the negative impacts of using implicits
   As an end-user, you should think of Arbitrary[T] as the default generator for the type T

   they limit the scope through semantics. When a method requires an implicit Arbitrary, it means it want the default generator for a type. You could imagine another type class called EdgeCase, implemented just like Arbitrary, but with the semantic intention of representing just edge case generators for a type. Implicit EdgeCase values would then not compete with implicit Arbitrary values during implicit lookup

   the Arbitrary class sometimes simply delegates to Gen instances as in

   you might need to have multiple instances of Gen, so Arbitrary is used to "flag" the one that you want ScalaCheck to use?

   Arbitrary is used to supply implicit Gens, basically, so this allows to use both forAll variants with explicit Gen and with implicit Arbitrary. I don't think non-implicit Arbitrary is ever useful.

   To minimize explicit passing of Gens, you can add an overload taking implicit Arbitrary and still returning Gen as well.
 */
object MyProps extends Properties("MyProps") {
  // Grouping properties through `Properties` trait

  property("startsWith") = forAll { (a: String, b: String) =>
    (a + b).startsWith(a)
  }

  property("concatenate") = forAll { (a: String, b: String) =>
    (a + b).length > a.length && (a + b).length > b.length
  }

  property("substring") = forAll { (a: String, b: String, c: String) =>
    (a + b + c).substring(a.length, a.length + b.length) == b
  }

  property("concatenateList") = forAll { (l1: List[Int], l2: List[Int]) =>
    l1.size + l2.size == (l1 ::: l2).size
  }

  property("fail") = forAll { (l: List[Int], sz: Int) =>
    l.size == sz
  }

//  val propSqrt = forAll { (n: Int) => scala.math.sqrt(n*n) == n }
//  propSqrt.check

  // give forAll a specific data generator
  // state properties about a specific subset of a type
  val smallInteger = Gen.choose(0, 200)
  property("propSmallInteger") = forAll(smallInteger) { n =>
    n >= 0 && n <= 200
  }

  // Conditional Properties
  property("propMakeList") = forAll { n: Int =>
    // Now ScalaCheck will only care for the cases when n is not negative.
    // a specification takes the form of an implication. You can use the implication operator ==>
    // If the implication operator is given a condition that is hard or impossible to fulfill,
    // ScalaCheck might not find enough passing test cases to state that the property holds.
    // It is possible to tell ScalaCheck to try harder when it generates test cases,
    // but generally you should try to refactor your property specification instead of generating more test cases
    // Using implications, we realise that a property might not just pass or fail,
    // it could also be undecided if the implication condition doesn't get fulfilled.
    (n >= 0 && n < 1000) ==> (List.fill(n)("").length == n)
  }

  property("propTrivival") = forAll { n: Int =>
    n == 0 ==> (n == 0)
  }


  // Combing properties
  /*
      val p1 = forAll(...)
      val p2 = forAll(...)
      val p3 = p1 && p2
      val p4 = p1 || p2
      val p5 = p1 == p2
      val p6 = all(p1, p2) // same as p1 && p2
      val p7 = atLeastOne(p1, p2) // same as p1 || p2
   */


  // labeling properties
  // The labeling operator can also be used to inspect intermediate values used in the properties,
  // which can be very useful when trying to understand why a property fails.
  def sum(m: Int, n: Int) = m + n
  property("complexProp") = forAll {(m: Int, n: Int) =>
    val res = sum(m, n)
    (res >= m)      :| "result > #1" &&
    (res >= n)      :| "result >= #2" &&
    (res < m + n)   :| "result not sum"
    // swap the args: ("result > #1"    |: res >= m) &&
  }

  // The implication operators are used to protect us from zero-divisions.
  // A fifth label is added to the combined property to record the result of the multiplication.
  property("propMulti") = forAll { (n: Int, m: Int) =>
    val res = n * m
    ("evidence = " + res) |: all(
      "div1" |: m != 0 ==> (res / m == n),
      "div2" |: n != 0 ==> (res / n == m),
      "lt1"  |: res > m,
      "lt2"  |: res > n
    )
  }


  def ordered(list: List[Int]): Boolean = list == list.sorted
  import org.scalacheck.Prop.classify
  property("propCollectTestData") = forAll {l: List[Int] =>
    classify(ordered(l), "ordered") {
      classify(l.length > 5, "large", "small") {
        l.reverse.reverse == l
      }
    }
  }
  // watch the statistics, add special generators to generate missing distribution data if missing

  // collect data directly for presentation report
  import org.scalacheck.Prop.collect
  property("propCollect") = forAll(Gen.choose(0, 10)) { n =>
    collect(n) {
      n == n
    }
  }
}
