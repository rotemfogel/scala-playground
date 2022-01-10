package me.rotemfo.zio

import zio._

/**
  * project: scala-playground
  * package: me.rotemfo.zio
  * file:    Main
  * created: 2019-09-21
  * author:  rotem
  */
object Main extends App {

  private val program = for {

    physicianHip <- Consumer.createM[Int]("Hip")
    ctxPhHip <- physicianHip.run
    (phHipQueue, _) = ctxPhHip

    loggerHip <- Consumer.createM[Int]("HIP_LOGGER")
    ctxLoggerHip <- loggerHip.run
    (loggerHipQueue, _) = ctxLoggerHip

    physicianKnee <- Consumer.createM[Int]("Knee1")
    ctxPhKnee <- physicianKnee.run
    (phKneeQueue, _) = ctxPhKnee

    physicianKnee2 <- Consumer.createM[Int]("Knee2")
    ctxPhKnee2 <- physicianKnee2.run
    (phKneeQueue2, _) = ctxPhKnee2

    exchange <- Exchange.createM[Int]
    ctxExchange <- exchange.run
    (inputQueue, outputQueueHip, outputQueueKnee, _) = ctxExchange

    generator = IntRequestGenerator()
    xRayRoom <- Producer.createM[Int](inputQueue, generator)
    _ <- xRayRoom.run

    outputQueueHip <- outputQueueHip.subscribeM(phHipQueue, consumerGroup = 1)
    outputQueueHip <- outputQueueHip.subscribeM(loggerHipQueue, consumerGroup = 2)

    outputQueueKnee <- outputQueueKnee.subscribeM(phKneeQueue, consumerGroup = 1)
    outputQueueKnee <- outputQueueKnee.subscribeM(phKneeQueue2, consumerGroup = 1)

    _ <- outputQueueHip.run
    _ <- outputQueueKnee.run

    // _ <- phHipFiber.join

  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.exitCode
}
