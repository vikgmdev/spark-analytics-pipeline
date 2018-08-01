package com.mantix4.ap.kafka

import org.apache.spark.sql.DataFrame
import com.mantix4.ap.spark.SparkHelper

/**
@see https://com.mantix4.ap.spark.apache.org/docs/latest/structured-streaming-kafka-integration.html
  */
object KafkaSource {
  private val spark = SparkHelper.getSparkSession()

  val kafka_bootstrap_servers = "node4:9092,node5:9092"

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
}