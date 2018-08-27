package com.mantix4.ap.testing.pipelines

import breeze.linalg.split
import com.mantix4.ap.base.{Filebeat, SinkBase}
import com.mantix4.ap.spark.SparkHelper
import com.mantix4.ap.testing.bro.X509
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{ArrayType, BooleanType, DoubleType, StringType}
import org.apache.spark.sql.{DataFrame, Dataset}

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineNetworkAssets() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show(5000, truncate = true)

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", Conn.cassandraTable, Conn.cassandraColumns)
  }

  def getDataset(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Filebeat.schemaBase))
      .select("data.*")
      .withColumn("date", substring_index($"message", "]", 1))
      .withColumn("p0f_log", substring_index($"message", "] ", -1))
      .select(
        $"date",
        $"p0f_log")
      //.withColumn("_tmp", split($"p0f_log", "\\|"))
  }
}