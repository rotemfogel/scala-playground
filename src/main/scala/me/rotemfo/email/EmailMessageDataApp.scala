package me.rotemfo.email

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.json4s.native.JsonMethods
import org.slf4j.{Logger, LoggerFactory}

import java.time.Duration
import java.util.Properties

/**
  * project: scala-playground
  * package: me.rotemfo.email
  * file:    EmailMessageDataApp
  * created: 2019-05-19
  * author:  rotem
  */
object EmailMessageDataApp {
  private final val logger: Logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    val consumer = {
      val groupId = "emails-consumer"
      val topic = "emails"
      logger.info(s"starting email service, group id: $groupId topic: $topic")

      val props = new Properties()
      props.put("bootstrap.servers", "localhost:9092")
      props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)

      props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
      props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
      props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false.toString)

      val consumer = new KafkaConsumer[String, String](props)
      consumer.subscribe(java.util.Collections.singletonList(topic))
      consumer
    }

    import scala.collection.JavaConverters._

    while (true) {
      val emails = consumer.poll(Duration.ofMillis(1000))
      for (email <- emails.asScala) {
        try {
          val value = email.value()
          // println(value)
          val parsed = JsonMethods.parse(value)
          logger.info("{}", parsed \ "richMessage")
        } catch {
          case e: Throwable => e.printStackTrace()
        }
      }
    }
  }

}