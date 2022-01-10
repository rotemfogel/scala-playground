package me.rotemfo.akka

import akka.actor.{Actor, ActorLogging}

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    Actor
  * created: 2018-11-28
  * author:  rotem
  */
class LoggerActor extends Actor with ActorLogging {
  log.info("Logger started")

  override def receive: Receive = {
    case msg => log.info("Got msg: {}", msg)
  }
}
