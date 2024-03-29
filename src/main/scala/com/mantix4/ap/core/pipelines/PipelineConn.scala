package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.enrichments.{ConnEnricher, PCROberserver}
import com.mantix4.ap.core.logs.NetworkProtocols.Conn
import com.mantix4.ap.abstracts.cassandra.CassandraCRUDHelper._
import com.mantix4.ap.core.ml.AnomalyDetectionK
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.types.TimestampType

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineConn() extends Pipeline[Conn.Conn](Conn.schemaBase) {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def startPipeline(dt: Dataset[Conn.Conn]): Unit = {
    // Debug only
    dt.show()

    val dataWithIntervalsDF = PCROberserver.main(dt.toDF())

    // Set Categorical and Numeric columns features to detect outliers
    val categoricalColumns = Array("source_ip", "dest_ip")
    val numericCols = Array("diff_interval")

    var data_with_outliers = AnomalyDetectionK.main(dataWithIntervalsDF, categoricalColumns, numericCols)

    println("Outliers detected: ")
    data_with_outliers.show(false)


    data_with_outliers.saveToCassandra("conn", Conn.tableColumns)
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