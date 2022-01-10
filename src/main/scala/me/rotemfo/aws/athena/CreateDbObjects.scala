package me.rotemfo.aws.athena

import com.amazonaws.regions.Regions
import com.amazonaws.services.athena.model._
import com.amazonaws.services.athena.{AmazonAthena, AmazonAthenaClientBuilder}
import com.amazonaws.services.glue.model._
import com.amazonaws.services.glue.{AWSGlue, AWSGlueClientBuilder}
import org.slf4j.{Logger, LoggerFactory}

import java.io.{FileNotFoundException, PrintWriter}
import scala.collection.mutable
import scala.io.Source

object CreateDbObjects {
  private final val logger: Logger = LoggerFactory.getLogger(getClass)

  private final val BUCKET: String = "seekingalpha-data"
  private final val MARIADB: String = "mariadb"
  // private final val ZUORA: String = "zuora"
  private final val CREATE_VIEW: String = s"CREATE OR REPLACE VIEW %s AS SELECT global_hash_key,commit,database,ts,type,xid,xoffset,data,old,tbl,date_,hour FROM (SELECT row_number() OVER (PARTITION BY tbl,global_hash_key ORDER BY ts DESC,is_deleted DESC,is_updated DESC,is_inserted DESC) row_num,* FROM (SELECT (CASE WHEN (type = 'delete') THEN true ELSE false END) is_deleted,(CASE WHEN (type = 'insert') THEN true ELSE false END) is_inserted,(CASE WHEN (type = 'update') THEN true ELSE false END) is_updated,* FROM (SELECT * FROM test_dbl.maria_dim_incr_latest WHERE (tbl = '%s') UNION ALL SELECT * FROM test_dbl.maria_dim_latest WHERE (tbl = '%s')) a) b) c WHERE ((row_num = 1) AND (is_deleted = false))"
  private final val GET_TABLES: String = s"SELECT table_name FROM information_schema.tables WHERE table_schema='%s' ORDER BY table_name"
  private final val VIEWS_FILE: String = "%s_views.lst"
  private final val TABLES_FILE: String = "%s_tables.lst"

  private def filterLatest(name: String): Boolean = name.endsWith("_latest")

  private def filterNotLatest(name: String): Boolean = !name.endsWith("_latest")

  def defaultFilter(name: String): Boolean = !name.equals("table_name")

  private final val athena: AmazonAthena = AmazonAthenaClientBuilder.standard().withRegion(Regions.US_WEST_2).build()

  def main(args: Array[String]): Unit = {
    createViews(MARIADB)
    createTables(MARIADB)
    // createTables(ZUORA)
  }

  private def execute(db: String, query: String): String = {
    val queryExecutionContext: QueryExecutionContext = new QueryExecutionContext().withDatabase(db)
    val resultConfiguration: ResultConfiguration = new ResultConfiguration().withOutputLocation("s3://aws-athena-query-results-744522205193-us-west-2/airflow/")
    val startQueryExecutionRequest: StartQueryExecutionRequest = new StartQueryExecutionRequest()
      .withQueryExecutionContext(queryExecutionContext)
      .withResultConfiguration(resultConfiguration)
      .withQueryString(query)
    val startQueryExecutionResult: StartQueryExecutionResult = athena.startQueryExecution(startQueryExecutionRequest)
    startQueryExecutionResult.getQueryExecutionId
  }

  private def checkQuery(queryExecutionId: String): Unit = {
    val getQueryExecutionRequest = new GetQueryExecutionRequest().withQueryExecutionId(queryExecutionId)
    var getQueryExecutionResponse: GetQueryExecutionResult = null
    var isQueryStillRunning: Boolean = true
    while (isQueryStillRunning) {
      getQueryExecutionResponse = athena.getQueryExecution(getQueryExecutionRequest)
      val queryState = getQueryExecutionResponse.getQueryExecution.getStatus.getState
      if (queryState == QueryExecutionState.FAILED.toString) throw new RuntimeException(s"Query Failed to run with Error Message: ${getQueryExecutionResponse.getQueryExecution.getStatus.getStateChangeReason}")
      else if (queryState == QueryExecutionState.CANCELLED.toString) throw new RuntimeException("Query was cancelled.")
      else if (queryState == QueryExecutionState.SUCCEEDED.toString) isQueryStillRunning = false
      else {
        Thread.sleep(5000)
      }
      logger.debug("Current Status is: " + queryState)
    }
  }

  def createTables(_db: String, fn: String => Boolean = filterNotLatest): Unit = {
    logger.info("obtaining list of tables...")
    val dbFile = TABLES_FILE.format(_db)
    val start = System.currentTimeMillis()
    val tables = {
      var lines: List[String] = List()
      try {
        val source = Source.fromFile(dbFile, "utf-8")
        lines = source.getLines().toList
        source.close()
      } catch {
        case _: FileNotFoundException =>
      }
      if (lines.nonEmpty) {
        lines
      }
      else {
        val queryExecutionId: String = execute(_db, GET_TABLES.format(_db))
        checkQuery(queryExecutionId)
        val tables: List[String] = getRows(queryExecutionId).filter(fn)
        new PrintWriter(dbFile) {
          write(tables.mkString("\n"))
          close()
        }
        tables
      }
    }
    val end = System.currentTimeMillis() - start
    logger.info(s"retrieved ${tables.size} tables in $end ms")
    val db = s"test_${_db}"
    val glue: AWSGlue = AWSGlueClientBuilder.standard().withRegion(Regions.US_WEST_2).build()
    tables.foreach(table => {
      try {
        val getTableResult: GetTableResult = glue.getTable(new GetTableRequest().withDatabaseName(db).withName(table))
        if (getTableResult.getTable != null) {
          val sourceTable: Table = getTableResult.getTable
          try {
            glue.getTable(new GetTableRequest().withDatabaseName(db).withName(table))
            glue.deleteTable(new DeleteTableRequest().withDatabaseName(db).withName(table))
            logger.info(s"table $db.$table dropped")
          } catch {
            case _: EntityNotFoundException =>
          }
          val location = sourceTable.getStorageDescriptor.getLocation.replaceAll(_db, db).replaceAll(BUCKET, s"$BUCKET-dev")
          val storageDescriptor = sourceTable.getStorageDescriptor
          storageDescriptor.setLocation(location)
          glue.createTable(new CreateTableRequest().withDatabaseName(db).withTableInput(
            new TableInput()
              .withDescription(sourceTable.getDescription)
              .withLastAccessTime(sourceTable.getLastAccessTime)
              .withLastAnalyzedTime(sourceTable.getLastAccessTime)
              .withName(sourceTable.getName)
              .withOwner(sourceTable.getOwner)
              .withParameters(sourceTable.getParameters)
              .withPartitionKeys(sourceTable.getPartitionKeys)
              .withRetention(sourceTable.getRetention)
              .withStorageDescriptor(storageDescriptor)
              .withTableType(sourceTable.getTableType)
          ))
        }
        logger.info(s"table $db.$table created")
      } catch {
        case e: EntityNotFoundException =>
          logger.info(s"table $table registered in META_STORE, but does not exist")
      }
    })
  }

  def createViews(_db: String, fn: String => Boolean = filterLatest): Unit = {
    logger.info("obtaining list of views...")
    val dbFile = VIEWS_FILE.format(_db)
    val start = System.currentTimeMillis()
    val views = {
      var lines: List[String] = List()
      try {
        val source = Source.fromFile(dbFile, "utf-8")
        lines = source.getLines().toList
        source.close()
      } catch {
        case _: FileNotFoundException =>
      }
      if (lines.nonEmpty) {
        lines
      }
      else {
        val queryExecutionId: String = execute(_db, GET_TABLES.format(_db))
        checkQuery(queryExecutionId)
        val tables: List[String] = getRows(queryExecutionId).filter(fn)
        new PrintWriter(dbFile) {
          write(tables.mkString("\n"))
          close()
        }
        tables
      }
    }
    val end = System.currentTimeMillis() - start
    logger.info(s"retrieved ${views.size} tables in $end ms")
    val db = s"test_${_db}"
    views.foreach(view => {
      val table = view.substring(0, view.length - view.reverse.indexOf("_") - 1)
      val qId = execute(db, CREATE_VIEW.format(view, table, table))
      checkQuery(qId)
      logger.info(s"view $db.$view created (base on $table)")
    })
  }

  def getRows(queryExecutionId: String): List[String] = {
    val tables: mutable.Buffer[String] = mutable.Buffer()
    var token: String = null
    do {
      try {
        val getQueryResultsRequest: GetQueryResultsRequest = new GetQueryResultsRequest().withQueryExecutionId(queryExecutionId)
        val getQueryResultsResults: GetQueryResultsResult = athena.getQueryResults(getQueryResultsRequest)
        import scala.collection.JavaConverters._

        val resultSet: ResultSet = getQueryResultsResults.getResultSet
        val rows: List[Row] = resultSet.getRows.asScala.toList
        rows.foreach(row => {
          val data: List[Datum] = row.getData.asScala.toList
          tables += data.head.getVarCharValue
        })
        token = getQueryResultsResults.getNextToken
      } catch {
        case e: AmazonAthenaException =>
          e.printStackTrace()
          System.exit(1)
      }
    } while (token != null)
    tables.toList
  }
}