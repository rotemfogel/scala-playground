package me.rotemfo.kafka

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.time.Duration
import java.util
import java.util.UUID

/**
  * project: scala-playground
  * package: me.rotemfo.kafka
  * file:    ConsumerMain
  * created: 2019-08-11
  * author:  rotem
  */
object ConsumerMain {
  private final val stringDeserializerClass: String = "org.apache.kafka.common.serialization.StringDeserializer"

  def main(args: Array[String]): Unit = {
    val configs: java.util.Map[String, java.lang.Object] = new util.HashMap[String, java.lang.Object]() {
      {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, stringDeserializerClass)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, stringDeserializerClass)
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID.toString)
        put(ConsumerConfig.CLIENT_ID_CONFIG, getClass.getName)
      }
    }
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](configs)
    consumer.subscribe(util.Collections.singletonList("profiles"))
    while (true) {
      consumer.poll(Duration.ofMillis(1000)).iterator().forEachRemaining(c => {
        val value = c.value()
        println(value)
        // val geo = GeoProfile.fromString(value)
      })
    }
  }
}
