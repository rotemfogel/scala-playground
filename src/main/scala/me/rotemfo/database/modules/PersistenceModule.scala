package me.rotemfo.database.modules

import slick.jdbc.JdbcProfile

import scala.concurrent.Future

/**
  * package: com.acmesecurity.common.db.persistence.dao.util.sql
  * file:    PersistenceModule
  * created: 2017-05-16
  * author:  rotem
  */

trait Profile {
  val profile: JdbcProfile
}

trait DbModule extends Profile {
  val db: JdbcProfile#Backend#Database
}

trait HealthCheckModule {
  def dbHealthCheck: Future[Boolean]
}