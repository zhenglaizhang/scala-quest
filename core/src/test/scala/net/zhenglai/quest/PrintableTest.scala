package net.zhenglai.quest

import org.scalatest.FunSuite

/**
  * Created by zhenglai on 7/13/17.
  */
class PrintableTest extends FunSuite {

  test("testShow with interface object") {
    import PrintableInstances._
    assert(Printable.format(12) == "Int: 12")
    Printable.print(12)
    Printable.print("12")
  }

  test("testShow with extension method") {
    import PrintableInstances._
    import PrintableSyntax._
    assert(12.format == "Int: 12")
    12.print
    "12".print
  }
}
