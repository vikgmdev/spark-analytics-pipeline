package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.types.{ArrayType, BooleanType, DoubleType, StringType}
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.Files.X509
import org.apache.spark.sql.functions.from_json

class PipelineX509() extends Pipeline[X509.X509] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[X509.X509]): Unit = {
    // Debug only
    dt.show(5000, truncate = true)
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df
      // Rename column normalization
      .withColumnRenamed("ts", "timestamp")
      .withColumnRenamed("certificate.version", "certificate_version")
      .withColumnRenamed("certificate.serial", "certificate_serial")
      .withColumnRenamed("certificate.subject", "certificate_subject")
      .withColumnRenamed("certificate.issuer", "certificate_issuer")
      .withColumnRenamed("certificate.not_valid_before", "certificate_not_valid_before")
      .withColumnRenamed("certificate.not_valid_after", "certificate_not_valid_after")
      .withColumnRenamed("certificate.key_alg", "certificate_key_alg")
      .withColumnRenamed("certificate.sig_alg", "certificate_sig_alg")
      .withColumnRenamed("certificate.key_type", "certificate_key_type")
      .withColumnRenamed("certificate.key_length", "certificate_key_length")
      .withColumnRenamed("certificate.exponent", "certificate_exponent")
      .withColumnRenamed("certificate.curve", "certificate_curve")
      .withColumnRenamed("san.dns", "san_dns")
      .withColumnRenamed("san.uri", "san_uri")
      .withColumnRenamed("san.email", "san_email")
      .withColumnRenamed("san.ip", "san_ip")
      .withColumnRenamed("basic_constraints.ca", "basic_constraints_ca")
      .withColumnRenamed("basic_constraints.path_len", "basic_constraints_path_len")

      // Change column's to the righ type
      .withColumn("certificate_version", $"certificate_version".cast(DoubleType))
      .withColumn("certificate_key_length", $"certificate_key_length".cast(DoubleType))
      .withColumn("san_dns", $"san_dns".cast(ArrayType(StringType)))
      .withColumn("san_uri", $"san_uri".cast(ArrayType(StringType)))
      .withColumn("san_email", $"san_email".cast(ArrayType(StringType)))
      .withColumn("san_ip", $"san_ip".cast(ArrayType(StringType)))
      .withColumn("basic_constraints_ca", $"basic_constraints_ca".cast(BooleanType))
      .withColumn("basic_constraints_path_len", $"basic_constraints_path_len".cast(DoubleType))
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), X509.schemaBase))
  }
}