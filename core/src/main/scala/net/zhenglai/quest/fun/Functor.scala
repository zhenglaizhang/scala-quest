package net.zhenglai.quest.fun

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

// Either[A, B], which we implied “is-a” functor yesterday.
// This is not completely accurate because, even though it might not be useful,
// we could have defined another left-biased functor.

// math.abs -> value constructor
// math.abs(12) -> value, produced using a value parameter
// List -> type constructor
// List[A] -> type, produced using a type parameter

// declare type constructor with underscore
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object FunctorInstances {
  // inject dependencies into our instances
  implicit def futureFunctor(implicit ec: ExecutionContext) = new Functor[Future] {
    override def map[A, B](fa: Future[A])(f: (A) => B) = fa.map(f)
  }
}
