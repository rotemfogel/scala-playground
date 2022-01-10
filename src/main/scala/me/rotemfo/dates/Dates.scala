package me.rotemfo.dates

import java.time._
import java.time.format.DateTimeFormatter

object Dates {

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH")

  def main(args: Array[String]): Unit = {
    val now: LocalDateTime = LocalDateTime.of(2022, 1, 5, 5, 0, 0)
    val yesterday: LocalDateTime = now.minusDays(1)
    val iterator: Iterator[LocalDateTime] = Iterator.iterate(yesterday)(d => d.plusHours(1)).takeWhile(d => d.isBefore(now))
    var previous: Option[String] = None
    while (iterator.hasNext) {
      val ldt: LocalDateTime = iterator.next()
      if (previous.isEmpty)
        previous = Some(formatter.format(ldt))
      else {
        val current = formatter.format(ldt)
        println(s"${previous.get},$current")
        previous = Some(current)
      }
    }
  }
}
