package me.rotemfo.zio

import zio._

/**
  * project: scala-playground
  * package: me.rotemfo.zio
  * file:    Exchange
  * created: 2019-09-21
  * author:  rotem
  */
case class Exchange[A]() {
  val queueHipM: UIO[Queue[A]] = Queue.bounded[A](10)
  val queueKneeM: UIO[Queue[A]] = Queue.bounded[A](10)
  val jobQueueM: UIO[Queue[Request[A]]] = Queue.bounded[Request[A]](10)

  def run: ZIO[Any, Nothing, (Queue[Request[A]], TopicQueue[A], TopicQueue[A], Fiber.Runtime[Nothing, Nothing])] = for {
    jobQueue <- jobQueueM
    queueHip <- queueHipM
    queueKnee <- queueKneeM
    hipTopicQueue = TopicQueue(queueHip, Map.empty)
    kneeTopicQueue = TopicQueue(queueKnee, Map.empty)
    loop = for {
      job <- jobQueue.take
      _ <- job.topic match {
        case HipDiagnostic =>
          queueHip.offer(job.XRayImage)
        case KneeDiagnostic =>
          queueKnee.offer(job.XRayImage)
      }
    } yield ()
    fiber <- loop.forever.fork
  } yield (jobQueue, hipTopicQueue, kneeTopicQueue, fiber)
}

object Exchange {
  def createM[A]: UIO[Exchange[A]] = ZIO.succeed(Exchange[A]())
}