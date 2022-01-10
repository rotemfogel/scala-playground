package me.rotemfo.akka

import akka.actor.{ActorSystem, Props}

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    Start
  * created: 2018-09-16
  * author:  rotem
  */
object StartCoordinator extends App {

  import SharedMessages._

  implicit val system: ActorSystem = ActorSystem(AkkaConfig.clusterName)
  val coordinatorRef = system.actorOf(Props[Coordinator])
  coordinatorRef ! StartMessage("start message")
}
