package net.zhenglai.quest

import net.zhenglai.quest.fun.Box
import org.scalatest.{FunSuite, Matchers}

class PrintableTest extends FunSuite with Matchers {

  test("testShow with interface object") {
    import PrintableInstances._
    assert(Printable.format(12) == "12")
    Printable.print(12)
    Printable.print("12")
  }

  test("testShow with extension method") {
    import PrintableInstances._
    import PrintableSyntax._
    assert(12.format == "12")
    12.print
    "12".print
  }

  test("contramap") {
    import PrintableInstances.stringPrintable
    import PrintableSyntax._
    implicit val intPrintable: Printable[Int] = Printable[String].contramap((_: Int).toString)
    intPrintable.format(12).print
  }

  test("Box printable") {
    import net.zhenglai.quest.fun.Box
    import PrintableInstances._
    import net.zhenglai.quest.fun.Box._
    import PrintableSyntax._
    Box(12).print
    Box(true).print
    Box("13").print
  }
}
