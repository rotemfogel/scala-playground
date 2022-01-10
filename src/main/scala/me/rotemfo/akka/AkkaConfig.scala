package me.rotemfo.akka

import com.typesafe.config.{Config, ConfigFactory}

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    AkkaConfig
  * created: 2018-09-16
  * author:  rotem
  */
object AkkaConfig {
  private val config: Config = ConfigFactory.load().getConfig("akka")
  private val systemConfig = config.getConfig("system")
  val clusterName: String = systemConfig.getString("name")

  private val akkaConfig = config.getConfig("actor.remote.netty.tcp")
  val clusterHost: String = akkaConfig.getString("hostname")
  val clusterPort: Int = akkaConfig.getInt("port")
}
