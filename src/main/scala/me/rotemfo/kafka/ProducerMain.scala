package me.rotemfo.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import org.slf4j.{Logger, LoggerFactory}

/**
  * project: scala-playground
  * package: me.rotemfo.kafka
  * file:    KafkaMain
  * created: 2018-09-16
  * author:  rotem
  */
object ProducerMain extends App {

  import scala.collection.JavaConverters._

  private val logger: Logger = LoggerFactory.getLogger(getClass)
  private val topic = "test"
  private val key = "key"
  private val value = "value"

  val kafkaProducer = new KafkaProducer[String, String](Map[String, Object](
    "bootstrap.servers" -> "localhost:9092",
    "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "max.block.ms" -> "5000"
  ).asJava)

  kafkaProducer.send(new ProducerRecord[String, String](topic, key, value), (_: RecordMetadata, exception: Exception) => {
    if (exception != null) logger.error("error", exception)
  })

  kafkaProducer.close()
}
