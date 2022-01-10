package me.rotemfo.zio

import zio._
import zio.console._
import zio.duration.Duration

import java.io.IOException
import java.util.concurrent.TimeUnit

/**
  * project: scala-playground
  * package: me.rotemfo.zio
  * file:    Producer
  * created: 2019-09-21
  * author:  rotem
  */
case class Producer[A](queue: Queue[Request[A]], generator: RequestGenerator[A]) {
  def run: URIO[clock.Clock with Console, Fiber.Runtime[IOException, Nothing]] = {
    val loop = for {
      _ <- putStrLn("[XRayRoom] generating hip and knee request")
      _ <- queue.offer(generator.generate(HipDiagnostic))
      _ <- queue.offer(generator.generate(KneeDiagnostic))
      _ <- ZIO.sleep(Duration(2, TimeUnit.SECONDS))
    } yield ()
    loop.forever.fork
  }
}

object Producer {
  def createM[A](queue: Queue[Request[A]], generator: RequestGenerator[A]): UIO[Producer[A]] = UIO.succeed(Producer(queue, generator))
}