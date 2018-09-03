package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Kerberos

class PipelineKerberos() extends Pipeline[Kerberos] {
  private val spark = SparkHelper.getSparkSession
  import spark.implicits._

  override def startPipeline(dt: Dataset[Kerberos]): Unit = {
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
      .withColumn("success", $"success".cast(BooleanType))
      .withColumn("forwardable", $"forwardable".cast(BooleanType))
      .withColumn("renewable", $"renewable".cast(BooleanType))
  }
}