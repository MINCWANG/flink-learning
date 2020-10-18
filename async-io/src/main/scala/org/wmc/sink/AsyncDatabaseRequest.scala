package org.wmc.sink

import org.apache.flink.streaming.api.scala.async.{AsyncFunction, ResultFuture}

/**
 * @author: WangMC
 * @date: 2020/10/18 16:23
 * @description:
 */
class AsyncDatabaseRequest extends AsyncFunction[String, (String, String)]{
  override def asyncInvoke(input: String, resultFuture: ResultFuture[(String, String)]): Unit = ???
}
