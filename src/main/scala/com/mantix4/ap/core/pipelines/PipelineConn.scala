package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.enrichments.ConnEnricher
import com.mantix4.ap.core.logs.NetworkProtocols.Conn
import com.mantix4.ap.abstracts.cassandra.CassandraCRUDHelper._
import com.mantix4.ap.core.ml.AnomalyDetection
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

    // Set Categorical and Numeric columns features to detect outliers
    val categoricalColumns = Array("proto", "direction")
    val numericCols = Array("duration","pcr")

    val data_with_outliers = AnomalyDetection.main(dt, categoricalColumns, numericCols)

    println("Outliers detected: ")
    data_with_outliers.show(false)

    data_with_outliers.drop("protoIndex")
    data_with_outliers.drop("protoclassVec")
    data_with_outliers.drop("directionIndex")
    data_with_outliers.drop("directionclassVec")
    data_with_outliers.drop("features")
    data_with_outliers.drop("anomalyScore")
    data_with_outliers.drop("prediction")
    data_with_outliers.drop("pcaFeaturesArray")

    data_with_outliers.printSchema()

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