package net.zhenglai.quest.core

import org.scalatest.{ FunSuite, Matchers }

/*
1. Strict evaluation, in which the arguments are evaluated before the function is applied. (java, javascript...)
2. Non-strict evaluation, which will defer the evaluation of the arguments until they are actually required/used in the function body.
(Haskell)

Scala supports both of them.
Scala supports two evaluation strategies out-of-the-box:
  1. call by value
    the expression is evaluated and bound to the corresponding parameter before the function body is evaluated. It is also the default
    evaluation strategy in Scala.
  2. call by name.
    Call by name is a non-strict evaluation strategy which will defer the evaluation of the expression until the program needs it.
    The drawback of this evaluation strategy is that the arguments are evaluated on every use in the function body.
    Multiple occurrences of this argument means multiple evaluations (see the second invocation which prints "call-by-name" twice).
    In this case it is crucial the expression passed as a by-name argument to have no side effects,
    otherwise it may lead to unexpected behavior.

How to support `call by need`?
  Call by need is the memoised version of call by name which makes the function argument to be evaluated only once (on the first use).

  Memoisation is an optimisation technique which consists of caching the results of function calls and return the cached result when the function is called with the same input again.

 Scala has lazy (by need) values but no lazy arguments
 */
class EvaluationStrategyTest extends FunSuite with Matchers {

  test("call by value") {
    def times[T](t: Int)(elem: T) = {
      println("In times body")
      Seq.fill(t)(elem)
    }

    times(0) {
      println("previous body")
    }

    println("=" * 20)

    times(10) {
      println("previous body 2")
    }
  }

  test("call by name") {
    def times[T](t: Int)(elem: => T) = {
      println("In times body")
      Seq.fill(t)(elem)
    }

    times(0) {
      println("previous body")
    }

    println("=" * 20)

    times(10) {
      println("previous body 2")
    }
  }
}
