package net.zhenglai.quest

case class Person(name: String, email: String)

object Person {

  import cats.Eq
  import cats.syntax.eq._

  implicit val personEq = new Eq[Person] {
    import cats.instances.string._
    import cats.syntax.eq._

    override def eqv(x: Person, y: Person) =
      x.name === y.name
  }

  def notEq(x: Person, y: Person) = x =!= y
}


