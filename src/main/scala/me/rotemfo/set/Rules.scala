package me.rotemfo.set

object Rules {
  def isSet(c1: Card, c2: Card, c3: Card): Boolean = {
    val checkShape: Boolean = {
      (c1.shape == c2.shape &&
        c1.shape == c3.shape &&
        c2.shape == c3.shape) ||
        (c1.shape != c2.shape &&
          c1.shape != c3.shape &&
          c2.shape != c3.shape)
    }
    val checkNum: Boolean = {
      (c1.number == c2.number &&
        c1.number == c3.number &&
        c2.number == c3.number) ||
        (c1.number != c2.number &&
          c1.number != c3.number &&
          c2.number != c3.number)
    }
    val checkShade: Boolean = {
      (c1.shading == c2.shading &&
        c1.shading == c3.shading &&
        c2.shading == c3.shading) ||
        (c1.shading != c2.shading &&
          c1.shading != c3.shading &&
          c2.shading != c3.shading)
    }
    val checkColor: Boolean = {
      (c1.color == c2.color &&
        c1.color == c3.color &&
        c2.color == c3.color) ||
        (c1.color != c2.color &&
          c1.color != c3.color &&
          c2.color != c3.color)
    }
    checkShape && checkNum && checkShade && checkColor
  }
}
