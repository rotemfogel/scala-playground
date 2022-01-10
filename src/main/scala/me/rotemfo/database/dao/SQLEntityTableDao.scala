package me.rotemfo.database.dao

import me.rotemfo.database.model.{DBChildEntity, DBEntity, DBNamedChildEntity, DBNamedEntity}
import me.rotemfo.database.modules.{DbModule, Profile}
import me.rotemfo.database.tables.{ChildEntityTable, EntityTable, NamedChildEntityTable, NamedEntityTable}
import slick.jdbc.JdbcProfile
import slick.lifted.{CanBeQueryCondition, TableQuery}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * project: scala-playground
  * package: me.rotemfo.database.dao
  * file:    SQLEntityTableDao
  * created: 2019-06-15
  * author:  rotem
  */
sealed trait SQLEntityTableDao[T <: EntityTable[A], A <: DBEntity] extends SQLBaseDao[T, A] with BaseEntityDao[Long, A] {
  def deleteByFilter[C: CanBeQueryCondition](f: T => C): Future[Int]
}

sealed trait SQLNamedEntityTableDao[T <: NamedEntityTable[A], A <: DBNamedEntity] extends BaseNamedEntityDao[Long, A]

sealed trait SQLNamedChildEntityTableDao[T <: NamedChildEntityTable[A], A <: DBChildEntity] extends BaseEntityDao[Long, A] {
  def findByIdAndParent(id: Long, parentId: Long): Future[Option[A]]

  def findByParent(parentId: Long): Future[Seq[A]]

  def deleteByIdAndParent(id: Long, parentId: Long): Future[Int]
}

sealed trait SQLChildEntityTableDao[T <: ChildEntityTable[A], A <: DBEntity] extends BaseEntityDao[Long, A] {
  def findByParent(parentId: Long): Future[Seq[A]]
}

abstract class SQLEntityTableDaoImpl[T <: EntityTable[A], A <: DBEntity]()(implicit val tableQ: TableQuery[T],
                                                                           implicit val db: JdbcProfile#Backend#Database,
                                                                           implicit val profile: JdbcProfile) extends SQLEntityTableDao[T, A] with Profile with DbModule {

  import profile.api._

  override def createTable(): Future[Unit] = db.run(DBIO.seq(tableQ.schema.create))

  /**
    * get all entity types
    *
    * @param limit - limit returned list size (default unlimited)
    * @return [[A]]
    */
  override def all(limit: Int): Future[Seq[A]] = db.run(tableQ.result)

  /**
    * get a single entity by key
    *
    * @param id - entity key
    * @return [[A]]
    */
  override def findById(id: Long): Future[Option[A]] = db.run(tableQ.filter(_.id === id).result.headOption)

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
  override def save(os: Seq[A]): Future[Seq[Long]] = db.run(DBIO.sequence(os.map(o => tableQ += o))).map(_.seq.map(_.toLong))

  /**
    * update existing entity
    *
    * @param o - the entity
    * @return
    */
  override def update(o: A): Future[Int] = db.run(tableQ.filter(_.id === o.id).update(o))

  /**
    * update many entities
    *
    * @param os - list of entities
    * @return
    */
  override def update(os: Seq[A]): Future[Seq[Int]] = db.run(DBIO.sequence(os.map(r => tableQ.filter(_.id === r.id).update(r)))).map(_.seq)

  /**
    * delete existing entity
    *
    * @param id - the entity
    * @return Future[Int]
    */
  override def delete(id: Long): Future[Int] = db.run(tableQ.filter(_.id === id).delete)

  /**
    * delete existing entity
    *
    * @param ids - the entity
    * @return [[A]]
    */
  override def delete(ids: Seq[Long]): Future[Int] = db.run(tableQ.filter(_.id.inSet(ids)).delete)

  /**
    * delete entity by filter
    *
    * @param f - the entity
    * @tparam C - the query condition
    * @return
    */
  override def deleteByFilter[C: CanBeQueryCondition](f: T => C): Future[Int] = {
    db.run(tableQ.withFilter(f).delete)
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

abstract class SQLNamedEntityTableDaoImpl[T <: NamedEntityTable[A], A <: DBNamedEntity]()(implicit override val tableQ: TableQuery[T],
                                                                                          implicit override val db: JdbcProfile#Backend#Database,
                                                                                          implicit override val profile: JdbcProfile)
  extends SQLEntityTableDaoImpl[T, A]
    with SQLNamedEntityTableDao[T, A] {

  import profile.api._

  /**
    * get element by name
    *
    * @param name - the element name
    * @return Option of element
    */
  override def findByName(name: String): Future[Option[A]] = findByFilter(_.name === name).map(_.headOption)
}

abstract class SQLNamedChildEntityTableDaoImpl[T <: NamedChildEntityTable[A], A <: DBNamedChildEntity]()(implicit override val tableQ: TableQuery[T],
                                                                                                         implicit override val db: JdbcProfile#Backend#Database,
                                                                                                         implicit override val profile: JdbcProfile)
  extends SQLNamedEntityTableDaoImpl[T, A]
    with SQLNamedChildEntityTableDao[T, A] {

  /**
    * get element by id and parentId
    *
    * @param id       - element id
    * @param parentId - tenant id
    * @return Option of element
    */
  override def findByIdAndParent(id: Long, parentId: Long): Future[Option[A]]

  override def deleteByIdAndParent(id: Long, parentId: Long): Future[Int]

  override def findByParent(parentId: Long): Future[Seq[A]]
}

class SQLChildEntityTableDaoImpl[T <: ChildEntityTable[A], A <: DBEntity]()(implicit override val tableQ: TableQuery[T],
                                                                            implicit override val db: JdbcProfile#Backend#Database,
                                                                            implicit override val profile: JdbcProfile)
  extends SQLEntityTableDaoImpl[T, A]
    with SQLChildEntityTableDao[T, A] {

  /**
    * get element by id and parent id
    *
    * @param parentId - parent id
    * @return Option of element
    */
  override def findByParent(parentId: Long): Future[Seq[A]] = Future.failed(new NotImplementedError())
}