package com.mantix4.ap

import com.mantix4.ap.abstracts.sinks.KafkaSink
import com.mantix4.ap.abstracts.sources.KafkaSource
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.{Conn, DNS, HTTP}
import org.apache.spark.sql.streaming.{DataStreamWriter, OutputMode, StreamingQuery}
import org.apache.spark.sql.{DataFrame, Dataset, ForeachWriter, Row}

object Main {
  private val spark = SparkHelper.getAndConfigureSparkSession()
  import spark.implicits._

  def main(args: Array[String]) {

    startNewPipeline(KafkaSource.read(topic = "conn-topic-dev", Conn.schemaBase), Conn.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(topic = "dns-topic-dev", DNS.schemaBase), DNS.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(topic = "http-topic-dev", HTTP.schemaBase), HTTP.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(topic = "p0f-topic-dev"), P0f.getClass.getSimpleName)

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()
  }

  def startNewPipeline(ds: Dataset[Row], provider: String): StreamingQuery = {
    val withProvider = provider.replace("$","")
    KafkaSink.debugStream(ds, withProvider)

    foreach(ds)

    ds
      .toDF()
      .repartition($"sensor")
      .writeStream
      .format(s"com.mantix4.ap.abstracts.base.SinkProvider")
      .option("pipeline", s"com.mantix4.ap.core.pipelines.Pipeline$withProvider")
      .outputMode(OutputMode.Update())
      .queryName(s"KafkaStreamToPipeline$withProvider")
      .start()
  }

  def foreach(kafkaInputDS: DataFrame) : StreamingQuery = {
    kafkaInputDS.writeStream.foreach(new ForeachWriter[Row] {
      override def open(partitionId: Long, version: Long): Boolean = true

      override def process(value: Row): Unit = {
        // Write string to connection
        println(value)
      }

      override def close(errorOrNull: Throwable): Unit = {
        // Close the connection
      }
    }
    ).start()
  }
}
