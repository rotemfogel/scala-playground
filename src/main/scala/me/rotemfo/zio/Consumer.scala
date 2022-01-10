package me.rotemfo.zio

import zio._
import zio.console._
import zio.duration.Duration

import java.io.IOException
import java.util.concurrent.TimeUnit
import scala.util.Random

/**
  * project: scala-playground
  * package: me.rotemfo.zio
  * file:    Consumer
  * created: 2019-09-21
  * author:  rotem
  */
case class Consumer[A](title: String) {
  val queueM: UIO[Queue[A]] = Queue.bounded[A](10)

  def run: ZIO[Console with clock.Clock, Nothing, (Queue[A], Fiber.Runtime[IOException, Nothing])] = for {
    queue <- queueM
    loop = for {
      img <- queue.take
      _ <- putStrLn(s"[$title] worker: Starting analyzing task $img")
      _ <- ZIO.sleep(Duration(Random.nextInt(4), TimeUnit.SECONDS))
      _ <- putStrLn(s"[$title] worker: Finished task $img")
    } yield ()
    fiber <- loop.forever.fork
  } yield (queue, fiber)
}

object Consumer {
  def createM[A](title: String): UIO[Consumer[A]] = UIO.succeed(Consumer[A](title))
}