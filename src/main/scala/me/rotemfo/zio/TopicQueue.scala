package me.rotemfo.zio

import zio._

import scala.util.Random

/**
  * project: scala-playground
  * package: me.rotemfo.zio
  * file:    TopicQueue
  * created: 2019-09-21
  * author:  rotem
  */
case class TopicQueue[A](queue: Queue[A], subscribers: Map[Int, List[Queue[A]]]) {
  def subscribeM(sub: Queue[A], consumerGroup: Int): UIO[TopicQueue[A]] = {
    val updatedMap = subscribers.get(consumerGroup) match {
      case Some(value) =>
        subscribers + (consumerGroup -> (value :+ sub))
      case None =>
        subscribers + (consumerGroup -> List(sub))
    }

    UIO.succeed(copy(subscribers = updatedMap))
  }

  def run: URIO[Any, Fiber.Runtime[Nothing, Nothing]] = {
    def randomElement(list: List[Queue[A]]) = if (list.nonEmpty) {
      Some(list(Random.nextInt(list.length)))
    } else {
      None
    }

    val loop = for {
      elem <- queue.take
      mapped = subscribers.values.flatMap(randomElement(_).map(_.offer(elem)))
      _ <- ZIO.collectAll(mapped)
    } yield ()
    loop.forever.fork
  }
}

object TopicQueue {
  def createM[A](queue: Queue[A]): UIO[TopicQueue[A]] = UIO.succeed(TopicQueue(queue, Map.empty))
}