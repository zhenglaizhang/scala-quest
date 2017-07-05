package net.zhenglai.quest.core

import scala.async.Async._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ FunSuite, Matchers }

class AsyncTest extends FunSuite with Matchers with ScalaFutures {

  test("async works as expected") {
    // async marks a block of asynchronous code. Such a block usually contains one or more await calls,
    // which marks a point at which the computation will be suspended until the awaited Future is complete.
    val future: Future[Int] = async {
      val f1 = async {true}
      val f2 = async {3}
      if (await(f1)) await(f2) else 0
    }

    future.futureValue shouldBe 3
  }

  test("async transformation") {
    def slowCalcFuture = Future {12}

    def combined: Future[Int] = async {
      val future1 = slowCalcFuture
      val future2 = slowCalcFuture
      await(future1) + await(future2)
    }

    // combined is equal to future high order function as below
    val f1 = slowCalcFuture
    val f2 = slowCalcFuture
    def combined2 = for {
      r1 <- f1
      r2 <- f2
    } yield r1 + r2

    combined.futureValue shouldBe combined2.futureValue

    /*
The async approach has two advantages over the use of map and flatMap.

1. The code more directly reflects the programmers intent, and does not require us to name the results r1 and r2. This advantage is even more pronounced when we mix control structures in async blocks.
2. async blocks are compiled to a single anonymous class, as opposed to a separate anonymous class for each closure required at each generator (<-) in the for-comprehension. This reduces the size of generated code, and can avoid boxing of intermediate results.
     */
  }
}
