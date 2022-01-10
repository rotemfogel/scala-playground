package me.rotemfo.util

import org.json4s.native.JsonMethods._
import org.json4s.native._
import org.json4s.{DefaultFormats, Extraction}

object JsonUtil {

  implicit val formats: DefaultFormats.type = DefaultFormats

  /**
    * util function to extract T type from json string
    *
    * @param s the jsong string
    * @return T
    */
  def fromJson[T](s: String)(implicit m: Manifest[T]): T = parseJson(s).extract[T]

  /**
    * util function to create a json string from any
    *
    * @param a any object
    * @return json string
    */
  def toJson(a: Any): String = compact(render(Extraction.decompose(a)))
}
