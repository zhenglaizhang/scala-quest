package net.zhenglai.quest

// simple JSON AST
sealed trait Json

object Json {
  // type class interfaces
  //  interface objects
  def toJson[A: JsonWriter](value: A): Json = implicitly[JsonWriter[A]].write(value)
}

// `syntax` for the type class
// interface syntax
object JsonSyntax {

  // type class interfaces methods
  //  extension methods (type enrichment, pimping)
  //  extend existing types with interface methods
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
  }

}

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

// 'serialize to JSON` behavior is encoded in this trait
// type class itself, a generic trait
trait JsonWriter[A] {
  def write(value: A): Json
}

object JsonWriterInstances {
  // instances of a type class provide implementations for the types we care about
  // type class instances for each type we care about
  implicit val stringJsonWriter = new JsonWriter[String] {
    override def write(value: String): Json = JsString(value)
  }

  implicit val doubleWriter = new JsonWriter[Double] {
    override def write(value: Double): Json = JsNumber(value)
  }

  implicit val personJsonWriter = new JsonWriter[Person] {
    override def write(value: Person): Json = JsObject(
      Map(
        "name" -> JsString(value.name),
        "email" -> JsString(value.email)
      )
    )
  }

  // etc ...
}

