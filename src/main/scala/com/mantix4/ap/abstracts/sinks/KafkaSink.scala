package com.mantix4.ap.abstracts.sinks

import com.mantix4.ap.Main.spark
import com.mantix4.ap.abstracts.sources.KafkaSource.kafka_bootstrap_servers
import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.streaming.StreamingQuery

object KafkaSink {

  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  /**
  Console sink from Kafka's stream
      +----+--------------------+-----+---------+------+--------------------+-------------+--------------------+
      | key|               value|topic|partition|offset|           timestamp|timestampType|          radioCount|
      +----+--------------------+-----+---------+------+--------------------+-------------+--------------------+
      |null|[7B 22 72 61 64 6...| test|        0|    60|2017-11-21 22:56:...|            0|[Feel No Ways,Dra...|
    *
    */
  def debugStream(kafkaInputDS: DataFrame, queryName: String) : StreamingQuery = {
    kafkaInputDS
      .repartition($"sensor")
      .groupBy($"sensor")
      .count()
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
