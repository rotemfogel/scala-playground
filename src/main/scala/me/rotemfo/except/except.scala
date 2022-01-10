package me.rotemfo.except

import scala.util.{Failure, Random, Success, Try}

case class MultiDeleteObjectsResult(deleted: Seq[String])

case class MultiObjectDeleteException(deleted: Seq[String], errors: Seq[String]) extends Exception("MultiObjectDeleteException", None.orNull)

object Except {
  private val random: Random = Random

  private val deleted: Seq[String] = Seq("1")

  @throws[MultiObjectDeleteException]
  def foo: MultiDeleteObjectsResult = {
    random.nextInt(2) match {
      case 0 => MultiDeleteObjectsResult(deleted)
      case 1 => throw MultiObjectDeleteException(deleted, Seq("12"))
    }
  }

  def main(args: Array[String]): Unit = {
    Range(1, 50).foreach(_ => {
      val maybe = Try(foo)
      maybe match {
        case Success(s) => println(s.deleted)
        case Failure(x) => println(x.asInstanceOf[MultiObjectDeleteException].errors)
      }
    })
  }
}