package net.zhenglai.quest

import java.util.Date

import cats.Show
import org.scalatest.FunSuite

class CatsTest extends FunSuite {
  test("show") {
    import cats.Show
    import cats.instances.int._
    import cats.instances.string._
    // TODO: where is apply defined ??
    val showInt: Show[Int] = Show.apply[Int]
    val showString: Show[String] = Show.apply[String]
    assert(showInt.show(12) == "12")
    assert(showString.show("12") == "12")


    // import interface syntax
    import cats.syntax.show._
    assert(123.show == "123")
    assert("abc".show == "abc")

    // another constructor methods
    // def show[A](f: A => String): Show[A] = ???
    implicit val dataShow: Show[Date] =
    Show.show(date => s"${date.getTime}ms since epoch")
    println(new Date().show)
  }

  test("eq") {
    // use `Eq` to define type-safe equality
    import cats.Eq
    import cats.instances.int._
    val eqInt = Eq[Int]
    assert(eqInt.eqv(123, 123))
    assert(!eqInt.eqv(123, 125))
    assert(!(123 == "123"))
    // assert(eqInt(123, "123"))

    // // with interface syntax
    // import cats.syntax.eq._
    // 123 === 123
    // 123 =!= 234
  }

}
