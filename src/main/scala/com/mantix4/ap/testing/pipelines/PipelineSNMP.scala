package com.mantix4.ap.testing.pipelines

import com.mantix4.ap.base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.spark.SparkHelper
import com.mantix4.ap.testing.bro.SNMP

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineSNMP() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show(5000, false)

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", SNMP.cassandraTable, SNMP.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[SNMP.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), SNMP.schemaBase))
      .select("data.*")

      // Rename column normalization
      .withColumnRenamed("ts", "timestamp")
      .withColumnRenamed("id.orig_h", "source_ip")
      .withColumnRenamed("id.orig_p", "source_port")
      .withColumnRenamed("id.resp_h", "dest_ip")
      .withColumnRenamed("id.resp_p", "dest_port")

      // Change column's to the righ type
      .withColumn("source_port", $"source_port".cast(IntegerType))
      .withColumn("dest_port", $"dest_port".cast(IntegerType))
      .withColumn("duration", $"duration".cast(DoubleType))
      .withColumn("get_requests", $"get_requests".cast(DoubleType))
      .withColumn("get_bulk_requests", $"get_bulk_requests".cast(DoubleType))
      .withColumn("get_responses", $"get_responses".cast(DoubleType))
      .withColumn("set_requests", $"set_requests".cast(DoubleType))

      .as[SNMP.Simple]
  }
}