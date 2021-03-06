package net.zhenglai.quest.catsext

import java.util.Date

import cats.Eq
import cats.instances.long._
import cats.syntax.option._
import cats.syntax.eq._

object EqInstancesExt {
  private[this] def note = {
    import cats.instances.int._
    import cats.instances.option._
    import cats.syntax.eq._

    // Eq type class is invariant
    (Some(1): Option[Int]) === (None: Option[Int])
    // equals to:
    Option(1) == Option.empty[Int]

    1.some === None
    1.some =!= None
  }

  implicit val dateEqual = Eq.instance[Date] { (d1, d2) =>
    d1.getTime === d2.getTime
  }
}

