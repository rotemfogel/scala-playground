package me.rotemfo.akka

import akka.actor.Actor

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    Coordinator
  * created: 2018-09-16
  * author:  rotem
  */
class Coordinator extends Actor {

  import AkkaConfig._
  import SharedMessages._

  override def receive: Receive = {
    case StartMessage(body) =>
      println(body)
      val workerActorRef = context.actorSelection(s"akka.tcp://$clusterName@$clusterHost:$clusterPort/workerActor")
      workerActorRef ! WorkerMessage(s"greetings from coordinator: $body")
    case WorkerResponseMessage(body) =>
      println(s"Coordinator actor received response from worker: $body")
  }
}
