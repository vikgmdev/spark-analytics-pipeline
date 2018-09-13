package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.enrichments.ConnEnricher
import com.mantix4.ap.core.logs.NetworkProtocols.Conn
import com.mantix4.ap.core.ml.AnomalyDetection
import org.apache.spark.sql.catalyst.encoders.RowEncoder

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineConn() extends Pipeline[Conn.Conn] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._
  import com.mantix4.ap.core.enrichments.IpLookupEnricher._

  def startPipeline(dt: Dataset[Conn.Conn]): Unit = {
    // Debug only
    dt.show(100,truncate = false)

    /*

    // Set Categorical and Numeric columns features to detect outliers
    val categoricalColumns = Array("proto", "direction")
    val numericCols = Array("pcr")

    val data_with_outliers = AnomalyDetection.main(dt, categoricalColumns, numericCols)

    println("Outliers detected: ")
    data_with_outliers.printSchema()
    data_with_outliers.show()
    */
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df

      .withColumn("tunnel_parents", split(col("tunnel_parents"), ","))

      // Enrich
      .withColumn("direction", ConnEnricher.withDirection(col("local_orig"), col("local_resp")))
      .withColumn("pcr", ConnEnricher.withPCR($"direction", $"orig_bytes", $"resp_bytes"))
      .addGeoIPdata($"direction".toString, $"source_ip".toString, $"dest_ip".toString)
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Conn.schemaBase))
      .select("data.*")
  }
}