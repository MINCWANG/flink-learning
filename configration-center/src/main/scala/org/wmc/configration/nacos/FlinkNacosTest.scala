package org.wmc.configration.nacos

import java.util.Properties
import java.util.concurrent.Executor

import com.alibaba.nacos.api.NacosFactory
import com.alibaba.nacos.api.config.listener.Listener
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala._

/**
 * @author: WangMC
 * @date: 2020/9/28 17:03
 * @description: flink nacos 整合测试
 */
object FlinkNacosTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    val serverAddr = "localhost"
    val dataId = "test"
    val group = "DEFAULT_GROUP"
    val properties = new Properties
    properties.put("serverAddr", serverAddr)
    val configService = NacosFactory.createConfigService(properties)
    val content = configService.getConfig(dataId, group, 5000)
    System.out.println("init =  " + content)


    env
      .addSource(new NacosConfigSourceFunc)
      .print()


    env.execute("flink nacos")

  }


}
class NacosConfigSourceFunc extends RichSourceFunction[String] {

  import com.alibaba.nacos.api.config.ConfigService

  var configService: ConfigService = _
  var config: String = _
  val dataId = "test"
  val group = "DEFAULT_GROUP"

  override def open(parameters: Configuration): Unit = {
    import com.alibaba.nacos.api.NacosFactory
    val serverAddr = "localhost"
    val properties = new Properties
    properties.put("serverAddr", serverAddr)
    configService = NacosFactory.createConfigService(properties)
    config = configService.getConfig(dataId, group, 5000)
    configService.addListener(dataId,group,new Listener {
      override def getExecutor: Executor = null

      override def receiveConfigInfo(configInfo: String): Unit = {
        config = configInfo
        println("open Listener receive : " + configInfo)
      }
    })
  }

  override def close(): Unit = super.close()

  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while (true) {
      Thread.sleep(3000)
     println("run config = " + config)
      ctx.collect(String.valueOf(System.currentTimeMillis))
    }

  }

  override def cancel(): Unit = ???
}