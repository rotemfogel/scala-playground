package me.rotemfo.de

import scala.annotation.tailrec

object Exam {

  def removeSpaces(in: String): String = {
    @tailrec
    def _removeSpaces(in: Array[Char], acc: Array[Char]): String = {
      if (in.isEmpty) acc.mkString("")
      else {
        if (in.head == ' ' && acc.isEmpty) // first time
          _removeSpaces(in.tail, acc)
        else
          _removeSpaces(in.tail, acc :+ in.head)
      }
    }

    _removeSpaces(_removeSpaces(in.toCharArray, Array.empty[Char]).reverse.toCharArray, Array.empty[Char]).reverse
  }

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    println(s"[${removeSpaces("  hello world  ")}]")
  }
}
