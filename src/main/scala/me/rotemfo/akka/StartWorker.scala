package me.rotemfo.akka

import akka.actor.{ActorSystem, Props}

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    StartWorker
  * created: 2018-09-16
  * author:  rotem
  */
object StartWorker extends App {

  implicit val system: ActorSystem = ActorSystem(AkkaConfig.clusterName)
  system.actorOf(Props[Worker], name = "workerActor")
}
