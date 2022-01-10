package me.rotemfo.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    HttpMaikn
  * created: 2018-10-07
  * author:  rotem
  */
object HttpMain extends App {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  implicit val materializer: Materializer = Materializer.createMaterializer(actorSystem)

  private var host: String = "localhost"
  private val port: Int = 80

  private val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = Http().outgoingConnection(host, port)

  def execute(): Unit = {
    val f = Source.single(HttpRequest(method = HttpMethods.GET, uri = "/")).via(connectionFlow).runWith(Sink.head)
    val r: HttpResponse = Await.result[HttpResponse](f, 2.seconds)
    val body = Await.result[String](r.entity.toStrict(500.millis).map(_.data).map(_.utf8String), 100.milli)
    println(body)
  }

  execute()
  host = "www.sldgh.skdgui"
  execute()

  actorSystem.terminate()
}
