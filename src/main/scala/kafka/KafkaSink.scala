package kafka

import bro.Conn
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.streaming.StreamingQuery
import spark.SparkHelper

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
  def debugStream[T](kafkaInputDS: Dataset[T], queryName: String): StreamingQuery = {
    kafkaInputDS
      .writeStream
      .queryName("Debug Stream Kafka - " + queryName)
      .option("truncate", value = true)
      .format("console")
      .start()
  }
}
