package pipelines

import bro.Conn
import com.datastax.spark.connector._
import enrichment.ConnEnrichment.withDirection
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import enrichment.ConnEnrichment._

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineConn() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show()

    // Save to Cassandra
    /* dataset.rdd.saveToCassandra("bro",
      Conn.cassandraTable,
      Conn.cassandraColumns
    )*/
  }

  def getDataset(df: DataFrame): DataFrame = {
    df.withColumn("jsondata",
      from_json($"value".cast(StringType), Conn.schemaBase))
      .select("jsondata.*")
      .addSensorName(col("sensor"))
      .withColumnRenamed("ts", "timestamp")
      .withColumnRenamed("id.orig_h", "source_ip")
      .withColumnRenamed("id.orig_p", "source_port")
      .withColumnRenamed("id.resp_h", "dest_ip")
      .withColumnRenamed("id.resp_p", "dest_port")
      .withColumn("direction", withDirection(col("local_orig"), col("local_resp")))
      //.as[Conn.Simple]
    // .addGeoIP()
  }
}