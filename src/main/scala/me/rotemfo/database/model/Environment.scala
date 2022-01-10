package me.rotemfo.database.model

/**
  * project: scala-playground
  * package: me.rotemfo.database
  * file:    Environment
  * created: 2019-06-13
  * author:  rotem
  */
object Environment extends Enumeration {
  type Environment = Value
  // DON'T REMOVE / CHANGE ENUMERATION VALUES / NUMBERING
  val DEVELOPMENT: Environment.Value = Value("development")
  val STAGING: Environment.Value = Value("staging")
  val INTEGRATION: Environment.Value = Value("integration")
  val PRODUCTION: Environment.Value = Value("production")
}