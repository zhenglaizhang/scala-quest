package net.zhenglai.quest

// A Printable[A] represents a transformation from A to String
trait Printable[A] {
  def format(value: A): String

  // if A is Printable & we can transform B to A
  // then B is also Printable
  def contramap[B](func: B => A): Printable[B] = {
    val self = this
    // single abstract method
    (value: B) => self.format(func(value))
  }
}

object Printable {
  def format[A: Printable](value: A): String =
    implicitly[Printable[A]].format(value)

  def print[A: Printable](value: A): Unit =
    println(implicitly[Printable[A]].format(value))

  def apply[A](implicit ev: Printable[A]): Printable[A] = ev
}

object PrintableSyntax {

  implicit class PrintableOps[A](value: A) {
    def format(implicit printable: Printable[A]) = printable.format(value)

    def print(implicit printable: Printable[A]) = println(printable.format(value))

  }

}

object PrintableInstances {
  implicit val intPrintable = new Printable[Int] {
    override def format(value: Int): String = s"$value"
  }

  implicit val stringPrintable = new Printable[String] {
    override def format(value: String): String = s""""$value""""
  }

  implicit val booleanPrintable = new Printable[Boolean] {
    override def format(value: Boolean) = if (value) "yes" else "no"
  }
}
