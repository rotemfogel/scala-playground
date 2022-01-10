package me.rotemfo.database.dao.tables

import me.rotemfo.database.model.UserEntity
import me.rotemfo.database.tables.NamedEntityTable
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{ProvenShape, Rep => _, TableQuery => _, Tag => _}

/**
  * project: scala-playground
  * package: me.rotemfo.database.dao.tables
  * file:    UsersTable
  * created: 2019-06-15
  * author:  rotem
  */

object ManagementTables {

  class UsersTable(tag: Tag) extends NamedEntityTable[UserEntity](tag, "users") {
    def parentId: Rep[Long] = column[Long]("parentId")

    override def * : ProvenShape[UserEntity] = (id.?, name, parentId, createdAt.?, updatedBy.?, updatedAt.?) <> (UserEntity.tupled, UserEntity.unapply)
  }

  implicit val usersTableQ: TableQuery[UsersTable] = TableQuery[UsersTable]
}
