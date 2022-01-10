package me.rotemfo.aws.ecrs

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.ecs.AmazonECSAsyncClientBuilder
import com.amazonaws.services.ecs.model._

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * project: scala-playground
  * package: me.rotemfo.aws.ecrs
  * file:    ECRManager
  * created: 2019-09-04
  * author:  rotem
  */
object ECSManager {
  def main(args: Array[String]): Unit = {
    val client = AmazonECSAsyncClientBuilder
      .standard()
      .withRegion(Regions.US_EAST_1) // acme Docker repository is in US_EAST_1
      .withCredentials(new ProfileCredentialsProvider("integration"))
      .build()

    val taskDefinitions = mutable.Buffer[String]()

    var taskDefinitionsResult = client.listTaskDefinitions(new ListTaskDefinitionsRequest())
    var c: Boolean = true
    while (c) {
      taskDefinitions ++= taskDefinitionsResult.getTaskDefinitionArns.asScala
      val nextToken: String = taskDefinitionsResult.getNextToken
      if (nextToken != null && nextToken.nonEmpty) {
        taskDefinitionsResult = client.listTaskDefinitions(new ListTaskDefinitionsRequest().withNextToken(nextToken))
      } else c = false
    }
    taskDefinitions
      .map(s => {
        val a = s.split(":")
        val ss = a.dropRight(1).mkString(":")
        (ss, a.last.toInt)
      }).groupBy(_._1).mapValues(_.maxBy(_._2)).mapValues(_._2).map(s => s._1 + ":" + s._2)
      .map(t => {
        val taskDefinition = client.describeTaskDefinition(new DescribeTaskDefinitionRequest().withTaskDefinition(t)).getTaskDefinition
        val value = taskDefinition.getContainerDefinitions.asScala.head.getEnvironment.asScala.flatMap(kv => Map(kv.getName -> kv.getValue)).toMap
        value.map({ case (k, v) => k + ": " + v }).toSeq.sorted.mkString("\n\t")
      })
      .foreach(println)

  }
}
