package net.zhenglai.quest.fun

// e.g. NonEmptyList, Positive integer addition
trait Semigroup[@specialized(Int, Long, Float, Double) A] {
  // closed, associated, binary operation
  def combine(x: A, y: A): A
}
