package me.rotemfo.monads

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object MonadsForBeginners {
  case class SafeValue[+T](private val internalValue: T) { // unit/pure
    def get: T = synchronized {
      internalValue
    }

    def flatMap[S](f: T => SafeValue[S]): SafeValue[S] = { // bind/flatMap
      synchronized {
        f(internalValue)
      }
    }
  }

  def getSafeValue[T](value: T): SafeValue[T] = SafeValue(value)

  val safeString: SafeValue[String] = getSafeValue("Scala is Awesome !")

  // extract
  val value: String = safeString.get
  // transform
  val upperValue: String = value.toUpperCase
  // wrap
  val safeUpperValue: SafeValue[String] = SafeValue(upperValue)

  // E T W (extract/transform/wrap)
  //  ||
  //  ||
  // \  /
  //  \/
  safeString.flatMap(v => SafeValue(v.toUpperCase))

  // example 1:
  case class Person(firstName: String, lastName: String) {
    require(firstName != null && lastName != null)
  }

  def getPerson(firstName: String, lastName: String): Option[Person] =
    for {
      fName <- Option(firstName)
      lName <- Option(lastName)
    } yield Person(fName, lName)

  // example 2:
  // @formatter:off
  case class User(id: Int)
  case class Product(sku: String, price: Double)
  def getUser(url: String): Future[User] = {
    println(s"fetching user for url $url")
    Future(User(1))
  }
  def getLastOrder(userId: Int): Future[Product] = {
    println(s"fetching product for userId $userId")
    Future(Product("24242", 29.99))
  }
  // @formatter:on

  // example 3:
  val numbers = List(1, 2, 3)
  val chars = List('a', 'b', 'c')

  def twoConsecutiveNumbers(x: Int): List[Int] = List(x, x + 1)

  private def onComplete(f: Future[Double]): Unit = {
    f.onComplete {
      case Success(value) => println(value)
      case Failure(e) => e.printStackTrace()
    }
  }


  def main(args: Array[String]): Unit = {
    println(getPerson(null, null))
    println(getPerson("a", null))
    println(getPerson(null, "b"))
    println(getPerson("a", "b"))

    val url = "https://my.store/users/1"

    val getLastPrice: Future[Double] = getUser(url)
      .flatMap(u => getLastOrder(u.id))
      .map(_.price * 1.18)

    val lastPrice = for {
      u <- getUser(url)
      p <- getLastOrder(u.id)
    } yield p.price * 1.18

    onComplete(getLastPrice)
    onComplete(lastPrice)

    // construct all possible combination of numbers and chars
    val list = numbers.flatMap(n => chars.map(c => (n, c)))
    println(s"flatMap:\t\t\t${list.mkString(",")}")

    val list2 = for {
      n <- numbers
      c <- chars
    } yield (n, c)
    println(s"for comprehension:\t${list2.mkString(",")}")

    Thread.sleep(2000)

    // Property 1:
    // Monad(x).flatMap(f) == f(x)
    println(twoConsecutiveNumbers(3))
    println(List(3).flatMap(twoConsecutiveNumbers))

    // Property 2:
    // Right Identity
    // Monad(x).flatMap(x => Monad(x)) == Monad(x)
    // ==> USELESS
    val same = List(1, 2, 3).flatMap(List(_)) == List(1, 2, 3)
    println(same)

    // Property 3:
    // associativity
    // Monad(x).flatMap(f).flatMap(g) == Monad(x).flatMap(x => f(x).flatMap(g))
    // @formatter:off
    def increment(x: Int): List[Int] = List(x, x + 1)
    def double(x: Int): List[Int] = List(x, 2 * x)
    // @formatter:on

    val a1 = numbers.flatMap(increment).flatMap(double)
    println(a1)
    val a2 = numbers.flatMap(x => increment(x).flatMap(double))
    println(a2)
    assert(a1 == a2)
  }

}
