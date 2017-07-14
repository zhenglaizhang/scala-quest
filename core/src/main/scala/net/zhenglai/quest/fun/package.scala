package net.zhenglai.quest

import cats.syntax.either._

package object fun {

  def countPositive(xs: Seq[Int]) =
    xs.foldLeft(0.asRight[String]) { (accumulator, num) =>
      if (num > 0)
        accumulator.map(_ + 1)
      else
        "Negative, stopping".asLeft[Int]
    }

}
