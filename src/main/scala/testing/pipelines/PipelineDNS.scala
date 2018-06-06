package testing.pipelines

import base.SinkBase
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}
import spark.SparkHelper
import testing.bro.DNS

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineDNS() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    var dataset = getDataset(df)

    // Debug only
    // dataset.show()
    dataset.groupBy($"qtype_name", $"proto").count().sort($"count".desc_nulls_last)

    dataset = dataset.withColumn("query_length", length(col("query")))
    dataset = dataset.withColumn("answer_length", length(col("answers")))
    dataset.show(5000, truncate = false)

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", DNS.cassandraTable, DNS.cassandraColumns)
  }

  def getDataset(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), DNS.schemaBase))
      .select("data.*")

      // Rename column normalization
      .withColumnRenamed("ts", "timestamp")
      .withColumnRenamed("id.orig_h", "source_ip")
      .withColumnRenamed("id.orig_p", "source_port")
      .withColumnRenamed("id.resp_h", "dest_ip")
      .withColumnRenamed("id.resp_p", "dest_port")
      .withColumnRenamed("AA", "aa")
      .withColumnRenamed("TC", "tc")
      .withColumnRenamed("RD", "rd")
      .withColumnRenamed("RA", "ra")
      .withColumnRenamed("Z", "z")
      .withColumnRenamed("TTLs", "ttls")

      // Change column's to the righ type
      .withColumn("source_port", $"source_port".cast(IntegerType))
      .withColumn("dest_port", $"dest_port".cast(IntegerType))
      .withColumn("trans_id", $"trans_id".cast(IntegerType))
      .withColumn("rtt", $"rtt".cast(DoubleType))
      .withColumn("qclass", $"qclass".cast(IntegerType))
      .withColumn("qtype", $"qtype".cast(IntegerType))
      .withColumn("rcode", $"rcode".cast(IntegerType))
      .withColumn("aa", $"aa".cast(BooleanType))
      .withColumn("tc", $"tc".cast(BooleanType))
      .withColumn("rd", $"rd".cast(BooleanType))
      .withColumn("ra", $"ra".cast(BooleanType))
      .withColumn("z", $"z".cast(IntegerType))
      .withColumn("answers", $"answers".cast(ArrayType(StringType)))
      .withColumn("ttls", $"ttls".cast(ArrayType(DoubleType)))
      .withColumn("rejected", $"rejected".cast(BooleanType))

      // .as[DNS.Simple]
  }
}