package net.zhenglai.quest
/*
Scala's types are erased at compiler time (type erasure)

TypeTags can be thought of as objects which carry along all type information available at compile time, to runtime.
For example, TypeTag[T] encapsulates the runtime type representation of some compile-time type T.
 */
class TypeWrapper {

  def m[A](xs: List[A]) = xs match {
    case _: List[Int] => "list of ints"
    case _: List[String] => "list of strings"
  }
}
