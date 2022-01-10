package me.rotemfo.akka

import akka.actor.{ActorSystem, Address, Props}
import akka.cluster.Cluster
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.RoundRobinPool
import com.typesafe.config.ConfigValueFactory._
import me.rotemfo.akka.SystemConfig.config

import scala.collection.mutable

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    Clustered
  * created: 2018-11-28
  * author:  rotem
  */
object Clustered {
  def main(args: Array[String]): Unit = {
    val systems = mutable.Buffer[ActorSystem]()
    Array(2500, 2501, 2502).foreach(port => systems += createCluster(port))
    val roundRobinPool = RoundRobinPool(4)
    val clusterRouterPoolSettings = ClusterRouterPoolSettings(4, 2, allowLocalRoutees = true, Set.empty[String])
    val clusterRouterPool = ClusterRouterPool(roundRobinPool, clusterRouterPoolSettings)
    val router = systems.head.actorOf(clusterRouterPool.props(Props[LoggerActor]))
    (1 to 10).foreach(i => router ! i)
  }

  private def createCluster(port: Int): ActorSystem = {
    val localConf = if (port == 2500) {
      config.withValue("akka.remote.netty.tcp.port", fromAnyRef("2500"))
        .withValue("akka.actor.provider", fromAnyRef("akka.cluster.ClusterActorRefProvider"))
    }
    else
      config.withValue("akka.cluster.seed-nodes.0", fromAnyRef("akka.tcp://system@127.0.0.1:2500"))
    val system = ActorSystem("system", localConf)
    if (port == 2500)
      Cluster(system).join(Address("akka.tcp", "system", "127.0.0.1", 2500))
    sys.addShutdownHook(system.terminate())
    system
  }
}
