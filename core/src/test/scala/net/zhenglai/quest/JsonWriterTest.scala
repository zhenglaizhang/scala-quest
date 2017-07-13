package net.zhenglai.quest

import org.scalatest.FunSuite

class JsonWriterTest extends FunSuite {

  test("testWrite with interface object") {
    import JsonWriterInstances._
    assert(Json.toJson(Person("Zhenglai", "zhenglaizhang@hotmail.com"))
      == JsObject(Map(
      "name" -> JsString("Zhenglai"),
      "email" -> JsString("zhenglaizhang@hotmail.com")
    )))
  }

  test("testWrite with interface method") {
    import JsonWriterInstances._
    import JsonSyntax._
    assert(
      Person("Zhenglai", "zhenglaizhang@hotmail.com").toJson == JsObject(Map(
        "name" -> JsString("Zhenglai"),
        "email" -> JsString("zhenglaizhang@hotmail.com")
      )))
  }

}
