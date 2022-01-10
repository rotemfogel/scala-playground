package me.rotemfo.database.config

/**
  * project: scala-playground
  * package: me.rotemfo.database
  * file:    MySQLConfig
  * created: 2019-06-13
  * author:  rotem
  */
trait MySQLConfig extends SlickDBConfig {
  override protected def databaseName: String = "mysql"

  override def alterUserStatement: String = "SET PASSWORD FOR %s = '%s'"
}
