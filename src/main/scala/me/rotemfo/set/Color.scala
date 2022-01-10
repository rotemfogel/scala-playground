package me.rotemfo.set

import scala.util.Random

sealed trait Color

object Color {
  case object Red extends Color

  case object Green extends Color

  case object Purple extends Color

  val values: Array[Color] = Array(Red, Green, Purple)

  def random: Color = {
    values(Random.nextInt(3))
  }
}
