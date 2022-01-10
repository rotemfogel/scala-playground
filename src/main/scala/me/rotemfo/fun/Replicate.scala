package me.rotemfo.fun

import java.time.LocalDate

object Replicate {

  private val functions = List((x: Int) => x + 1, (x: Int) => x - 1, (x: Int) => x * 2)

  private def repeat(n: Int, text: String): String = List.fill(n)(text).mkString(" ")

  def replicate(n: Int, text: String): String = repeat(n, text)

  val replicateVal: (Int, String) => String = (n: Int, text: String) => repeat(n, text)

  def createDate(year: Int, month: Int, day: Int): LocalDate = LocalDate.of(year, month, day)

  val createDateVal: (Int, Int, Int) => LocalDate = (year: Int, day: Int, month: Int) => createDate(year, month, day)

  def filter(text: String, predicate: Char => Boolean): String = text.filter(predicate)

  def map(text: String, f: Char => Char): String = {
    text.toArray.map(f(_)).mkString("")
  }

  def main(args: Array[String]): Unit = {
    println(functions.apply(1))

    val hello = "Hello"
    val helloWorld = s"$hello World"

    println(replicate(2, hello))
    println(replicateVal(2, hello))

    println("")
    println(filter(helloWorld, (c: Char) => c.isUpper))
    println(filter(helloWorld, (c: Char) => c.isLetter))

    println("")
    println(map(helloWorld, (c: Char) => c.toUpper))
    println(map(helloWorld, (c: Char) => c.toLower))

    //    println(functions(0)(10))
    //    println(functions(1)(10))
    //    println(functions(2)(10))
  }
}
