package com.mantix4.ap

import com.mantix4.ap.abstracts.sinks.KafkaSink
import com.mantix4.ap.abstracts.sources.KafkaSource
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkObservations.P0f
import com.mantix4.ap.core.logs._
import com.mantix4.ap.core.logs.NetworkProtocols.Conn
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{Dataset, Row}

object Main {

  def main(args: Array[String]) {
    val spark = SparkHelper.getAndConfigureSparkSession()

    // GOOD
    startNewPipeline(KafkaSource.read(topic = "conn-topic-dev-ml"), Conn.getClass.getSimpleName)

    // GOOD
    // startNewPipeline(KafkaSource.read(DNS.topicName), DNS.getClass.getSimpleName)

    // BAD
    // startNewPipeline(KafkaSource.read(Files.topicName), Files.getClass.getSimpleName)

    // BAD
    // startNewPipeline(KafkaSource.read(HTTP.topicName), HTTP.getClass.getSimpleName)

    // GOOD
    // startNewPipeline(KafkaSource.read(Kerberos.topicName), Kerberos.getClass.getSimpleName)

    // GOOD
    // startNewPipeline(KafkaSource.read(SNMP.topicName), SNMP.getClass.getSimpleName)

    // BAD
    // startNewPipeline(KafkaSource.read(SSL.topicName), SSL.getClass.getSimpleName)

    // GOOD
    // startNewPipeline(KafkaSource.read(Syslog.topicName), Syslog.getClass.getSimpleName)

    // BAAAAAD
    // startNewPipeline(KafkaSource.read(X509.topicName), X509.getClass.getSimpleName)

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
