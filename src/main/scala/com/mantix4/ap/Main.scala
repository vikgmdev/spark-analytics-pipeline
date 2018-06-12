package com.mantix4.ap


import com.mantix4.ap.kafka.{KafkaSink, KafkaSource}
import com.mantix4.ap.production.bro._
import com.mantix4.ap.spark.SparkHelper
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{Dataset, Row}

object Main {

  def main(args: Array[String]) {
    val spark = SparkHelper.getAndConfigureSparkSession()

    startNewPipeline(KafkaSource.read(Conn.topicName), Conn.getClass.getSimpleName)

    startNewPipeline(KafkaSource.read(DNS.topicName), DNS.getClass.getSimpleName)

    startNewPipeline(KafkaSource.read(PCR.topicName), PCR.getClass.getSimpleName)

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()
  }

  def startNewPipeline(ds: Dataset[Row], provider: String): StreamingQuery = {
    val withProvider = provider.replace("$","")
    KafkaSink.debugStream(ds, withProvider)
    ds
      .toDF()
      .writeStream
      .format(s"com.mantix4.ap.base.SinkProvider")
      .option("pipeline", s"production.pipelines.Pipeline$withProvider")
      .outputMode(OutputMode.Update())
      .queryName(s"KafkaStreamToPipeline$withProvider")
      .start()
  }
}
