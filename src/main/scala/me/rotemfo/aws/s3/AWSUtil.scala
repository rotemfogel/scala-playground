package me.rotemfo.aws.s3

/**
  * project: acmesecurity
  * package: com.acmesecurity.management.upgrade.services.aws
  * file:    aws
  * created: 2018-09-13
  * author:  rotem
  */
object AWSUtil {

  val VERSION: String = "version"
  val PREV_VERSION: String = "pversion"
  val KEY: String = "key"
  val PREV_KEY: String = "pkey"

  val LATEST: String = "latest"

  /**
    * parse a docker image
    *
    * @param image image string
    * @return array of image parts
    */
  def parseImage(image: String): Array[String] = {
    stripRepo(image).split(":")
  }

  /**
    * strip repository from image
    *
    * @param image image string
    * @return repo name and image version
    */
  def stripRepo(image: String): String = {
    image.split("/").last
  }

  /**
    * parse string to int with error handling
    *
    * @param s the string
    * @return the integer value of the String
    */
  def asInt(s: String): Int = {
    try {
      s.toInt
    } catch {
      case _: NumberFormatException => -1
    }
  }

  /**
    * get bare jar name
    *
    * @param name original name
    * @return bare name
    */
  def getBareJarName(name: String): String = {
    splitJarName(name).dropRight(1).mkString("-")
  }

  /**
    * get jar version
    *
    * @param name object name
    * @return version
    */
  def getJarVersion(name: String): String = {
    // 1. first drop the .jar suffix
    // 2. split by minus sign - return Array(acme, parsers, custom, acme_2.12, 1.0.294)
    val parts = splitJarName(name)
    if (parts.last.equals(LATEST)) parts(parts.length - 2) else parts.last
  }

  /**
    * split jar name into its components
    *
    * @param name jar name
    * @return array of parts
    */
  private def splitJarName(name: String): Array[String] = {
    name.dropRight(name.length - name.lastIndexOf(".")).split("-")
  }
}
