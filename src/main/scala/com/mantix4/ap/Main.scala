package com.mantix4.ap

import com.mantix4.ap.abstracts.sinks.KafkaSink
import com.mantix4.ap.abstracts.sources.KafkaSource
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkObservations.P0f
import com.mantix4.ap.core.logs._
import com.mantix4.ap.core.logs.NetworkProtocols.{Conn, DNS, HTTP}
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{Dataset, Row}

object Main {

  def main(args: Array[String]) {
    val spark = SparkHelper.getAndConfigureSparkSession()

    // startNewPipeline(KafkaSource.read(topic = "conn-topic-dev-ml"), Conn.getClass.getSimpleName)

    startNewPipeline(KafkaSource.read(topic = "dns-topic-dev"), DNS.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(topic = "http-topic-dev"), HTTP.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(topic = "p0f-topic-dev"), P0f.getClass.getSimpleName)

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()
  }

  def startNewPipeline(ds: Dataset[Row], provider: String): StreamingQuery = {
    val withProvider = provider.replace("$","")
    KafkaSink.debugStream(ds, withProvider)
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
