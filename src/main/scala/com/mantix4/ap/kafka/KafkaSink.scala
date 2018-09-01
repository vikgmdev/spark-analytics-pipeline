package com.mantix4.ap.kafka

import com.mantix4.ap.kafka.KafkaSource.kafka_bootstrap_servers
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.streaming.StreamingQuery
import com.mantix4.ap.spark.SparkHelper

object KafkaSink {

  private val spark = SparkHelper.getSparkSession()

  /**
  Console sink from Kafka's stream
      +----+--------------------+-----+---------+------+--------------------+-------------+--------------------+
      | key|               value|topic|partition|offset|           timestamp|timestampType|          radioCount|
      +----+--------------------+-----+---------+------+--------------------+-------------+--------------------+
      |null|[7B 22 72 61 64 6...| test|        0|    60|2017-11-21 22:56:...|            0|[Feel No Ways,Dra...|
    *
    */
  def debugStream(kafkaInputDS: DataFrame, queryName: String): StreamingQuery = {
    kafkaInputDS
      .writeStream
      .queryName("Debug Stream Kafka - " + queryName)
      .option("truncate", value = true)
      .format("console")
      .start()
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
