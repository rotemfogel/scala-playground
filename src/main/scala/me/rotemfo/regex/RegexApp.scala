package me.rotemfo.regex

import scala.io.Source
import scala.util.matching.Regex

class SourceReader(val file: String) {

  def fromFile: List[String] = {
    val source = Source.fromFile(file, "UTF-8")
    val lines = source.getLines().toList
    source.close()
    lines
  }
}

object SourceReader {
  def fromFile(file: String): List[String] = {
    new SourceReader(file).fromFile
  }
}

object RegexApp extends App {
  val regex: Regex = """(\S+)\s(\S+)\s\[([^\]]+)\]\s(\S+)\s(\S+)\s(\S+)\s(\S+)\s(\S+)\s(-|"-"|"\S+ \S+ (?:-|\S+)")\s(\S+)\s(\S+)\s(\S+)\s(\S+)\s(\S+)\s(\S+)\s(-|"[^"]+")\s(-|"[^"]+")\s(\S+)(.*)""".r

  val lines = SourceReader.fromFile("/disk1/data/s3_logs/emr2021-11-08-00-11-07-BF915D48E05D9FF4")
  for (line <- lines) {
    line match {
      case regex(bucketOwner, bucketName, requestDateTime, remoteIp, requester, requestId, operation,
      key, requestUri, httpStatus, errorCode, bytesSent, objectSize, totalTime, turnAroundTime, referrer,
      userAgent, versionId, additional) => println("regex")
      case _ => println("none")
    }
  }
}
