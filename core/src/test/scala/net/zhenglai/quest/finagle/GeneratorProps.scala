package net.zhenglai.quest.finagle

import org.scalacheck.{ Arbitrary, Gen, Properties }

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
  case class Node(left: Tree, right: Tree, v: Int) extends Tree
  case object Leaf extends Tree
  import org.scalacheck.Gen
  import org.scalacheck.Arbitrary.arbitrary
  val genLeaf = Gen.const(Leaf)
  def genTree: Gen[Tree] = Gen.oneOf(genLeaf, genNode)
  val genNode = for {
    v <- arbitrary[Int]
    left <- genTree
    right <- genTree
  } yield Node(left, right, v)

  println(genTree.sample)

  def matrix[T](g: Gen[T]): Gen[Seq[Seq[T]]] = Gen.sized { size => // generation size
    println(s"wow size=$size")
    val side = scala.math.sqrt(size).asInstanceOf[Int]
    Gen.listOfN(side, Gen.listOfN(side, g))
  }

  println(matrix(arbitrary[Int]).sample)


  // conditional generator
  val smallEvenGenerator = Gen.choose(0, 200) suchThat (_ % 2 == 0)
  // be careful
  // Conditional generators work just like conditional properties, in the sense that if the condition is too hard,
  // ScalaCheck might not be able to generate enough values, and it might report a property test as undecided.


  // generating containers
  val genIntList = Gen.containerOf[List, Int](Gen.oneOf(1, 3, 5))
  val genStringStream = Gen.containerOf[Stream, String](Gen.alphaStr)
  val genBooleanList = Gen.nonEmptyListOf[Boolean](true)

  // a special generator, org.scalacheck.Arbitrary.arbitrary, which generates arbitrary values of any supported type
  val evenInteger = Arbitrary.arbitrary[Int] suchThat (_ % 2 == 0)
  var squares = for {
    xs <- arbitrary[List[Int]]
  } yield xs.map(x => x * x)

  // The arbitrary generator is the generator used by ScalaCheck when it generates values for property parameters.
  println(squares.sample)

  // You can use arbitrary for any type that has an implicit Arbitrary instance.
  // implicit lazy val arbBool: Arbitrary[Boolean] = Arbitrary(oneOf(true, false))
  // -> define an implicit def or val of type Arbitrary[T]
  //  Use the factory method Arbitrary(...) to create the Arbitrary instance.
  //  This method takes one parameter of type Gen[T] and returns an instance of Arbitrary[T].


}
