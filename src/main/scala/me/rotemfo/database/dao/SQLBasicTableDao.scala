package me.rotemfo.database.dao

import me.rotemfo.database.model.DBBaseEntity
import me.rotemfo.database.modules.{DbModule, Profile}
import me.rotemfo.database.tables.BaseTable
import slick.jdbc.JdbcProfile
import slick.lifted.{CanBeQueryCondition, TableQuery}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * project: scala-playground
  * package: me.rotemfo.database.dao
  * file:    SQLBasicTableDao
  * created: 2019-06-15
  * author:  rotem
  */
trait SQLBasicTableDao[T <: BaseTable[A], A <: DBBaseEntity] extends SQLBaseDao[T, A] with BaseDao[A] {
}

abstract class SQLBasicTableDaoImpl[T <: BaseTable[A], A <: DBBaseEntity]()(implicit val tableQ: TableQuery[T],
                                                                            implicit val db: JdbcProfile#Backend#Database,
                                                                            implicit val profile: JdbcProfile) extends SQLBasicTableDao[T, A] with Profile with DbModule {

  import profile.api._

  override def createTable(): Future[Unit] = db.run(DBIO.seq(tableQ.schema.create))

  /**
    * get all entity types
    *
    * @param limit - limit returned list size (default unlimited)
    * @return [[A]]
    */
  def all(limit: Int): Future[Seq[A]] = if (limit < 1) db.run(tableQ.result) else db.run(tableQ.take(limit).result)

  /**
    * insert a new entity
    *
    * @param o - the entity
    * @return [[A]]
    */
  override def save(o: A): Future[Option[Long]] = save(Seq(o)).map(_.headOption)

  /**
    * insert a new entity
    *
    * @param os - list of entities
    * @return [[A]]
    */
  override def save(os: Seq[A]): Future[Seq[Long]] = {
    val q = DBIO.sequence(os.map(o => tableQ += o))
    db.run(q).map(_.seq.map(_.toLong))
  }


  /**
    * fetch entities by filter
    *
    * @param f - the query condition
    * @tparam C - the query filter
    * @return list of entities
    */
  override def findByFilter[C: CanBeQueryCondition](f: T => C): Future[Seq[A]] = db.run(tableQ.withFilter(f).result)
}
