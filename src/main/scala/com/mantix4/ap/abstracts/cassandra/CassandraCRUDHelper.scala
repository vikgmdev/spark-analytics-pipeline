package com.mantix4.ap.abstracts.cassandra

import com.datastax.spark.connector._
import com.datastax.spark.connector.writer.SqlRowWriter
import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.sql.DataFrame


object CassandraCRUDHelper {

  implicit class DataFrameTransforms(df: DataFrame) {

    def saveToCassandra(tableName: String, tableColumns: SomeColumns): DataFrame = {
      implicit val rowWriter: SqlRowWriter.Factory.type = SqlRowWriter.Factory
      df.rdd.saveToCassandra(SparkHelper.sensor_name, tableName, tableColumns)
      df
    }
  }
}
