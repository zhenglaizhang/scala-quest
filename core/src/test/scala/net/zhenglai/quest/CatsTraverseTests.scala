package net.zhenglai.quest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import cats.Traverse
import cats.instances.future._
import cats.instances.list._
import cats.syntax.applicative._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}

class CatsTraverseTests extends FunSuite with Matchers with ScalaFutures {
  test("future.traverse") {
    val hostnames = List(
      "alpha.example.com",
      "beta.example.com",
      "gamma.demo.com"
    )

    def getUpTime(hostname: String): Future[Int] = Future(hostname.length * 60)

    val times: Future[List[Int]] = Future.traverse(hostnames)(getUpTime)
    val times2: Future[List[Int]] = Traverse[List].traverse(hostnames)(getUpTime)
    times.futureValue should be(times2.futureValue)
    import cats.syntax.traverse._
    hostnames.traverse(getUpTime).futureValue
    val y: Future[List[Int]] = List(Future(1), Future(2), Future(3)).sequence[Future, Int]
    y.futureValue should be(List(1, 2, 3))
  }

  test("") {
    val x: Future[List[Int]] = List.empty[Int].pure[Future]
  }
}
