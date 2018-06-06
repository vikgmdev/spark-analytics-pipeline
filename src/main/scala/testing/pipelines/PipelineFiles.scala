package testing.pipelines

import base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types._
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
    dataset.show(false)

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", Files.cassandraTable, Files.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[Files.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Files.schemaBase))
      .select("data.*")

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

      .as[Files.Simple]
  }
}