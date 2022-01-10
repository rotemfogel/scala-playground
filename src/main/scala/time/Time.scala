package time

import scala.util.Try

object Time {
  def time[R](f: => R): Either[R, Throwable] = {
    val t0 = System.currentTimeMillis()
    val result = Try(f) // call-by-name
    val t1 = System.currentTimeMillis() - t0
    val method = Thread.currentThread.getStackTrace.last
    println(s"[$method] Elapsed time: $t1 ms") // side-effect :(
    if (result.isSuccess) Left(result.get) else Right(result.failed.get)
  }

  def main(args: Array[String]): Unit = {
    val increment: Int => Int = x => x + 1
    time(increment(1))
  }
}
