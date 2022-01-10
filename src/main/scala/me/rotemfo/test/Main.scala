package me.rotemfo.test

import me.rotemfo.util.JsonUtil

/**
  * project: scala-playground
  * package: me.rotemfo.test
  * file:    Main
  * created: 2018-12-30
  * author:  rotem
  */
object Main extends App {
  //  A1.inc
  //  A1.print
  //  A2.inc
  //  A2.print
  //  A1.inc
  //  A1.print
  //  A2.print

  def parse(path: String): String = {
    JsonUtil.toJson(path.split("/").filter(_.contains("=")).map(_.split("=")).map(e => (e.head, e.last)).toMap)
  }

  val data = Seq("/a/b/c/date_=2020-01-01/hour=01", "/a/b/c/date_=2021-01-01/hour=02")
  data.foreach(e => println(parse(e)))
}