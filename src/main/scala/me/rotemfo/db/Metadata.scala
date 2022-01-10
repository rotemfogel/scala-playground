package me.rotemfo.db

import java.sql._
import scala.collection.mutable

object Metadata extends App {
  val connection: Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/maxwell", "maxwell", "maxwell")
  val resultSet = connection.createStatement().executeQuery("SELECT * FROM t")
  val md: ResultSetMetaData = resultSet.getMetaData
  val list = mutable.ListBuffer[(String, Int)]()
  for (i <- 1 to md.getColumnCount) {
    list += ((md.getColumnName(i), md.getColumnType(i)))
  }
  resultSet.close()
  connection.close()
}
