package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.Files.Files
import org.apache.spark.sql.functions.from_json

class PipelineFiles() extends Pipeline[Files.Files] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[Files.Files]): Unit = {
    // Debug only
    dt.show(5000, truncate = true)
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df
      // Rename column normalization
      .withColumnRenamed("ts", "timestamp")

      // Change column's to the righ type
      .withColumn("tx_hosts", $"tx_hosts".cast(ArrayType(StringType)))
      .withColumn("rx_hosts", $"rx_hosts".cast(ArrayType(StringType)))
      .withColumn("conn_uids", $"conn_uids".cast(ArrayType(StringType)))
      .withColumn("depth", $"depth".cast(DoubleType))
      .withColumn("analyzers", $"analyzers".cast(ArrayType(StringType)))
      .withColumn("duration", $"duration".cast(DoubleType))
      .withColumn("local_orig", $"local_orig".cast(BooleanType))
      .withColumn("is_orig", $"is_orig".cast(BooleanType))
      .withColumn("seen_bytes", $"seen_bytes".cast(DoubleType))
      .withColumn("total_bytes", $"total_bytes".cast(DoubleType))
      .withColumn("missing_bytes", $"missing_bytes".cast(DoubleType))
      .withColumn("overflow_bytes", $"overflow_bytes".cast(DoubleType))
      .withColumn("timedout", $"timedout".cast(BooleanType))
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Files.schemaBase))
  }
}