package pipelines

import bro.PCR
import com.datastax.spark.connector._
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import enrichment.ConnEnrichment._

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelinePCR() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset: Dataset[PCR.Simple] = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    dataset.rdd.saveToCassandra(col("sensor").toString(),
      PCR.cassandraTable,
      PCR.cassandraColumns
    )
  }

  def getDataset(df: DataFrame): Dataset[PCR.Simple] = {
    df.withColumn("data",
      from_json($"value".cast(StringType), PCR.schemaBase))
      .select("data.*")
      .as[PCR.Simple]
  }
}