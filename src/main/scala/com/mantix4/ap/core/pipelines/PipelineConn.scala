package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.enrichments.ConnEnricher
import com.mantix4.ap.core.logs.NetworkProtocols.Conn
import com.mantix4.ap.core.enrichments.IpLookupEnricher._
import org.apache.spark.sql.types.TimestampType

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineConn() extends Pipeline[Conn.Conn](Conn.schemaBase) {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def startPipeline(dt: Dataset[Conn.Conn]): Unit = {
    // Debug only
    dt.show(5000)
    val sensor_name = dt.toDF().select("sensor").takeAsList(1).get(0).getString(0)
    dt.toDF().saveToCassandra(sensor_name)

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
      .withColumn("timestamp", from_unixtime($"timestamp"))
      // .withColumn("date", from_unixtime($"timestamp"))
      .withColumn("tunnel_parents", split(col("tunnel_parents"), ","))

      // Enrich
      .withColumn("direction", ConnEnricher.withDirection(col("local_orig"), col("local_resp")))
      .withColumn("pcr", ConnEnricher.withPCR($"direction", $"orig_bytes", $"resp_bytes"))
      //.addGeoIPdata($"direction".toString, $"source_ip".toString, $"dest_ip".toString)
  }
}