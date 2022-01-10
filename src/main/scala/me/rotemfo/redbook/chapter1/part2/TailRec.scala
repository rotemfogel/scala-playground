package me.rotemfo.redbook.chapter1.part2

import scala.annotation.tailrec

object TailRec {
  def findFirst[A](xs: Array[A], f: A => Boolean): Int = {
    @tailrec
    def loop(n: Int): Int = {
      if (n >= xs.length) -1
      else if (f(xs(n))) n
      else loop(n + 1)
    }

    loop(0)
  }

  def isSorted[A](as: Array[A], f: (A, A) => Boolean): Boolean = {
    @tailrec
    def work(n: Int): Boolean = {
      if (n >= as.length - 1) true
      else if (!f(as(n), as(n + 1))) false
      else work(n + 1)
    }

    work(0)
  }

  def partial1[A, B, C](a: A, f: (A, B) => C): B => C = b => f(a, b)

  def curry[A, B, C](f: (A, B) => C): A => B => C = a => b => f(a, b)

  def uncurry[A, B, C](f: A => B => C): (A, B) => C = (a, b) => f(a)(b)

  def compose[A, B, C](f: B => C, g: A => B): A => C = a => f(g(a))


  def main(args: Array[String]): Unit = {
    println("-" * 100)
    case class Bar(i: Int)
    case class Foo(bar: Bar)

    val array = Array(1, 2, 3)
    val strArr = array.map(_.toString)
    val barArr = array.map(Bar)
    val fooArr = barArr.map(Foo)

    println("-" * 100)
    println(findFirst[Int](array, f = i => i % 3 == 0))
    println(findFirst[String](strArr, f = s => s.equals("2")))
    println(findFirst[Bar](barArr, f = b => b.i % 2 == 0))
    println(findFirst[Foo](fooArr, f = foo => foo.bar.i % 3 == 0))

    println("-" * 100)
    val arr = array.map(i => if (i % 2 == 0) i * 2 else i)
    println(isSorted[Int](array, (i, j) => i < j))
    println(isSorted[Int](arr, (i, j) => i < j))
    println(isSorted[String](strArr, (i, j) => i < j))
    println(isSorted[Bar](barArr, (i, j) => i.i < j.i))
    println(isSorted[Foo](fooArr, (i, j) => i.bar.i < j.bar.i))
  }
}
