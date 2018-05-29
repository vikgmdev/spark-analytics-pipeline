package kafka

import bro.Conn
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.functions.{struct, to_json, _}
import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.types.{StringType, _}
import spark.SparkHelper

/**
@see https://spark.apache.org/docs/latest/structured-streaming-kafka-integration.html
  */
object KafkaSource {
  private val spark = SparkHelper.getSparkSession()

  import spark.implicits._

  val kafka_bootstrap_servers = "node5:9094,node5:9095"

  /**
    * will return, we keep some kafka metadata for our example, otherwise we would only focus on "radioCount" structure
     |-- key: binary (nullable = true)
     |-- value: binary (nullable = true)
     |-- topic: string (nullable = true) : KEPT
     |-- partition: integer (nullable = true) : KEPT
     |-- offset: long (nullable = true) : KEPT
     |-- timestamp: timestamp (nullable = true) : KEPT
     |-- timestampType: integer (nullable = true)
     |-- radioCount: struct (nullable = true)
     |    |-- title: string (nullable = true)
     |    |-- artist: string (nullable = true)
     |    |-- radio: string (nullable = true)
     |    |-- count: long (nullable = true)

    * @return
    *
    *
    * startingOffsets should use a JSON coming from the lastest offsets saved in our DB (Cassandra here)
    */
  // def read(startingOption: String = "startingOffsets", partitionsAndOffsets: String = "earliest") : Dataset[Conn.SimpleKafka] = {
  def read(topic: String) : DataFrame = {
    println(s"Reading from Kafka, topic: '$topic'")
    spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", kafka_bootstrap_servers)
      .option("subscribe", topic)
      .option("startingOffsets", "earliest")
      .option("enable.auto.commit", value = false)
      .option("group.id", s"Kafka-Streaming-Topic-$topic")
      .option("failOnDataLoss", value = false)
      .load()

      // .withColumn("typeLog", from_json($"value".cast(StringType), KafkaService.schemaTypeColumn).getField("type"))
  }

  def write(dataFrame: DataFrame, topic: String) : StreamingQuery = {
    println(s"Writing to Kafka, topic: '$topic'")
    dataFrame.selectExpr("to_json(struct(*)) AS value").
      writeStream
      .format("kafka")
      .option("topic", topic)
      .option("kafka.bootstrap.servers", kafka_bootstrap_servers)
      .start()
  }
}
