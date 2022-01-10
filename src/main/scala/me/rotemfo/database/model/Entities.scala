package me.rotemfo.database.model

import java.time.Instant

/**
  * project: scala-playground
  * package: me.rotemfo.database.model
  * file:    Entities
  * created: 2019-06-15
  * author:  rotem
  */
trait DBBaseEntity

trait DBEntity extends DBBaseEntity {
  val id: Option[Long]
  val createdAt: Option[Instant]
  val updatedBy: Option[String]
  val updatedAt: Option[Instant] = Some(Instant.now)
}

trait DBChildEntity {
  val parentId: Long
}

trait DBNamedEntity extends DBEntity {
  val name: String
}

trait DBNamedChildEntity extends DBNamedEntity with DBChildEntity
