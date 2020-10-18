package org.wmc.source

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import scala.util.Random

/**
 * @author: WangMC
 * @date: 2020/9/28 10:58
 * @description:
 */
class InputSource extends RichSourceFunction[String]{

  // 定义一个标识位，用来指示是否正常生成数据
  var running: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while (running){
      val i = new Random().nextInt(1000) + ""
      ctx.collect(i)
//      Thread.sleep()
    }

  }

  override def cancel(): Unit = running = false
}
