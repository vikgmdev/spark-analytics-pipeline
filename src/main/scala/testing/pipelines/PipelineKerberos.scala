package testing.pipelines

import base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import testing.bro.Kerberos

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineKerberos() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show(5000, false)

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", Kerberos.cassandraTable, Kerberos.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[Kerberos.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Kerberos.schemaBase))
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
      .withColumn("success", $"success".cast(BooleanType))
      .withColumn("forwardable", $"forwardable".cast(BooleanType))
      .withColumn("renewable", $"renewable".cast(BooleanType))

      .as[Kerberos.Simple]
  }
}