package me.rotemfo.zio

import zio.{ExitCode, UIO, URIO, ZIO}

object ZioFibers extends zio.App {
  val showerTime: UIO[String] = ZIO.succeed("Taking a Shower")
  val boilingWater: UIO[String] = ZIO.succeed("Boiling some water")
  val prepareCoffee: UIO[String] = ZIO.succeed("Prepare some Coffee")

  def synchronousRoutine(): UIO[Unit] =
    for {
      _ <- showerTime
      _ <- boilingWater
      _ <- prepareCoffee
    } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    synchronousRoutine().exitCode
  }
}
