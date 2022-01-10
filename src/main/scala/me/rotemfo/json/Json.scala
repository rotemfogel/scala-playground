package me.rotemfo.json

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import me.rotemfo.util.JsonUtil

import scala.io.Source
import scala.util.Try

object Json {
  private final val EMPTY_LIST = List.empty[String]

  def parse(dashboards: java.util.List[java.util.Map[String, Object]]): Map[Int, List[String]] = {
    import scala.collection.JavaConverters._
    dashboards.asScala.map(d => {
      import scala.collection.JavaConverters._
      try {
        val id: Int = Integer.parseInt(d.get("id").toString)
        val elements = d.get("dashboard_elements").asInstanceOf[java.util.List[java.util.LinkedHashMap[String, Object]]]
        val fields: List[String] = elements.asScala.flatMap(e => {
          val query = e.get("query").asInstanceOf[java.util.LinkedHashMap[String, Object]]
          val maybeFields = Try(query.get("fields").asInstanceOf[java.util.List[String]].asScala.filter(_.contains("delighted")))
          if (maybeFields.isSuccess) maybeFields.get else EMPTY_LIST
        }).toList
        (id, fields)
      } catch {
        case _: NumberFormatException => (0, EMPTY_LIST)
      }
    }).filter(_._2.nonEmpty).toMap
  }

  def parse(dashboards: List[Map[String, Any]]): Map[Int, List[String]] = {
    dashboards.map(d => {
      try {
        val id: Int = Integer.parseInt(d("id").toString)
        val elements = d("dashboard_elements").asInstanceOf[List[Map[String, Any]]]
        val fields: List[String] = elements.flatMap(e => {
          val query = e("query").asInstanceOf[Map[String, Any]]
          val maybeFields = Try(query("fields").asInstanceOf[List[String]].filter(_.contains("delighted")))
          if (maybeFields.isSuccess) maybeFields.get else EMPTY_LIST
        })
        (id, fields)
      }
      catch {
        case _: NumberFormatException => (0, EMPTY_LIST)
      }
    }).filter(_._2.nonEmpty).toMap
  }

  def json4s[T](in: String)(implicit m: Manifest[T]): T = {
    JsonUtil.fromJson[T](in)
  }

  def jackson[T](in: String): T = {
    val mapper = new ObjectMapper()
    mapper.readValue(in, new TypeReference[T]() {})
  }

  def main(args: Array[String]): Unit = {
    val source = Source.fromFile("dashboards.json")
    var start = System.currentTimeMillis()
    val json = source.getLines().mkString("")
    System.out.println(s"source.getLines() took ${System.currentTimeMillis() - start} ms")
    source.close()

    start = System.currentTimeMillis()
    val json4sDashboards = json4s[List[Map[String, Any]]](json)
    System.out.println(s"json4s parse took ${System.currentTimeMillis() - start} ms")
    val json4sDelighted = parse(json4sDashboards)
    println(json4sDelighted)

    start = System.currentTimeMillis()
    val jacksonDashboards = jackson[java.util.List[java.util.Map[String, Object]]](json)
    System.out.println(s"jackson parse took ${System.currentTimeMillis() - start} ms")
    val jacksonDelighted = parse(jacksonDashboards)
    println(jacksonDelighted)
  }
}
