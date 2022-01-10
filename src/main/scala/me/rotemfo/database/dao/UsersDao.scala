package me.rotemfo.database.dao

import me.rotemfo.database.dao.tables.ManagementTables.UsersTable
import me.rotemfo.database.model.UserEntity
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

/**
  * project: scala-playground
  * package: me.rotemfo.database.dao
  * file:    UsersDao
  * created: 2019-06-15
  * author:  rotem
  */
trait UsersTableDao extends SQLNamedEntityTableDaoImpl[UsersTable, UserEntity] {
  def findByParent(parentId: Long): Future[Seq[UserEntity]]

  def findByIdAndParent(id: Long, parentId: Long): Future[Option[UserEntity]]

  def deleteByIdAndParent(id: Long, parentId: Long): Future[Int]
}

class UsersTableDaoImpl()(implicit override val db: JdbcProfile#Backend#Database,
                          implicit override val profile: JdbcProfile) extends UsersTableDao {

  import profile.api._

  override def findByParent(parentId: Long): Future[Seq[UserEntity]] = {
    db.run(tableQ.filter(_.parentId === parentId).result)
  }

  /**
    * get element by id and parentId
    *
    * @param id       - element id
    * @param parentId - tenant id
    * @return Option of element
    */
  override def findByIdAndParent(id: Long, parentId: Long): Future[Option[UserEntity]] = {
    db.run(tableQ.filter(r => r.id === id && r.parentId === parentId).result.headOption)
  }

  override def deleteByIdAndParent(id: Long, parentId: Long): Future[Int] = {
    db.run(tableQ.filter(r => r.id === id && r.parentId === parentId).delete)
  }
}
