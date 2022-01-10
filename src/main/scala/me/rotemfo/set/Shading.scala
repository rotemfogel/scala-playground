package me.rotemfo.set

import scala.util.Random

sealed trait Shading

object Shading {
  case object Solid extends Shading

  case object Striped extends Shading

  case object Open extends Shading

  val values: Array[Shading] = Array(Solid, Striped, Open)

  def random: Shading = {
    values(Random.nextInt(3))
  }
}