package org.wmc.sink


import io.vertx.pgclient.PgPool
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.async.{ResultFuture, RichAsyncFunction}

/**
 * @author: WangMC
 * @date: 2020/10/18 10:03
 * @description:
 */
class AsyncVertx extends RichAsyncFunction[String, Int] {


  var client: PgPool = _

  override def asyncInvoke(input: String, resultFuture: ResultFuture[Int]): Unit = {
    VertxTools.execSql("insert into async_writer VALUES($1)", input, client)
    resultFuture.complete(List(1))
  }

  override def open(parameters: Configuration): Unit = {


    //    val vertx = Vertx.vertx(VertxOptions()
    //      .setWorkerPoolSize(100)
    //      .setEventLoopPoolSize(100))

    client = VertxTools.getConnection("***.gpdb.rds.aliyuncs.com",
      3432, "****", "admin", "admin")

  }

  override def close(): Unit = {
    client.close()
  }

  override def timeout(input: String, resultFuture: ResultFuture[Int]): Unit = super.timeout(input, resultFuture)
}
