package org.wmc.vertx


import java.util.concurrent.TimeUnit
import org.apache.flink.streaming.api.scala._
import org.wmc.sink.AsyncVertx
import org.wmc.source.InputSource

/**
 * @author: WangMC
 * @date: 2020/10/18 10:00
 * @description:
 */
object VertxTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setMaxParallelism(1)

    val input = env.addSource(new InputSource)

    AsyncDataStream
        .orderedWait(input,new AsyncVertx,10000,TimeUnit.MILLISECONDS,100).setParallelism(1)


    env.execute("Async I/O job")
  }

}
