package testing.pipelines

import base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import testing.bro.HTTP

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineHTTP() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", HTTP.cassandraTable, HTTP.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[HTTP.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), HTTP.schemaBase))
      .select("data.*")

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
      .withColumn("orig_mime_types", $"orig_mime_types".cast(ArrayType(StringType)))
      .withColumn("resp_fuids", $"resp_fuids".cast(ArrayType(StringType)))
      .withColumn("resp_mime_types", $"resp_mime_types".cast(ArrayType(StringType)))

      .as[HTTP.Simple]
  }
}