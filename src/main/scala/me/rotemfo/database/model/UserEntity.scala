package me.rotemfo.database.model

import java.time.Instant

/**
  * project: scala-playground
  * package: me.rotemfo.database
  * file:    UserEntity
  * created: 2019-06-15
  * author:  rotem
  */
case class UserEntity(id: Option[Long],
                      name: String,
                      parentId: Long,
                      override val createdAt: Option[Instant] = Some(Instant.now()),
                      override val updatedBy: Option[String] = None,
                      override val updatedAt: Option[Instant] = Some(Instant.now())) extends DBNamedEntity {
  require(name.nonEmpty, "username cannot be empty")

  @inline override def equals(obj: scala.Any): Boolean = {
    obj match {
      case other: UserEntity =>
        name.equals(other.name) &&
          parentId.equals(other.parentId)
      case _ => false
    }
  }
}
