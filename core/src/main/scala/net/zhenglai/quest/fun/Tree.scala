package net.zhenglai.quest.fun

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  implicit val treeFunctor: cats.Functor[Tree] = new cats.Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: (A) => B): Tree[B] = {
      def loop(tree: Tree[A]): Tree[B] = tree match {
        case Leaf(value: A) => Leaf(f(value))
        case Branch(left, right) => Branch(loop(left), loop(right))
      }

      loop(fa)
    }
  }
}

