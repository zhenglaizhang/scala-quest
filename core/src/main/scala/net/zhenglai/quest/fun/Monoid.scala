package net.zhenglai.quest.fun

trait Monoid[A] extends Semigroup[A] {
  // identity element
  def empty: A
}

object Monoid {
  def associateLaw[A: Monoid[A]](x: A, y: A, z: A): Boolean = {
    val m = implicitly[Monoid[A]]
    m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
  }

  def identityLaw[A: Monoid](x: A): Boolean = {
    val m = implicitly[Monoid[A]]
    (m.combine(x, m.empty) == x) &&
      (m.combine(m.empty, x) == x)
  }

  def apply[A: Monoid]: Monoid[A] = implicitly[Monoid[A]]

}

object MonoidInstances {
  implicit val booleanAndMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    override def empty = true
    override def combine(x: Boolean, y: Boolean) = x && y
  }

  implicit val booleanOrMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    override def empty = false
    override def combine(x: Boolean, y: Boolean) = x || y
  }

  implicit def setUnionMonoid[A](): Monoid[Set[A]] = new Monoid[Set[A]] {
    override def empty = Set.empty[A]
    override def combine(x: Set[A], y: Set[A]) = x union y
  }

  implicit def setIntersectionMonoid[A](): Monoid[Set[A]] = new Monoid[Set[A]] {
    override def empty = Set.empty[A]
    override def combine(x: Set[A], y: Set[A]) = x intersect y
  }
}
