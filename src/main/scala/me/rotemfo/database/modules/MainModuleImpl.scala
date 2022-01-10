package me.rotemfo.database.modules

import me.rotemfo.database.config.SlickDBConfig
import me.rotemfo.database.dao.UsersTableDaoImpl

/**
  * project: scala-playground
  * package: me.rotemfo.database
  * file:    MainModule
  * created: 2019-06-15
  * author:  rotem
  */

trait MainModuleImpl {
  this: SlickDBConfig =>
  val usersDao: UsersTableDaoImpl = new UsersTableDaoImpl
}
