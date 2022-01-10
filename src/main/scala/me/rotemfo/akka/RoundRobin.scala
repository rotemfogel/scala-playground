package me.rotemfo.akka

import akka.actor.Props
import akka.routing.RoundRobinPool

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    RoundRobin
  * created: 2018-11-28
  * author:  rotem
  */
object RoundRobin extends App {
  def sleep(): Unit = {
    try {
      Thread.sleep(2000)
    } catch {
      case _: InterruptedException =>
    }

  }

  val system = System.system
  val roundRobinPool = RoundRobinPool(4)
  val router = system.actorOf(roundRobinPool.props(Props[LoggerActor]))
  sleep()
  (1 to 10).foreach(i => router ! i)
  sleep()
  system.terminate
}
