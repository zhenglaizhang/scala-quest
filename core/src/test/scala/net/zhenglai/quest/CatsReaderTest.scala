package net.zhenglai.quest

import org.scalatest.{FunSuite, Matchers}

class CatsReaderTest extends FunSuite with Matchers {
  test("Reader monad") {
    import cats.data.Reader
    case class Cat(name: String, favoriteFood: String)
    val catName: Reader[Cat, String] = Reader(cat => cat.name)
    val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello $name")
    val res: String = greetKitty.run(Cat("Heathcliff", "junk food"))
    res should be("Hello Heathcliff")
    // same as: greetKitty(Cat("Name1", "foodi1"))

    val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${ cat.favoriteFood }")
    val greetAndFeed: Reader[Cat, String] = for {
      msg1 <- greetKitty
      msg2 <- feedKitty
    } yield s"$msg1 $msg2"
    (greetAndFeed(Cat("name1", "food1")): String) should be("Hello name1 Have a nice bowl of food1")

    val feedAndGreet: Reader[Cat, String] = for {
      msg2 <- feedKitty
      msg1 <- greetKitty
    } yield s"$msg2 $msg1"
    (feedAndGreet(Cat("name1", "food1")): String) should be("Have a nice bowl of food1 Hello name1")
  }

  test("checkLogin") {
    import AuthUser._
    val db = Db(
      Map(
        1 -> "dade",
        2 -> "kate",
        3 -> "margo"
      ),
      Map(
        "dade" -> "zerocool",
        "kate" -> "acidburn",
        "margo" -> "secret"
      )
    )

    checkLogin(1, "zerocool").run(db).asInstanceOf[Boolean] should be(true)
    checkLogin(1, "ade").run(db).asInstanceOf[Boolean] should be(false)
    checkLogin(100, "dade").run(db).asInstanceOf[Boolean] should be(false)
  }
}
