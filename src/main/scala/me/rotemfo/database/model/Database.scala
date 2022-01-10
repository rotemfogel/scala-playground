package me.rotemfo.database.model

/**
  * project: acmesecurity
  * package: com.acmesecurity.common.db.enums
  * file:    Operational
  * created: 2018-06-07
  * author:  rotem
  */
object Database {
  private final val acme: String = "acme"

  sealed abstract class DbProperties(val profile: String,
                                     val host: String = "localhost",
                                     val port: Int,
                                     val dbName: String = acme,
                                     val dbSchema: String = acme,
                                     val jdbcPrefix: String,
                                     val driverClassName: String,
                                     val connectionTimeout: Int)

  case object MySQLProperties extends DbProperties(
    profile = "slick.jdbc.MySQLProfile$",
    port = 3306,
    jdbcPrefix = "mariadb",
    driverClassName = "org.mariadb.jdbc.Driver",
    connectionTimeout = 1000
  )

  case object PostgresProperties extends DbProperties(
    profile = "slick.jdbc.PostgresProfile$",
    port = 5439,
    jdbcPrefix = "postgresql",
    driverClassName = "org.postgresql.Driver",
    connectionTimeout = 10000
  )

  sealed abstract class SupportedDatabase(val name: String)

  object SupportedDatabase {

    case object MYSQL extends SupportedDatabase("mysql")

    case object POSTGRES extends SupportedDatabase("postgres")

    case object ATHENA extends SupportedDatabase("athena")

    case object HIVE extends SupportedDatabase("hive")

    case object PRESTO extends SupportedDatabase("presto")

    def withName(name: String): SupportedDatabase = {
      name.toLowerCase() match {
        case "mysql" => MYSQL
        case "postgres" => POSTGRES
        case "athena" => ATHENA
        case "hive" => HIVE
        case "presto" => PRESTO
      }
    }
  }

  trait DatabasePurpose {
    def purpose: String

    val OPERATIONAL: String = "operational"
    val REPORTING: String = "reporting"
    val OTHER: String = "other"
  }

  trait Operational extends DatabasePurpose {
    override def purpose: String = OPERATIONAL
  }

  trait Reporting extends DatabasePurpose {
    override def purpose: String = REPORTING
  }

  trait Other extends DatabasePurpose {
    override def purpose: String = OTHER
  }
}
