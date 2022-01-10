package me.rotemfo.redbook.chapter1.part1

import com.google.common.util.concurrent.AtomicDouble

case class CreditCard(ccNumber: String) {
  private val funds: AtomicDouble = new AtomicDouble(0)

  def deposit(amount: Double): Double = {
    funds.getAndAdd(amount)
  }

  def charge(price: Double): Unit = {
    val newFund = funds.get - price
    if (newFund > 0) {
      funds.set(newFund)
    }
    else throw new Exception("Insufficient Funds")
  }

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[CreditCard]) false
    else obj.asInstanceOf[CreditCard].ccNumber.equals(ccNumber)
  }

  override def toString: String =
    s"""
       |ccNumber=$ccNumber
       |funds=${funds.get}""".stripMargin
}
