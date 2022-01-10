package me.rotemfo.zio

import scala.util.Random

/**
  * project: scala-playground
  * package: me.rotemfo.zio
  * file:    Domain
  * created: 2019-09-21
  * author:  rotem
  */
sealed trait Diagnostic

case object HipDiagnostic extends Diagnostic

case object KneeDiagnostic extends Diagnostic

case class Request[A](topic: Diagnostic, XRayImage: A)

trait RequestGenerator[A] {
  def generate(topic: Diagnostic): Request[A]
}

case class IntRequestGenerator() extends RequestGenerator[Int] {
  override def generate(topic: Diagnostic): Request[Int] = Request(topic, Random.nextInt(1000))
}