package me.rotemfo.set

import scala.util.Random

sealed trait NumberOfShapes

object NumberOfShapes {
  case object One extends NumberOfShapes

  case object Two extends NumberOfShapes

  case object Three extends NumberOfShapes

  val values: Array[NumberOfShapes] = Array(One, Two, Three)

  def random: NumberOfShapes = {
    values(Random.nextInt(3))
  }

}