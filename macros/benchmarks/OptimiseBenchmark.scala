package benchmarks

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

import foo.Optimise._

class OptimiseBenchmark {

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime))
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  def withoutOptimise(bh: Blackhole) = {
    for (i <- 1 to 10000000) {
      bh.consume(true)
    }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime))
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  def withOptimise(bh: Blackhole) = {
    optimise { 
      for (i <- 1 to 10000000) {
        bh.consume(true)
      }
    }
  }

}
