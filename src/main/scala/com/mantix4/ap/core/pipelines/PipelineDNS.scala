package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.DNS
import com.mantix4.ap.core.ml.AnomalyDetection

class PipelineDNS() extends Pipeline[DNS.DNS] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[DNS.DNS]): Unit = {
    // Debug only
    dt.show(100, truncate = false)

    /*

    // Set Categorical and Numeric columns features to detect outliers
    val categoricalColumns = Array("z", "rejected", "proto", "query", "qclass_name", "qtype_name", "rcode_name")
    val numericCols = Array("query_length", "answer_length")

    val data_with_outliers = AnomalyDetection.main[DNS.DNS](dt, categoricalColumns, numericCols)

    println("Outliers detected: ")
    data_with_outliers.printSchema()
    data_with_outliers.show()
    */
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df
      // Rename column normalization
      .withColumnRenamed("AA", "aa")
      .withColumnRenamed("TC", "tc")
      .withColumnRenamed("RD", "rd")
      .withColumnRenamed("RA", "ra")
      .withColumnRenamed("Z", "z")
      .withColumnRenamed("TTLs", "ttls")

      // Change column's to the righ type
      .withColumn("answers", split(col("answers"), ","))
      .withColumn("ttls", split(col("ttls"), ",").cast(ArrayType(DoubleType)))

      /*
      // Add columns needed for Anomaly Detection
      .withColumn("query_length", length(col("query")))
      // Normalize query_length
      .withColumn("query_length_norm",
          (col("query_length") - min("query_length")).cast(DoubleType) /
          (max("query_length") - min("query_length")).cast(DoubleType)
          )
      .withColumn("answer_length", size(col("answers")))
      */
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), DNS.schemaBase))
      .select("data.*")
  }
}