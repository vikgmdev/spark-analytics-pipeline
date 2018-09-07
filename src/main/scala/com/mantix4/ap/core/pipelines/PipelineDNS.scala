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
    dt.show(100)

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
      // Rename column normalization
      .withColumnRenamed("AA", "aa")
      .withColumnRenamed("TC", "tc")
      .withColumnRenamed("RD", "rd")
      .withColumnRenamed("RA", "ra")
      .withColumnRenamed("Z", "z")
      .withColumnRenamed("TTLs", "ttls")

      // Change column's to the righ type
      /*
      .withColumn("source_port", $"source_port".cast(IntegerType))
      .withColumn("dest_port", $"dest_port".cast(IntegerType))
      .withColumn("trans_id", $"trans_id".cast(IntegerType))
      .withColumn("rtt", $"rtt".cast(DoubleType))
      .withColumn("qclass", $"qclass".cast(IntegerType))
      .withColumn("qtype", $"qtype".cast(IntegerType))
      .withColumn("rcode", $"rcode".cast(IntegerType))
      .withColumn("aa", $"aa".cast(BooleanType))
      .withColumn("tc", $"tc".cast(BooleanType))
      .withColumn("rd", $"rd".cast(BooleanType))
      .withColumn("ra", $"ra".cast(BooleanType))
      .withColumn("z", $"z".cast(IntegerType))
      //.withColumn("answers", $"answers".cast(ArrayType(StringType)))
      .withColumn("ttls", $"ttls".cast(DoubleType))
      .withColumn("rejected", $"rejected".cast(BooleanType))
    */
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), DNS.schemaBase))
      .select("data.*")
  }
}