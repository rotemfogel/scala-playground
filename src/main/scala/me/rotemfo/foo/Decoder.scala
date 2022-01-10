package me.rotemfo.foo

import jdk.nashorn.internal.objects.Global.println

import java.net.URLEncoder.encode
import java.nio.charset.StandardCharsets
import scala.annotation.tailrec

object Decoder {
  private final val decoded: String = "&AA=A&BB=B"
  private final val utf8: String = StandardCharsets.UTF_8.name
  private final val pattern = "%\\d".r

  /**
    * tail recursion for decoding URLs
    *
    * @param text - the URL string to decode
    * @return Option[String]
    */
  @tailrec
  def decodeUrlRec(text: String): Option[String] = {
    if (text == null) None
    else {
      val trimmed = text.trim
      if (trimmed.length <= 1 || pattern.findFirstIn(trimmed).isEmpty) Some(trimmed)
      else decodeUrlRec(java.net.URLDecoder.decode(trimmed, StandardCharsets.UTF_8.name))
    }
  }

  def decodeUrl(text: String): Option[String] = {
    try {
      decodeUrlRec(text: String)
    } catch {
      case _: Exception => Some(s"""{"decoding_failure":"$text"}""")
    }
  }

  @tailrec
  private def _encode(text: String, times: Int): String = {
    if (times == 0) text
    else _encode(encode(text, utf8), times - 1)
  }

  def main(args: Array[String]): Unit = {
    Range.inclusive(1, 5).foreach(i => {
      val encoded = _encode(decoded, i)
      val nDecoded = decodeUrl(encoded)
      println(s"$i:")
      println(encoded)
      println(nDecoded)
    })
  }


}
