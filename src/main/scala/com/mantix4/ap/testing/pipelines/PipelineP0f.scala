package com.mantix4.ap.testing.pipelines

import com.mantix4.ap.base.{Filebeat, SinkBase}
import com.mantix4.ap.spark.SparkHelper
import com.mantix4.ap.testing.bro.P0f
import com.mantix4.ap.testing.enrichment.ConnEnrichment._
import com.mantix4.ap.testing.enrichment.EnrichPCR._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineP0f() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show(5000, false)
    dataset.printSchema()

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", Conn.cassandraTable, Conn.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[P0f.Simple] = {
    df.withColumn("filebeat_log",
        from_json($"value".cast(StringType), Filebeat.schemaBase))
      .select("value.*")
      .withColumn("p0f_log",
        from_json($"json".cast(StringType), P0f.schemaBase))
      .select("p0f_log.*")

      // Convert to object
      .as[P0f.Simple]
  }
}