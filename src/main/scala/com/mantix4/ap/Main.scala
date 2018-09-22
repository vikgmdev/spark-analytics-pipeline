package com.mantix4.ap

import com.mantix4.ap.abstracts.sinks.KafkaSink
import com.mantix4.ap.abstracts.sources.KafkaSource
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.{Conn, DNS, HTTP}
import org.apache.spark.sql.streaming.{DataStreamWriter, OutputMode, StreamingQuery}
import org.apache.spark.sql.{DataFrame, Dataset, ForeachWriter, Row}

// TODO: Check https://github.com/dibbhatt/kafka-spark-consumer
/*
  Submitting:
  spark-submit --master yarn-client \
       --num-executors 2 \
       --driver-memory 512m \
       --executor-memory 512m \
       --executor-cores 1 \
       --class simpleexample.SparkFileExample \
       spark-streaming-simple-example-0.1-SNAPSHOT.jar /spark_log
 */
/**
  * * Takes
  *  args(0) - sensor name or id
  */
object Main {

  def main(args: Array[String]) {
    if (args.length < 1)
    {
      System.err.println("Usage: <sensor-name>")
      System.exit(1)
    }

    val Array(sensor_name) = args

    // Receive the sensor name
    SparkHelper.sensor_name = sensor_name

    val spark = SparkHelper.getAndConfigureSparkSession()

    startNewPipeline(KafkaSource.read(topic = "conn-topic-dev", Conn.schemaBase), Conn.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(topic = "dns-topic-dev", DNS.schemaBase), DNS.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(topic = "http-topic-dev", HTTP.schemaBase), HTTP.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(topic = "p0f-topic-dev"), P0f.getClass.getSimpleName)

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()
  }

  /**
    * Create an input stream that returns tweets received from Gnip.
    * @param ds           Dataset[Row] object
    * @param provider     Log's pipeline to execute
    */
  def startNewPipeline(
    ds: Dataset[Row],
    provider: String): StreamingQuery = {
    val withProvider = provider.replace("$","")
    // KafkaSink.debugStream(ds, withProvider)
    ds
      .toDF()
      .writeStream
      .format(s"com.mantix4.ap.abstracts.base.SinkProvider")
      .option("pipeline", s"com.mantix4.ap.core.pipelines.Pipeline$withProvider")
      .outputMode(OutputMode.Update())
      .queryName(s"KafkaStreamToPipeline$withProvider")
      .start()
  }
}
