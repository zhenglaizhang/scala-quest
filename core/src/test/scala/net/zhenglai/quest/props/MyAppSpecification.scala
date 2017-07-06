package net.zhenglai.quest.props

import org.scalacheck.Properties

object MyAppSpecification extends Properties("MyApp") {
  include(MyProps)
}
