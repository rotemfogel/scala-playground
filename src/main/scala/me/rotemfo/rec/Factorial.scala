package me.rotemfo.rec

import scala.annotation.tailrec

object Factorial {

  def factorial1(n: Int): Int = {
    @tailrec
    def iter(i: Int, acc: Int): Int = {
      if (i == 0) acc
      else iter(i - 1, i * acc)
    }

    iter(n, 1)
  }

  def factorial2(n: Int): Int = {
    @tailrec
    def iter(i: Int, acc: Int): Int = {
      if (i > 0) iter(i - 1, i * acc)
      else acc
    }

    iter(n, 1)
  }

  def main(args: Array[String]): Unit = {
    println(factorial1(5))
    println(factorial2(5))
  }
}
