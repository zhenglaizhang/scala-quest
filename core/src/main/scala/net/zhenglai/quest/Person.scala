package net.zhenglai.quest

import cats.Eq
import cats.instances.string._

final case class Person(name: String, email: String)

object Person {
  implicit val personEq = Eq.by[Person, String](_.name)
}



