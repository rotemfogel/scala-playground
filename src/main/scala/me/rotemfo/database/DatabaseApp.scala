package me.rotemfo.database

import me.rotemfo.database.config.MySQLConfig
import me.rotemfo.database.model.Database.Operational
import me.rotemfo.database.model.UserEntity
import me.rotemfo.database.modules.{HealthCheckModuleImpl, MainModuleImpl}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * project: scala-playground
  * package: me.rotemfo.database
  * file:    DatabaseApp
  * created: 2019-06-13
  * author:  rotem
  */
object DatabaseApp extends App {
  val mysql = new MySQLConfig with Operational with MainModuleImpl with HealthCheckModuleImpl
  val isHealthy = Await.result[Boolean](mysql.dbHealthCheck, 1.seconds)
  println(isHealthy)
  Await.result[Unit](mysql.usersDao.createTable(), 2.seconds)
  val users = (1 to 10).map(n => {
    UserEntity(id = None, name = s"user#$n", parentId = 1L)
  })
  val saved: Seq[Long] = Await.result[Seq[Long]](mysql.usersDao.save(users), 2.seconds)
  assert(saved.size == 10)
  val usersFromDB: Seq[UserEntity] = Await.result[Seq[UserEntity]](mysql.usersDao.all(), 2.seconds)
  usersFromDB.foreach(println)
  assert(usersFromDB.equals(users))
  mysql.db.close()
}
