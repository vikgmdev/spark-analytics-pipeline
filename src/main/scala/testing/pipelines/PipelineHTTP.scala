package testing.pipelines

import base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.StringType
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
      .withColumnRenamed("ts", "timestamp")
      .withColumnRenamed("id.orig_h", "source_ip")
      .withColumnRenamed("id.orig_p", "source_port")
      .withColumnRenamed("id.resp_h", "dest_ip")
      .withColumnRenamed("id.resp_p", "dest_port")
      .as[HTTP.Simple]
  }
}