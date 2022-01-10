package me.rotemfo.test

import cats.effect._

object Stam extends IOApp.Simple {

  // pure idempotent
  private val plus1: Int => (Int, String) = i => (i + 1, s"added 1 to $i")
  // pure idempotent
  private val log: String => IO[Unit] = s => IO.println(s)

  override def run: IO[Unit] = {
    plus1.andThen(r => log(r._2))(1)
  }
}
