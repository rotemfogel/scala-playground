package me.rotemfo.akka

import akka.actor.Actor
import me.rotemfo.akka.SharedMessages.{WorkerMessage, WorkerResponseMessage}

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    Worker
  * created: 2018-09-16
  * author:  rotem
  */
class Worker extends Actor {
  override def receive: Receive = {
    case WorkerMessage(body) =>
      println(s"Worker actor received the message $body")
      sender ! WorkerResponseMessage("Item processed successfully")
  }
}
