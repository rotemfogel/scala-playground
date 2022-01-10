package me.rotemfo.akka

import akka.actor._
import akka.event.{Logging, LoggingAdapter}
import akka.pattern.{CircuitBreaker, ask}
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}

/**
  * project: scala-playground
  * package: me.rotemfo.akka
  * file:    FastSlowAkkademyDb
  * created: 2018-10-02
  * author:  rotem
  */

case class SetRequest(key: String, value: Object)

case class GetRequest(key: String)

final case class KeyNotFoundException(private val key: String,
                                      private val cause: Throwable = None.orNull)
  extends Exception(s"$key not found", cause)

class FastSlowAkkademyDb extends Actor {
  val map = new mutable.HashMap[String, Object]()
  val log: LoggingAdapter = Logging(context.system, this)

  override def receive: PartialFunction[Any, Unit] = {
    case SetRequest(key, value) =>
      log.info("received SetRequest - key: {} value: {}", key, value)
      map.put(key, value)
      sender() ! Status.Success
    case GetRequest(key) =>
      Thread.sleep(70)
      respondToGet(key)
    case _ => Status.Failure(new ClassNotFoundException)
  }

  def respondToGet(key: String): Unit = {
    val response: Option[Object] = map.get(key)
    response match {
      case Some(x) => sender() ! x
      case None => sender() ! Status.Failure(KeyNotFoundException(key))
    }
  }
}

object Main extends App {
  val system = ActorSystem("Akkademy")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val breaker =
    new CircuitBreaker(system.scheduler,
      maxFailures = 10,
      callTimeout = 1.seconds,
      resetTimeout = 1.seconds).
      onOpen(println("circuit breaker opened!")).
      onClose(println("circuit breaker closed!")).
      onHalfOpen(println("circuit breaker half-open"))

  implicit val timeout: Timeout = Timeout(2.seconds)
  val db = system.actorOf(Props[FastSlowAkkademyDb])
  Await.result(db ? SetRequest("key", "value"), 2.seconds)

  (1 to 1000).foreach(_ => {
    Thread.sleep(50)
    val askFuture = breaker.withCircuitBreaker(db ? GetRequest("key"))
    askFuture.map(x => "got it: " + x).recover({
      case t => "error: " + t.toString
    }).foreach(x => println(x))
  })
}