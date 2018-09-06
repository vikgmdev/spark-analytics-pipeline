package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.HTTP.HTTP
import org.apache.spark.sql.functions.from_json

class PipelineHTTP() extends Pipeline[HTTP] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[HTTP]): Unit = {
    // Debug only
    dt.show(5000, truncate = true)
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df
      // Rename column normalization
      .withColumnRenamed("ts", "timestamp")
      .withColumnRenamed("id.orig_h", "source_ip")
      .withColumnRenamed("id.orig_p", "source_port")
      .withColumnRenamed("id.resp_h", "dest_ip")
      .withColumnRenamed("id.resp_p", "dest_port")

      // Change column's to the righ type
      .withColumn("source_port", $"source_port".cast(IntegerType))
      .withColumn("dest_port", $"dest_port".cast(IntegerType))
      .withColumn("trans_depth", $"trans_depth".cast(DoubleType))
      .withColumn("request_body_len", $"request_body_len".cast(DoubleType))
      .withColumn("response_body_len", $"response_body_len".cast(DoubleType))
      .withColumn("status_code", $"status_code".cast(DoubleType))
      .withColumn("info_code", $"info_code".cast(DoubleType))
      .withColumn("tags", $"tags".cast(ArrayType(StringType)))
      .withColumn("proxied", $"proxied".cast(ArrayType(StringType)))
      .withColumn("orig_fuids", $"orig_fuids".cast(ArrayType(StringType)))
      .withColumn("orig_filenames", $"orig_filenames".cast(ArrayType(StringType)))
      .withColumn("orig_mime_types", $"orig_mime_types".cast(ArrayType(StringType)))
      .withColumn("resp_fuids", $"resp_fuids".cast(ArrayType(StringType)))
      .withColumn("resp_filenames", $"resp_filenames".cast(ArrayType(StringType)))
      .withColumn("resp_mime_types", $"resp_mime_types".cast(ArrayType(StringType)))
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    /*
    val schema_base = Encoders.product[HTTP].asInstanceOf[HTTP]
    df.withColumn("data",
      from_json($"value".cast(StringType), schema_base.schemaBase))
    */
  }
}