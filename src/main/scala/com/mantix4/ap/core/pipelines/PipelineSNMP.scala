package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.SNMP.SNMP
import org.apache.spark.sql.functions.from_json

class PipelineSNMP() extends Pipeline[SNMP] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[SNMP]): Unit = {
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
      .withColumn("duration", $"duration".cast(DoubleType))
      .withColumn("get_requests", $"get_requests".cast(DoubleType))
      .withColumn("get_bulk_requests", $"get_bulk_requests".cast(DoubleType))
      .withColumn("get_responses", $"get_responses".cast(DoubleType))
      .withColumn("set_requests", $"set_requests".cast(DoubleType))
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    /*
    val schema_base = Encoders.product[SNMP].asInstanceOf[SNMP]
    df.withColumn("data",
      from_json($"value".cast(StringType), schema_base.schemaBase))
      */
  }
}