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

  // This code is equivalent to a for comprehension
  def product[M[_] : cats.Monad, A, B](fa: M[A], fb: M[B]): M[(A, B)] = {
    cats.Monad[M].flatMap(fa) { a =>
      cats.Monad[M].map(fb) { b => (a, b) }
    }
  }
}
