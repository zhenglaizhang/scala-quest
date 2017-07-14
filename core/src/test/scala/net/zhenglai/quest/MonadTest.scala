package net.zhenglai.quest

import scala.util.Try

import org.scalatest.{FunSuite, Matchers}

class MonadTest extends FunSuite with Matchers {

  // monads are truly about sequencing
  test("Option monad -> fail fast error handling") {
    def parseInt(s: String): Option[Int] = Try(s.toInt).toOption

    def divide(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)

    def stringDivide(aStr: String, bStr: String): Option[Int] =
      for {
        aNum <- parseInt(aStr)
        bNum <- parseInt(bStr)
        res <- divide(aNum, bNum)
      } yield res

    stringDivide("1", "3") should be(Some(0))
    stringDivide("str", "1") should be(None)
    stringDivide("1", "0") should be(None)
  }
}
