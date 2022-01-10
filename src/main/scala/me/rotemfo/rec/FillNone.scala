package me.rotemfo.rec

import scala.annotation.tailrec
import scala.util.Try

object FillNone {
  def fillNone(xs: List[Option[Int]]): List[Int] = {
    @tailrec
    def fill(o: List[Option[Int]], n: List[Int], last: Option[Int]): List[Int] = {
      if (o.isEmpty) n
      else {
        fill(o.tail,
          n :+ (if (o.head.nonEmpty) o.head.get else last.get),
          if (o.head.nonEmpty) o.head else last)
      }
    }

    if (xs.isEmpty) List()
    else {
      require(xs.head.nonEmpty, "first value must be present !")
      fill(xs, List(), None)
    }
  }

  def main(args: Array[String]): Unit = {
    val input: List[Option[Int]] = List(Some(1), None, Some(1), Some(2), None, None, Some(3), Some(4), None, None, Some(5))
    val output: List[Int] = List(1, 1, 1, 2, 2, 2, 3, 4, 4, 4, 5)
    val result = fillNone(input)
    require(result.equals(output))
    require(fillNone(List()) == List())
    require(Try(fillNone(List(None))).isFailure)
  }
}
