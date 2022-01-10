package me.rotemfo.database.tables

import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{Index, Rep, Tag}
import slick.sql.SqlProfile.ColumnOption.Nullable

import java.time.Instant

/**
  * project: scala-playground
  * package: me.rotemfo.database
  * file:    BaseTable
  * created: 2019-06-15
  * author:  rotem
  */
abstract class BaseTable[T](tag: Tag, tableName: String) extends Table[T](tag, tableName) {
  // convert Instant to Timestamp and vice-versa implicitly
  implicit val timeConverter: JdbcType[Instant] with BaseTypedType[Instant] = MappedColumnType.base[Instant, java.sql.Timestamp](
    i => java.sql.Timestamp.from(i),
    ts => Instant.ofEpochMilli(ts.getTime)
  )
  implicit val booleanConverter: JdbcType[Boolean] with BaseTypedType[Boolean] = MappedColumnType.base[Boolean, Int](
    b => if (b) 1 else 0,
    i => if (i == 1) true else false
  )
  implicit val longSetConverter: JdbcType[Set[Long]] with BaseTypedType[Set[Long]] = MappedColumnType.base[Set[Long], String](
    set => set.mkString(","),
    str => if (str == null || str.isEmpty) Set.empty[Long] else str.split(",").map(_.toLong).toSet
  )
  implicit val stringSetConverter: JdbcType[Set[String]] with BaseTypedType[Set[String]] = MappedColumnType.base[Set[String], String](
    set => set.mkString(","),
    str => if (str == null || str.isEmpty) Set.empty[String] else str.split(",").toSet
  )
}

abstract class EntityTable[T](tag: Tag, tableName: String) extends BaseTable[T](tag, tableName) {
  def id: Rep[Long] = column[Long]("id")

  def createdAt: Rep[Instant] = column[Instant]("created_at", Nullable, O.SqlType("timestamp default now()"))

  def updatedBy: Rep[String] = column[String]("updated_by")

  def updatedAt: Rep[Instant] = column[Instant]("updated_at", Nullable, O.SqlType("timestamp default now()"))
}

abstract class NamedEntityTable[T](tag: Tag, tableName: String) extends EntityTable[T](tag, tableName) {
  def name: Rep[String] = column[String]("name", O.SqlType("VARCHAR(40)"))

  // indices
  def nameIx: Index = index("name_uix", name, unique = true)
}

abstract class ChildEntityTable[T](tag: Tag, tableName: String) extends EntityTable[T](tag, tableName) {
  def parentId: Rep[Long] = column[Long]("parentid")
}

abstract class NamedChildEntityTable[T](tag: Tag, tableName: String) extends NamedEntityTable[T](tag, tableName) {
  def parentId: Rep[Long] = column[Long]("parentid")
}