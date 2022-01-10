package me.rotemfo.akka

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    SharedMessages
  * created: 2018-09-16
  * author:  rotem
  */
object SharedMessages {
  case class StartMessage(body: String)

  case class WorkerResponseMessage(body: String)

  case class WorkerMessage(body: String)
}
