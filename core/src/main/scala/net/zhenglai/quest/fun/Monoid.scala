package net.zhenglai.quest.fun

// Specialization is the feature that allows you to generate separate versions of generic classes for primitive types,
// thus avoiding boxing in most cases.
// Specialization has a high cost,  on the size of classes, so it must be added with careful consideration.
// In general, we say things like “is X a monoid?” to mean “can X form a monoid under some operation?
trait Monoid[@specialized(Int, Long, Float, Double) A] extends Semigroup[A] {
  // identity element
  def empty: A
}

object Monoid {
  def associateLaw[A: Monoid](x: A, y: A, z: A): Boolean = {
    val ev = implicitly[Monoid[A]]
    ev.combine(x, ev.combine(y, z)) == ev.combine(ev.combine(x, y), z)
  }

  def identityLaw[A: Monoid](x: A): Boolean = {
    val ev = implicitly[Monoid[A]]
    (ev.combine(x, ev.empty) == x) &&
      (ev.combine(ev.empty, x) == x)
  }

  // returns the type class instances
  @inline final def apply[A: Monoid]: Monoid[A] = implicitly[Monoid[A]]

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
