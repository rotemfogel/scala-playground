package me.rotemfo.either

import scala.annotation.tailrec

object EitherApp {
  private lazy val statistics: Map[Int, Either[Int, Set[Int]]] =
    Map(
      1 -> Right(Set(1, 2, 3, 4, 5)),
      2 -> Left(1),
      3 -> Left(1)
    )

  @tailrec
  private def getStatistics(i: Int): Set[Int] = {
    statistics(i) match {
      case Right(list) => list
      case Left(n) => getStatistics(n)
    }
  }

  def main(args: Array[String]): Unit = {
    Range.inclusive(1, 3).map(getStatistics).foreach(println)
  }
}
