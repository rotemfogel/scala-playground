package me.rotemfo.redbook.chapter1.part1

import java.util.UUID

class Cafe {
  def buyCoffee(cc: CreditCard): (Coffee, Charge) = {
    val cup: Coffee = new Coffee
    (cup, Charge(cc, cup.price))
  }

  def buyCoffees(cc: CreditCard, n: Int): (List[Coffee], Charge) = {
    val (coffees, charges) = List.fill(n)(buyCoffee(cc)).unzip
    (coffees, charges.reduce(_ combine _))
  }
}

object CafeApp extends App {
  val cafe = new Cafe
  val cc = CreditCard(UUID.randomUUID().toString)
  cc.deposit(100d)
  val (coffees, charge) = cafe.buyCoffees(cc, 4)
}