package testing.pipelines

import base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import testing.bro.Files

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineFiles() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", Files.cassandraTable, Files.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[Files.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Files.schemaBase))
      .select("data.*")
      .withColumnRenamed("ts", "timestamp")
      .as[Files.Simple]
  }
}