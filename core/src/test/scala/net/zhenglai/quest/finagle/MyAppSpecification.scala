package net.zhenglai.quest.finagle

import org.scalacheck.Properties

object MyAppSpecification extends Properties("MyApp") {
  include(MyProps)
}
