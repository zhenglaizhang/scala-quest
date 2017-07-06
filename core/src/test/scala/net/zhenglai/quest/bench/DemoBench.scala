package net.zhenglai.quest.bench

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
@Warmup(iterations = 2, batchSize = 1500)
@Measurement(iterations = 2, batchSize = 1500)
@Fork(1)
@BenchmarkMode(Array(Mode.SingleShotTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
class DemoBench {

  @Benchmark
  def quick(): Unit = {}

  @Benchmark
  def slow(): Unit = Thread.sleep(1000)
}
