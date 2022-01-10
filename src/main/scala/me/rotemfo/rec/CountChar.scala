package me.rotemfo.rec

import scala.annotation.tailrec

object CountChar {
  def countChar(s: String, c: Char): Int = {
    @tailrec
    def count(xs: Array[Char], acc: Int): Int = {
      if (xs.tail.isEmpty) acc
      else count(xs.tail, if (xs.head.equals(c)) acc + 1 else acc)
    }

    if (s.isEmpty) 0 else count(s.toCharArray, 0)
  }

  def simpleCharCount(s: String, c: Char): Int = {
    val arr = s.toCharArray
    var count: Int = 0
    for (i <- 0 until arr.length - 1) {
      if (arr(i).equals(c)) count += 1
    }
    count
  }

  def main(args: Array[String]): Unit = {
    val s: String = "ss ccbbb ddsss obnsd kljsn dg"
    assert(simpleCharCount(s, ' ') == 5)
    assert(simpleCharCount(s, 'k') == 1)
    assert(simpleCharCount(s, 'b') == 4)
    assert(simpleCharCount(s, 's') == 7)
    assert(simpleCharCount("", 'q') == 0)

    assert(countChar(s, ' ') == 5)
    assert(countChar(s, 'k') == 1)
    assert(countChar(s, 'b') == 4)
    assert(countChar(s, 's') == 7)
    assert(countChar("", 'q') == 0)
  }
}
