package me.rotemfo.foo

object Foo extends App {

  val add_one: Int => Int = i => i + 1
  val multiply_by_two: Int => Int = i => i * 2
  val subtract_one: Int => Int = i => i - 1

  val map: Map[String, Int => Int] = Map(
    "add_one" -> add_one,
    "multiply_by_two" -> multiply_by_two,
    "subtract_one" -> subtract_one
  )

  val seq: Seq[Int] = Seq(1, 10, 2, 5, 20, 34)
  seq.foreach(v => map.foreach { case (k, fn) => println(s"$k($v) => ${fn(v)}") })
  println(Array.fill[String](30)("-").mkString(""))
  map.foreach { case (k, fn) => seq.foreach(v => println(s"$k($v) => ${fn(v)}")) }
}
