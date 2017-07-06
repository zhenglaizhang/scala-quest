package net.zhenglai.quest.props

import org.scalacheck.{ Gen, Properties }

/*
  Gen[T] may be thought of as a function of type Gen.Params => Option[T]
  think of generators simply as functions,
  and the combinators in the Gen object can be used to create or modify the behaviour of such generator functions.
 */
// TODO: https://github.com/rickynils/scalacheck/blob/master/doc/UserGuide.md 
object GeneratorProps extends Properties("GeneratorProps") {
  val tupleGen: Gen[(Int, Int)] = for {
    a <- Gen.choose(10, 20)
    b <- Gen.choose(2 * a, 500)
  } yield (a, b)

  val vowel = Gen.oneOf('A', 'E', 'I', 'O', 'U', 'Y')

  // Notice that plain values are implicitly converted to generators (which always generates that value) if needed.

  // The distribution is uniform,
  // but if you want to control it you can use the frequency combinator:
  // the vowel generator will generate E:s more often than Y:s.
  // Roughly, 4/14 of the values generated will be E:s, and 1/14 of them will be Y:s
  val freqGen = Gen.frequency(
    (3, 'A'),
    (4, 'E'),
    (2, 'I'),
    (3, 'O'),
    (1, 'U'),
    (1, 'Y')
  )

  sealed abstract class Tree
  case class Node(left: Tree, right: Tree) extends Tree
  case object Leaf extends Tree
}
