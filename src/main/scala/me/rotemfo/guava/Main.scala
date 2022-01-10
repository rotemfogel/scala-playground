package me.rotemfo.guava

import com.google.common.cache.{CacheBuilder, CacheLoader}
import com.google.common.util.concurrent.UncheckedExecutionException

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

/**
  * project: scala-playground
  * package: me.rotemfo.guava
  * file:    Main
  * created: 2019-09-01
  * author:  rotem
  */
object Main extends App {

  private final case class MaxRetriesException(private val message: String = "", private val cause: Throwable = None.orNull)
    extends Exception(message, cause)

  private val fetchLimit: ConcurrentHashMap[String, Int] = new ConcurrentHashMap[String, Int]()

  @throws[IllegalStateException]
  private def fetchCounter(key: String): Int = {
    fetchLimit.compute(key, (_: String, currentCount: Int) => {
      if (currentCount == 0) 1
      else if (currentCount > 3) throw MaxRetriesException(s"maximum attempts reached for geo profile key $key")
      else currentCount + 1
    })
  }

  val cache = CacheBuilder.newBuilder
    .maximumSize(1000)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build[String, Option[String]](
      new CacheLoader[String, Option[String]]() {
        override def load(key: String): Option[String] = {
          fetchCounter(key)
          Some("s")
        }
      }
    )

  try {
    println(cache.get("1"))
  } catch {
    case e: UncheckedExecutionException => if (e.getCause.isInstanceOf[MaxRetriesException]) throw e
  }
  try {
    println(cache.get("1"))
  } catch {
    case e: UncheckedExecutionException => if (e.getCause.isInstanceOf[MaxRetriesException]) throw e
  }
  try {
    println(cache.get("1"))
  } catch {
    case e: UncheckedExecutionException => if (e.getCause.isInstanceOf[MaxRetriesException]) throw e
  }
  try {
    println(cache.get("1"))
  } catch {
    case e: UncheckedExecutionException => if (e.getCause.isInstanceOf[MaxRetriesException]) throw e
  }
  try {
    println(cache.get("1"))
  } catch {
    case e: UncheckedExecutionException => if (e.getCause.isInstanceOf[MaxRetriesException]) throw e
  }
  try {
    println(cache.get("1"))
  } catch {
    case e: UncheckedExecutionException => if (e.getCause.isInstanceOf[MaxRetriesException]) throw e
  }
  try {
    println(cache.get("1"))
  } catch {
    case e: UncheckedExecutionException => if (e.getCause.isInstanceOf[MaxRetriesException]) throw e
  }
}
