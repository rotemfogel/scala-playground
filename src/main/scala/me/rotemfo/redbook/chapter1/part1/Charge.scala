package me.rotemfo.redbook.chapter1.part1

case class Charge(cc: CreditCard, amount: Double) {
  def combine(other: Charge): Charge = {
    if (cc == other.cc) Charge(cc, amount + other.amount)
    else throw new Exception("Can't combine charges to different Credit Cards")
  }
}
