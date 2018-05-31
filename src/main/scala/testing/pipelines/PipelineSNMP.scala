package testing.pipelines

import base.SinkBase
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import testing.bro.SNMP

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineSNMP() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", SNMP.cassandraTable, SNMP.cassandraColumns)
  }

  def getDataset(df: DataFrame): Dataset[SNMP.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), SNMP.schemaBase))
      .select("data.*")
      .withColumnRenamed("ts", "timestamp")
      .withColumnRenamed("id.orig_h", "source_ip")
      .withColumnRenamed("id.orig_p", "source_port")
      .withColumnRenamed("id.resp_h", "dest_ip")
      .withColumnRenamed("id.resp_p", "dest_port")
      .as[SNMP.Simple]
  }
}