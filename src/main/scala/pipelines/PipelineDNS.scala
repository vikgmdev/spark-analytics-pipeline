package pipelines

import bro.DNS
import com.datastax.spark.connector._
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import enrichment.ConnEnrichment._

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineDNS() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    /* dataset.rdd.saveToCassandra("bro",
      DNS.cassandraTable,
      DNS.cassandraColumns
    ) */
  }

  def getDataset(df: DataFrame): Dataset[DNS.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), DNS.schemaBase))
      .select("data.*")
      .as[DNS.Simple]
  }
}