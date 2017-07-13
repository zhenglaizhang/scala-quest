package net.zhenglai.quest

trait Printable[A] {
  def show(value: A): String
}

object Printable {
  def format[A: Printable](value: A): String =
    implicitly[Printable[A]].show(value)

  def print[A: Printable](value: A): Unit =
    println(implicitly[Printable[A]].show(value))
}

object PrintableSyntax {

  implicit class PrintableOps[A](value: A) {
    def format(implicit printable: Printable[A]) = printable.show(value)

    def print(implicit printable: Printable[A]) = println(printable.show(value))
  }

}

object PrintableInstances {
  implicit val intPrintable = new Printable[Int] {
    override def show(value: Int): String = s"Int: $value"
  }

  implicit val stringPrintable = new Printable[String] {
    override def show(value: String): String = s"String: $value"
  }
}
