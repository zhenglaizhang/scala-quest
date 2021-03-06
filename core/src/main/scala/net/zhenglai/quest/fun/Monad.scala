package net.zhenglai.quest.fun

/**
  * Monad Laws
  *  1. left identity:  pure(a).flatMap(f) == f(a)
  *  2. right identity: m.flatMap(pure) == m
  *  3. associativity:  m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
  */
trait Monad[F[_]] {
  def pure[A](value: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  // an alias terminology for flatMap
  def bind[A, B](value: F[A])(func: A => F[B]): F[B] = flatMap(value)(func)

  // every monad is also a functor
  // define map in terms of `pure` & `flatMap`
  def map[A, B](value: F[A])(func: A => B): F[B] = flatMap(value)(func.andThen(pure))
}

object MonadInstances {
  type Id[A] = A

  implicit val idMonadInstance = new Monad[Id] {
    override def pure[A](value: A): Id[A] = value

    override def flatMap[A, B](value: Id[A])(func: (A) => Id[B]): Id[B] = func(value)
  }
}
