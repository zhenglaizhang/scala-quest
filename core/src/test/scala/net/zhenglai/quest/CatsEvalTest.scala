package net.zhenglai.quest

import org.scalatest.{FunSuite, Matchers}

class CatsEvalTest extends FunSuite with Matchers {

  test("cats.Eval") {
    import cats.Eval
    val now: Eval[Int] = Eval.now(1 + 2)
    val later: Eval[Int] = Eval.later(3 + 4)
    val always: Eval[Int] = Eval.always(5 + 6)

    now.value should be(3)
    later.value should be(7)
    always.value should be(11)
  }

  test("Eval map/flatMap computation sequencing") {
    import cats.Eval
    Eval.always {
      println("Step 1")
      "Hello"
    }.map { str =>
      println("Step 2")
      s"$str World"
    }.value should be("Hello World")

    println("-" * 40)

    val res = for {
      a <- Eval.now { println("Calculating A"); 40 }
      b <- Eval.always { println("Calculating B"); 2 }
    } yield {
      println("Adding A and B")
      a + b
    }

    println("-" * 40)
    res.value should be(42)
    res.value should be(42)
    res.value should be(42)

    println("-" * 40)
    val mem = res.memoize
    println("-" * 40)
    mem.value should be(42)
    mem.value should be(42)
    mem.value should be(42)
    mem.value should be(42)
  }
}
