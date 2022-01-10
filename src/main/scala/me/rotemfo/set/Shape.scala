package me.rotemfo.set

import scala.util.Random

sealed trait Shape

object Shape {
  case object Diamond extends Shape

  case object Squiggle extends Shape

  case object Oval extends Shape

  val values: Array[Shape] = Array(Diamond, Squiggle, Oval)

  def random: Shape = {
    values(Random.nextInt(3))
  }

}
