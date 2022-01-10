package me.rotemfo.database.modules

import me.rotemfo.database.config.SlickDBConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * project: scala-playground
  * package: me.rotemfo.database
  * file:    DBHealthCheck
  * created: 2019-06-13
  * author:  rotem
  */
private sealed class DBHealthCheck()(implicit val db: JdbcProfile#Backend#Database,
                                     implicit val profile: JdbcProfile,
                                     implicit val ec: ExecutionContext) extends Profile with DbModule {

  import profile.api._

  def dbHealthCheck: Future[Boolean] = db.run(sql"SELECT 1".as[Int]).map(_.head match {
    case 1 => true
    case _ => false
  })
}

trait HealthCheckModuleImpl extends HealthCheckModule {
  this: SlickDBConfig =>

  import scala.concurrent.ExecutionContext.Implicits.global

  private val dbhc = new DBHealthCheck

  override def dbHealthCheck: Future[Boolean] = dbhc.dbHealthCheck
}