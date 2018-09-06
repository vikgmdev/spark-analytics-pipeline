package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.enrichments.ConnEnricher
import com.mantix4.ap.core.logs.NetworkProtocols.Conn
import org.apache.spark.sql.catalyst.encoders.RowEncoder

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineConn() extends Pipeline[Conn.Conn] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def startPipeline(dt: Dataset[Conn.Conn]): Unit = {
    // Debug only
    dt.show(5000)
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df
      // Enrich
      .withColumn("direction", ConnEnricher.withDirection(col("local_orig"), col("local_resp")))
      .withColumn("pcr", ConnEnricher.withPCR($"direction", $"orig_bytes", $"resp_bytes"))
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Conn.schemaBase))
      .select("data.*")
  }
}