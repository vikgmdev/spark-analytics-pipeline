package com.mantix4.ap.testing.pipelines

import com.mantix4.ap.base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.spark.SparkHelper
import com.mantix4.ap.testing.bro.SSL

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineSSL() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show(5000, false)

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", SSL.cassandraTable, SSL.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[SSL.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), SSL.schemaBase))
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
      .withColumn("resumed", $"resumed".cast(BooleanType))
      .withColumn("established", $"established".cast(BooleanType))
      .withColumn("cert_chain_fuids", $"cert_chain_fuids".cast(ArrayType(StringType)))
      .withColumn("client_cert_chain_fuids", $"client_cert_chain_fuids".cast(ArrayType(StringType)))

      .as[SSL.Simple]
  }
}