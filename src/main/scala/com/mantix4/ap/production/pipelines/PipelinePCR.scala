package com.mantix4.ap.production.pipelines

import com.mantix4.ap.base.SinkBase
import com.datastax.spark.connector._
import com.mantix4.ap.production.bro.PCR
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.spark.SparkHelper

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelinePCR() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset: Dataset[PCR.Simple] = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", PCR.cassandraTable, PCR.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[PCR.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), PCR.schemaBase))
      .select("data.*")
      .as[PCR.Simple]
  }
}