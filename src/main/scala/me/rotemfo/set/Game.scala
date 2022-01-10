package me.rotemfo.set

import me.rotemfo.set.Rules.isSet

import java.util.concurrent.atomic.{AtomicBoolean, AtomicLong}

object Game {

  def main(args: Array[String]): Unit = {
    val setFound: AtomicBoolean = new AtomicBoolean(false)
    val trials: AtomicLong = new AtomicLong(0)
    val deck: Deck = new Deck()
    try {
      while (!setFound.get()) {
        val c1: Card = deck.draw()
        val c2: Card = deck.draw()
        val c3: Card = deck.draw()
        setFound.set(isSet(c1, c2, c3))
        if (setFound.get())
          println(s"Set found after ${trials.get()} trials\nc1: $c1\nc2: $c2\nc3: $c3}")
        trials.incrementAndGet()
      }
    } catch {
      case _: EmptyDeckException => println("No set found")
    }
  }
}
