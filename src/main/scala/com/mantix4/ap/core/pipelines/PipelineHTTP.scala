package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.HTTP
import org.apache.spark.sql.functions.from_json

class PipelineHTTP() extends Pipeline[HTTP.HTTP] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[HTTP.HTTP]): Unit = {
    // Debug only
    dt.show(100,truncate = false)
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df

      // Change column's to the righ type, only apply for test's logs
      // TODO: Remove when is real sensor logs
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
    df.withColumn("data",
      from_json($"value".cast(StringType), HTTP.schemaBase))
      .select("data.*")
  }
}