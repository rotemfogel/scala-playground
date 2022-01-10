package me.rotemfo.database.config

import com.typesafe.config.ConfigException.{Missing, WrongType}
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import me.rotemfo.database.model.Database._
import me.rotemfo.database.model.Environment
import me.rotemfo.database.model.Environment.Environment
import me.rotemfo.database.util.EnvUtils.envOrSysProp
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import java.sql.{Connection, DriverManager}
import java.util.Properties

/**
  * package: com.acmesecurity.common.config
  * file:    DatabaseConfig
  * created: 2017-04-24
  * author:  rotem
  */
sealed trait DBConfig {
  private lazy val config: Config = ConfigFactory.load()
  private lazy val acmeConfig: Config = config.getConfig("acme")
  protected lazy val databaseConfig: Config = acmeConfig.getConfig("database")

  private def getDBType(dbType: String, default: String): SupportedDatabase = {
    SupportedDatabase.withName(getOrDefault(dbType, default))
  }

  val operationalDbType: SupportedDatabase = getDBType("operationalDbType", "mysql")
  val reportingDbType: SupportedDatabase = getDBType("reportingDbType", "postgres")

  def getEnvironmentValue: String = getOrDefault("environment", Environment.DEVELOPMENT.toString, acmeConfig)

  def getEnvironment: Environment = {
    val envStr = getEnvironmentValue
    try {
      Environment.withName(envStr)
    } catch {
      case _: Throwable => Environment.DEVELOPMENT
    }
  }

  def getOrDefault(key: String, default: String): String = getOrDefault(key, default, config)

  def getOrDefault(key: String, default: Int): Int = getOrDefault(key, default, config)

  def getOrDefault(key: String, default: Boolean): Boolean = getOrDefault(key, default, config)

  def getOrDefault(key: String, default: String, config: Option[Config]): String = {
    if (config.isEmpty) default else getOrDefault(key, default, config.get)
  }

  def getOrDefault(key: String, default: String, config: Config): String = {
    val x = try {
      config.getString(key)
    } catch {
      case _@(_: Missing | _: WrongType) => default
    }
    x
  }

  def getOrDefault(key: String, default: Int, config: Config): Int = {
    val x = try {
      config.getInt(key)
    } catch {
      case _@(_: Missing | _: WrongType) => default
    }
    x
  }

  def getOrDefault(key: String, default: Boolean, config: Config): Boolean = {
    val x = try {
      config.getBoolean(key)
    } catch {
      case _@(_: Missing | _: WrongType) => default
    }
    x
  }
}

object DBConfig extends DBConfig

trait QueryableDBConfig extends DBConfig with DatabasePurpose {
  protected def databaseName: String

  protected lazy val mergedConfig: Config = databaseConfig.withFallback(buildDbConfig)
  private val specificDatabaseConfig = mergedConfig.getConfig(databaseName)
  val dbUrl: String = specificDatabaseConfig.getString("db.url")
  val dbUser: String = specificDatabaseConfig.getString("db.user")
  val dbPassword: String = specificDatabaseConfig.getString("db.password")
  val dbName: String = specificDatabaseConfig.getString("db.dbname")
  val dbSchema: String = if (specificDatabaseConfig.hasPath("db.dbschema")) specificDatabaseConfig.getString("db.dbschema") else ""
  val dbDriverClassName: String = specificDatabaseConfig.getString("db.driverClassName")
  val dbProperties: Config = specificDatabaseConfig.getConfig("db.properties")

  private def getProperties: Properties = {
    val properties = new Properties()
    properties.put("user", dbUser)
    properties.put("password", dbPassword)
    import scala.collection.JavaConverters._
    dbProperties.entrySet().asScala.foreach(item => {
      properties.put(item.getKey, item.getValue.unwrapped().toString)
    })
    properties
  }

  def getConnection: Connection = {
    DriverManager.getConnection(dbUrl, getProperties)
  }

  private def buildDbConfig: Config = {
    try {
      val dbProps: DbProperties = databaseName match {
        case "mysql" => MySQLProperties
        case "postgres" => PostgresProperties
      }

      val profile: String = envOrSysProp(purpose + "_JDBC_PROFILE", dbProps.profile)
      val host: String = envOrSysProp(purpose + "_HOST", dbProps.host)
      val port: Int = envOrSysProp(purpose + "_PORT", dbProps.port.toString).toInt
      val dbName: String = envOrSysProp(purpose + "_DB_NAME", dbProps.dbName)
      val dbSchema: String = envOrSysProp(purpose + "_DB_SCHEMA", dbProps.dbSchema)
      val driverClassName: String = envOrSysProp(purpose + "_DB_DRIVER", dbProps.driverClassName)
      val jdbcPrefix: String = envOrSysProp(purpose + "_JDBC_PREFIX", dbProps.jdbcPrefix)
      val maxConnections: Int = envOrSysProp(purpose + "_MAX_CONNECTIONS", "5").toInt

      val username: String = "scott"
      val password: String = "tiger"

      ConfigFactory.empty()
        .withValue(s"$databaseName.profile", ConfigValueFactory.fromAnyRef(profile))
        .withValue(s"$databaseName.db.url", ConfigValueFactory.fromAnyRef(s"jdbc:$jdbcPrefix://$host:$port/$dbName?currentSchema=$dbSchema"))
        .withValue(s"$databaseName.db.host", ConfigValueFactory.fromAnyRef(host))
        .withValue(s"$databaseName.db.dbname", ConfigValueFactory.fromAnyRef(dbName))
        .withValue(s"$databaseName.db.dbschema", ConfigValueFactory.fromAnyRef(dbSchema))
        .withValue(s"$databaseName.db.driverClassName", ConfigValueFactory.fromAnyRef(driverClassName))
        .withValue(s"$databaseName.db.poolName", ConfigValueFactory.fromAnyRef(s"${databaseName}_${host.substring(0, 5)}"))
        .withValue(s"$databaseName.db.connectionPool", ConfigValueFactory.fromAnyRef("HikariCP"))
        .withValue(s"$databaseName.db.numThreads", ConfigValueFactory.fromAnyRef(maxConnections))
        .withValue(s"$databaseName.db.maxConnections", ConfigValueFactory.fromAnyRef(maxConnections))
        .withValue(s"$databaseName.db.user", ConfigValueFactory.fromAnyRef(username))
        .withValue(s"$databaseName.db.password", ConfigValueFactory.fromAnyRef(password))
        .withValue(s"$databaseName.db.properties.AwsCredentialsProviderClass", ConfigValueFactory.fromAnyRef("com.acmesecurity.common.aws.credentials.acmeCredentialsWrapper"))
    } catch {
      case _: NoSuchElementException =>
        ConfigFactory.empty()
    }
  }
}

trait SlickDBConfig extends QueryableDBConfig {
  private val conf: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig(databaseName, mergedConfig)
  implicit val profile: JdbcProfile = conf.profile
  implicit val db: JdbcProfile#Backend#Database = conf.db

  def alterUserStatement: String

  def getDBConfigName: String = databaseName
}