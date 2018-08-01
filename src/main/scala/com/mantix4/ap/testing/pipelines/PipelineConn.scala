package com.mantix4.ap.testing.pipelines

import com.mantix4.ap.base.SinkBase
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.testing.bro.Conn
import com.mantix4.ap.testing.enrichment.ConnEnrichment._
import com.mantix4.ap.testing.enrichment.EnrichPCR._
import com.mantix4.ap.spark.SparkHelper

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineConn() extends SinkBase {
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

  def getDataset(df: DataFrame): Dataset[Conn.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Conn.schemaBase))
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
      .withColumn("orig_bytes", $"orig_bytes".cast(DoubleType))
      .withColumn("resp_bytes", $"resp_bytes".cast(DoubleType))
      .withColumn("local_orig", $"local_orig".cast(BooleanType))
      .withColumn("local_resp", $"local_resp".cast(BooleanType))
      .withColumn("missed_bytes", $"missed_bytes".cast(DoubleType))
      .withColumn("orig_pkts", $"orig_pkts".cast(DoubleType))
      .withColumn("orig_ip_bytes", $"orig_ip_bytes".cast(DoubleType))
      .withColumn("resp_pkts", $"resp_pkts".cast(DoubleType))
      .withColumn("resp_ip_bytes", $"resp_ip_bytes".cast(DoubleType))
      .withColumn("tunnel_parents", $"tunnel_parents".cast(ArrayType(StringType)))

      // Enrich
      .withColumn("direction", withDirection(col("local_orig"), col("local_resp")))
      .withColumn("pcr", withPCR($"direction", $"orig_bytes", $"resp_bytes"))

      // Convert to object
      .as[Conn.Simple]
  }
}