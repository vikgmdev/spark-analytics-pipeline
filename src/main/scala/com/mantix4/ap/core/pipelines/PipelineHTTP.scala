package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.HTTP
import com.mantix4.ap.core.ml.AnomalyDetection
import org.apache.spark.sql.functions.{col, from_json, split}

class PipelineHTTP() extends Pipeline[HTTP.HTTP] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[HTTP.HTTP]): Unit = {
    // Debug only
    dt.show(100,truncate = false)

    /*
    // Set Categorical and Numeric columns features to detect outliers
    val categoricalColumns = Array("dest_port", "method", "resp_mime_types")
    val numericCols = Array("request_body_len")

    val data_with_outliers = AnomalyDetection.main[HTTP.HTTP](dt, categoricalColumns, numericCols)

    println("Outliers detected: ")
    data_with_outliers.printSchema()
    data_with_outliers.show()
    */
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df

      // Change column's to the righ type, only apply for test's logs
      // TODO: Remove when is real sensor logs
      .withColumn("tags", split(col("tags"), ","))
      .withColumn("proxied", split(col("proxied"), ","))
      .withColumn("orig_fuids", split(col("orig_fuids"), ","))
      .withColumn("orig_filenames", split(col("orig_filenames"), ","))
      .withColumn("orig_mime_types", split(col("orig_mime_types"), ","))
      .withColumn("resp_fuids", split(col("resp_fuids"), ","))
      .withColumn("resp_filenames", split(col("resp_filenames"), ","))
      .withColumn("resp_mime_types", split(col("resp_mime_types"), ","))
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), HTTP.schemaBase))
      .select("data.*")
  }
}