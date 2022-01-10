package me.rotemfo.redbook.chapter1.part3

sealed trait LinkedList[+A]

case object Nil extends LinkedList[Nothing]

case class Node[+A](head: A, tail: LinkedList[A]) extends LinkedList[A]

object LinkedList {
  def sum(ints: LinkedList[Int]): Int = ints match {
    case Nil => 0
    case Node(x, xs) => x + sum(xs)
  }

  def product(ds: LinkedList[Double]): Double = ds match {
    case Nil => 1.0
    case Node(0.0, _) => 0.0
    case Node(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): LinkedList[A] = {
    if (as.isEmpty) Nil
    else Node(as.head, apply(as.tail: _*))
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val ex1: LinkedList[Double] = Nil
    val ex2: LinkedList[Int] = Node(1, Nil)
    val ex3: LinkedList[String] = Node("a", Node("b", Nil))
    println(ex1)
    println(ex2)
    println(ex3)
  }
}