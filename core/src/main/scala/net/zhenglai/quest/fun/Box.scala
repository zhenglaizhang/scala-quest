package net.zhenglai.quest.fun

import net.zhenglai.quest.Printable

final case class Box[A](value: A)

object Box {
  //implicit def boxPrintable[A]: Printable[Box[A]] = new Printable[Box[A]] {
  //  override def format(value: Box[A]) = s"Box(${value.value})"
  //}
  implicit def boxPrintable[A](implicit ev: Printable[A]) = ev.contramap[Box[A]](_.value)
}
