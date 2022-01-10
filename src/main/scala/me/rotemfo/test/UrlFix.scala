package me.rotemfo.test

import java.io.PrintWriter
import scala.io.Source

object UrlFix extends App {
  private def firstLevel(url: String): String = {
    if (url.isEmpty || url.equals(" ") || url.equals("/")) "/"
    else {
      val parts = url.split("/")
      if (url.startsWith("/")) {
        parts(1)
      } else {
        parts(0)
      }
    }.split("\\?").head
  }

  val csvFiles = Array("/home/rotem/Downloads/google-in.csv") // , "/home/rotem/Downloads/vertica.csv")
  csvFiles.map(f => {
    val fileName = "/home/rotem/Downloads/" + f.split("/").last.split("\\.").head + "-load.csv"

    val source = Source.fromFile(f, "UTF-8")
    val lines = source.getLines().toList
    source.close()
    val counts = lines.map(l => {
      val parts = l.split(",")
      val (head, pageViews) = (parts.head, parts.last.replaceAll("\"", ""))
      val url = firstLevel(head)
      s"$url, ${
        pageViews.toInt
      }"
    })
    new PrintWriter(fileName) {
      write(counts.mkString("\n"))
      close()
    }
  })
}
