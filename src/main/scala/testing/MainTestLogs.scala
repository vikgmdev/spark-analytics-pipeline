package testing


import kafka.{KafkaSink, KafkaSource}
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{Dataset, Row}
import spark.SparkHelper
import testing.bro._

object MainTestLogs {

  def main(args: Array[String]) {
    val spark = SparkHelper.getAndConfigureSparkSession()

    // GOOD
    // startNewPipeline(KafkaSource.read(Conn.topicName), Conn.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(DNS.topicName), DNS.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(Files.topicName), Files.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(HTTP.topicName), HTTP.getClass.getSimpleName)

    // GOOD
    // startNewPipeline(KafkaSource.read(Kerberos.topicName), Kerberos.getClass.getSimpleName)

    // GOOD
    // startNewPipeline(KafkaSource.read(SNMP.topicName), SNMP.getClass.getSimpleName)

    // BAD
    // startNewPipeline(KafkaSource.read(SSL.topicName), SSL.getClass.getSimpleName)

    startNewPipeline(KafkaSource.read(Syslog.topicName), Syslog.getClass.getSimpleName)

    // startNewPipeline(KafkaSource.read(X509.topicName), X509.getClass.getSimpleName)

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()
  }

  def startNewPipeline(ds: Dataset[Row], provider: String): StreamingQuery = {
    val withProvider = provider.replace("$","")
    KafkaSink.debugStream(ds, withProvider)
    ds
      .toDF()
      .writeStream
      .format(s"base.SinkProvider")
      .option("pipeline", s"testing.pipelines.Pipeline$withProvider")
      .outputMode(OutputMode.Update())
      .queryName(s"KafkaStreamToPipeline$withProvider")
      .start()
  }
}
