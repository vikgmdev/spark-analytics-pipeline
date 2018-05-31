package testing.pipelines

import base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.{ArrayType, BooleanType, DoubleType, StringType}
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import testing.bro.X509

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineX509() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", Conn.cassandraTable, Conn.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[X509.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), X509.schemaBase))
      .select("data.*")
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
      .as[X509.Simple]
  }
}