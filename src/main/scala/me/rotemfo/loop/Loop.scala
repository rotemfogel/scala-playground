package me.rotemfo.loop

/**
  * project: scala-playground
  * package: me.rotemfo.loop
  * file:    Loop
  * created: 2019-07-17
  * author:  rotem
  */
object Loop {
  final val buckets = 10

  def main(args: Array[String]): Unit = {
    val recordsCount: Long = 598735
    val step: Long = recordsCount / 10
    var next: Long = 0
    while (next < recordsCount) {
      val from = next
      val to = Math.min(next + step - 1, recordsCount)
      next = next + step
      println(s"$from\t->\t$to")
    }
  }
}
