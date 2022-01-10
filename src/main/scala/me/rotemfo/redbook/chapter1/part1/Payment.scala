package me.rotemfo.redbook.chapter1.part1

object Payment {
  def charge(cc: CreditCard, price: Double): Unit = cc.charge(price)

  def coalesce(charges: List[Charge]): List[Charge] =
    charges.groupBy(_.cc).values.map(_.reduce(_ combine _)).toList

}
