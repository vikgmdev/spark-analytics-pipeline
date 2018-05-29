package testing


import kafka.{KafkaSink, KafkaSource}
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{Dataset, Row}
import spark.SparkHelper
import testing.bro._

object MainTestLogs {

  def main(args: Array[String]) {
    val spark = SparkHelper.getAndConfigureSparkSession()

    startNewPipeline(KafkaSource.read(Conn.topicName), Conn.getClass.getName)

    startNewPipeline(KafkaSource.read(DNS.topicName), DNS.getClass.getName)

    startNewPipeline(KafkaSource.read(Files.topicName), Files.getClass.getName)

    startNewPipeline(KafkaSource.read(HTTP.topicName), HTTP.getClass.getName)

    startNewPipeline(KafkaSource.read(Kerberos.topicName), Kerberos.getClass.getName)

    startNewPipeline(KafkaSource.read(SNMP.topicName), SNMP.getClass.getName)

    startNewPipeline(KafkaSource.read(SSL.topicName), SSL.getClass.getName)

    startNewPipeline(KafkaSource.read(Syslog.topicName), Syslog.getClass.getName)

    startNewPipeline(KafkaSource.read(X509.topicName), X509.getClass.getName)

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()
  }

  def startNewPipeline(ds: Dataset[Row], whichProvider: String): StreamingQuery = {
    KafkaSink.debugStream(ds, whichProvider)
    ds
      .toDF()
      .writeStream
      .format(s"base.SinkProvider")
      .option("pipeline", s"testing.pipelines.Pipeline$whichProvider")
      .outputMode(OutputMode.Update())
      .queryName(s"KafkaStreamToPipeline$whichProvider")
      .start()
  }
}
