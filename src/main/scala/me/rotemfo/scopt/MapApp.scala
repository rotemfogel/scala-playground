package me.rotemfo.scopt

import scopt.OptionParser

trait AppConfig {
  val sparkConfig: Map[String, String]
}

trait TheApp[T <: AppConfig] {
  def getParser: OptionParser[T]
}

abstract class BaseApp[T <: AppConfig](t: T) extends TheApp[T] {
  def main(args: Array[String]): Unit = {
    getParser.parse(args, t).foreach(p => println(p.sparkConfig))
  }
}

// ---------------------------------------------------
// Actual code
// ---------------------------------------------------

case class MapConfig(override val sparkConfig: Map[String, String] = Map.empty[String, String]) extends AppConfig

object MapParser extends OptionParser[MapConfig](programName = "Map Test Application") {
  opt[Map[String, String]]("spark-config").required().action { (x, p) => p.copy(sparkConfig = x) }
}

object MapApp extends BaseApp[MapConfig](MapConfig()) {
  override def getParser: OptionParser[MapConfig] = MapParser
}
