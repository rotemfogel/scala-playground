package me.rotemfo.test

import java.time.LocalDateTime

object Underscore extends App {
  def timesTwo: Int => Long = x => x * 2

  val range = 1 to 10
  range.map(timesTwo).foreach(println)
  // equals to
  range.map(e => e * 2).foreach(e => println(e))
  // equals to
  range.map(_ * 2).foreach(println(_))

}

trait result

case object success extends result

case class failure(reason: String) extends result

object test {
  def check(p: Seq[LocalDateTime]): result = {
    if (p.length == 1) success
    else if (p.head.isBefore(p.last)) {
      if (p.last.toLocalDate.isAfter(p.head.toLocalDate)) failure("cannot spill sessions over the boundaries of a day")
      else success
    } else failure("start date is after end date")
  }

  def main(args: Array[String]): Unit = {
    val now = LocalDateTime.of(2020, 1, 1, 1, 0)
    val p = Seq(now) // , now.plusHours(1))
    println(check(p))
  }
}