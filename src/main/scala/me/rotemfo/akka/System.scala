package me.rotemfo.akka

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    System
  * created: 2018-11-28
  * author:  rotem
  */
object System {

  import SystemConfig._

  val system: ActorSystem = ActorSystem("system", config)
  sys.addShutdownHook(system.terminate())
}

object SystemConfig {
  val config: Config = ConfigFactory.load()
}
