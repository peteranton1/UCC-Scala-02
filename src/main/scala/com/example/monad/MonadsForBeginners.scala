package com.example.monad

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

object MonadsForBeginners {

  // "constructor" = pure or unit
  case class SafeValue[+T](private val internalValue: T) {
    def get: T = synchronized {
      // does something interesting
      internalValue
    }

    // bind or flatmap
    def flatMap[S](transformer: T => SafeValue[S]): SafeValue[S] =
      synchronized {
        transformer(internalValue)
      }
  }

  // "external" API
  def gimmeSafeValue[T](value: T): SafeValue[T] = SafeValue(value)

  val safeString: SafeValue[String] =
    gimmeSafeValue(("Scala is awesome"))

  // extract
  val string = safeString.get
  // transform
  val upperString = string.toUpperCase()
  // wrap
  val upperSafeString = SafeValue(upperString)
  // ETW

  // compressed
  val upperSafeString2 = safeString.flatMap(
    s => SafeValue(s.toUpperCase())
  )

  // Examples

  // example 1 : census
  case class Person(firstName: String, lastName: String) {
    assert(firstName != null && lastName != null)
  }

  def getPerson(firstName: String, lastName: String): Person =
    if (firstName != null) {
      if(lastName != null) {
        Person(firstName, lastName)
      } else {
        null
      }
    } else {
      null
    }

  def getPersonBetter(firstName: String, lastName: String):
  Option[Person] =
    Option(firstName).flatMap(fName =>
       Option(lastName).flatMap( lName =>
         Option(Person(fName, lName))
       )
    )

  def getPersonFor(firstName: String, lastName: String):
  Option[Person] = for {
    fName <- Option(firstName)
    lName <- Option(lastName)
  } yield Person(fName, lName)

  // Example 2: asynchronous fetches

  case class User(id: String)
  case class Product(sku: String, price: Double)

  def getUser(url: String): Future[User] = Future {
    User("Bob") // sample implementation
  }

  def getLastOrder(userId: String): Future[Product] = Future {
    Product("123-456", 99.99) // sample
  }

  val bobsUrl = "my.store.com/users/bob"

  // ETC
  getUser(bobsUrl).onComplete {
    case Success(User(id)) =>
      val lastOrder = getLastOrder(id)
        .onComplete( {
          case Success(Product(sku, p)) =>
            val vatIncludedPrice = p * 1.19
            // pass it on - send user an email
        })
  }

  val vatIncludedPrice: Future[Double] = getUser(bobsUrl)
    .flatMap(user => getLastOrder(user.id))
    .map(_.price * 1.19)

  val vatIncludedFor: Future[Double] = for {
    user <- getUser(bobsUrl)
    product <- getLastOrder(user.id)
  } yield product.price * 1.19

  // Example 3: double-for loops

  val numbers = List(1,2,3)
  val chars = List('a','b','c')
  // flatMaps
  val checkerboard: List[(Int, Char)] = numbers.flatMap(
    number => chars.map(
      char => (number, char)
    )
  )
  val checkerboard2: List[(Int, Char)] = for {
    number <- numbers
    char <- chars
  } yield (number, char)

  // Properties

  // prop 1
  def twoConsecutive(x: Int) = List(x, x + 1)
  twoConsecutive(3)
  List(3).flatMap(twoConsecutive) // = List(3,4)
  // Monad(x).flatMap(f) == f(x)

  // prop 2
  List(1,2,3).flatMap(x => List(x)) // = Same as original List
  // Monad(v).flatMap(x => Monad(x)) // USELESS, returns Monad(v)

  // prop 3 - associativity ETW-ETW
  val incrementer = (x: Int) => List(x, x + 1)
  val doubler = (x: Int) => List(x, 2 * x)

  def main(args: Array[String]): Unit = {
    println(numbers.flatMap(incrementer).flatMap(doubler))
    // List(1, 2, 2, 4,  2, 4, 3, 6,  3, 6, 4, 8)
    // Monad(v).flatMap(f).flatMap(g)
    // ==
    // Monad(v).flatMap(x => f(x).flatMap(g)
  }

}
























