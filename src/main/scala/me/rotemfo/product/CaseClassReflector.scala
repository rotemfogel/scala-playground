package me.rotemfo.product

trait CaseClassReflector extends Product {
  def getFields: Map[String, AnyRef] = getClass.getDeclaredFields.map(field => {
    field.setAccessible(true)
    field.getName -> field.get(this)
  }).toMap
}

abstract class BaseApp[P <: CaseClassReflector](p: P) {
  def main(args: Array[String]): Unit = {
    p.getFields.foreach {
      case (k, v) => println(s"$k = ${v.toString}")
    }
    println(args.mkString("\n"))
  }
}

case class MyConfig(i: Int, d: Double, s: String) extends CaseClassReflector

object MyApp extends BaseApp[MyConfig](MyConfig(i = 1, d = 2.0, s = "s"))