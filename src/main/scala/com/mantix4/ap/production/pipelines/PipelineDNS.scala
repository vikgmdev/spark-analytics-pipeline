package com.mantix4.ap.production.pipelines

import com.mantix4.ap.base.SinkBase
import com.mantix4.ap.production.bro.DNS
import com.mantix4.ap.production.bro.DNS.Simple
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.spark.SparkHelper

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineDNS() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", DNS.cassandraTable, DNS.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), DNS.schemaBase))
      .select("data.*")
      .withColumnRenamed("AA", "aa")
      .withColumnRenamed("TC", "tc")
      .withColumnRenamed("RD", "rd")
      .withColumnRenamed("RA", "ra")
      .withColumnRenamed("Z", "z")
      .withColumnRenamed("TTLs", "ttls")
      .as[DNS.Simple]
  }
}