package net.zhenglai.quest

import net.zhenglai.quest.fun.Box

trait Codec[A] {
  def encode(value: A): String

  def decode(value: String): Option[A]

  // invariant functor
  def imap[B](dec: A => B, enc: B => A): Codec[B] = {
    val self = this
    new Codec[B] {
      override def encode(value: B): String = self.encode(enc(value))

      override def decode(value: String): Option[B] = self.decode(value).map(dec)
    }
  }
}

object Codec {
  def encode[A: Codec](value: A): String = implicitly[Codec[A]].encode(value)

  def decode[A: Codec](value: String): Option[A] = implicitly[Codec[A]].decode(value)
}

object CodecInstances {
  implicit val intCodec: Codec[Int] = new Codec[Int] {
    override def encode(value: Int) = ???

    override def decode(value: String) = ???
  }

  implicit def boxCodec[A]: Codec[Box[A]] = new Codec[Box[A]] {
    override def encode(value: Box[A]) = ???

    override def decode(value: String) = ???
  }
}
