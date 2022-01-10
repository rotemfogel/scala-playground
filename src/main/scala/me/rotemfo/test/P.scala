package me.rotemfo.test

/**
  * project: scala-playground
  * package: me.rotemfo.test
  * file:    P
  * created: 2018-12-30
  * author:  rotem
  */
class P {
  private var i: Int = 0

  protected def increment(): Unit = {
    i = i + 1
  }

  def print(): Unit = println(i)
}
