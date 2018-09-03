package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.core.enrichments.AddConnDirection._
import com.mantix4.ap.core.enrichments.AddPCR._
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Conn

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineConn() extends Pipeline[Conn] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def startPipeline(dt: Dataset[Conn]): Unit = {
    // Debug only
    dt.show(5000, truncate = true)
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df
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
  }
}