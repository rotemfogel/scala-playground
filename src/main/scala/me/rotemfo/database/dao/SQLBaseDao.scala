package me.rotemfo.database.dao

import me.rotemfo.database.tables.BaseTable
import slick.lifted.CanBeQueryCondition

import scala.concurrent.Future

/**
  * project: acmesecurity
  * package: me.rotemfo.database.dao
  * file:    SQLBaseDao
  * created: 2018-06-07
  * author:  rotem
  */
trait SQLBaseDao[T <: BaseTable[A], A] {

  /**
    * fetch entities by filter
    *
    * @param f - the query condition
    * @tparam C - the query filter
    * @return list of entities
    */
  def findByFilter[C: CanBeQueryCondition](f: T => C): Future[Seq[A]]
}