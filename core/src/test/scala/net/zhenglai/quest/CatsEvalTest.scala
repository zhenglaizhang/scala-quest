package net.zhenglai.quest

import cats.Eval
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

  test("Eval map/flatMap is trampolined - stack safety") {
    def factorialBad(n: BigInt): BigInt = if (n == 1) n else n * factorialBad(n - 1)

    a[StackOverflowError] should be thrownBy { factorialBad(500000) }

    def factorialStillBad(n: BigInt): Eval[BigInt] =
      if (n == 1) Eval.now(n) else factorialStillBad(n - 1).map(_ * n)

    a[StackOverflowError] should be thrownBy { factorialStillBad(50000000) }

    def factorial(n: BigInt): Eval[BigInt] = {
      if (n == 1) {
        Eval.now(1)
      } else {
        // trampoline is not free
        Eval.defer(factorial(n - 1).map(_ * n))
      }
    }

    val _ = factorial(300).value
    // stack safe
    // heap OOM might happen -:)
    // val _ = factorial(500000000).value
  }
}
