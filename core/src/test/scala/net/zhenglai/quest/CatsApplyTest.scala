package net.zhenglai.quest

import cats.Apply
import cats.instances.option._
import cats.syntax.option._
import org.scalatest.{FunSuite, Matchers}

/*
`ap`: Given a value and a function in the Apply context, applies the function to the value.

You can think of <*> as a sort of a beefed-up fmap. Whereas fmap takes a function and a functor and applies the function inside the
functor value
<*> takes a functor that has a function in it and another functor and extracts that function from the first functor and then maps it over
 the second one.
 */
class CatsApplyTest extends FunSuite with Matchers {
  test("The <*> function is called ap in Cats’ Apply") {
    // If either side fails, we get None.
    Apply[Option].ap({ (_: Int) + 3 }.some)(10.some) should be(13.some)
    Apply[Option].ap(none[Int => Int])(10.some) should be(none[Int])
    Apply[Option].ap({ (_: Int) + 3 }.some)(none[Int]) should be(none[Int])
  }


}
