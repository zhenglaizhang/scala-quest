package net.zhenglai.quest

// TODO: try https://github.com/mpilquist/simulacrum
trait CanTruthy[A] {
  /**
    * Return true if `a` is truthy
    *
    * @param a
    * @return
    */
  def truthy(a: A): Boolean
}

object CanTruthy {
  def fromTruthy[A](f: A => Boolean): CanTruthy[A] = new CanTruthy[A] {
    override def truthy(a: A) = f(a)
  }

  def apply[A: CanTruthy](): CanTruthy[A] = implicitly[CanTruthy[A]]

  trait Ops[A] {
    def typeClassInstance: CanTruthy[A]
    def self: A
    def truthy: Boolean = typeClassInstance.truthy(self)
    def asBoolean: Boolean = truthy
  }

  trait toCanTruthyOps {
    implicit def toCanTruthyOps[A: CanTruthy](target: A): Ops[A] = new Ops[A] {
      override val self = target
      override val typeClassInstance = implicitly[CanTruthy[A]]
    }
  }

  trait AllOps[A] extends Ops[A] {
    override def typeClassInstance: CanTruthy[A]
  }

  object ops {
    implicit def toAllCanTruthyOps[A: CanTruthy](target: A): AllOps[A] = new AllOps[A] {
      override val self = target
      override val typeClassInstance = implicitly[CanTruthy[A]]
    }
  }
}
