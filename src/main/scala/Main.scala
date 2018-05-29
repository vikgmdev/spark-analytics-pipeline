
import bro.Conn
import kafka.{KafkaSink, KafkaSource}
import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{Dataset, Row}
import spark.SparkHelper

object Main {

  def main(args: Array[String]) {
    val spark = SparkHelper.getAndConfigureSparkSession()

    /*
    val kafkaTopicConn = KafkaSource.read("conn")
    startNewPipeline(kafkaTopicConn, "Conn")
    */

    val streamingDataFrame = spark.readStream.schema(Conn.schemaBaseCSV).csv("/opt/data/csv/")
    KafkaSink.debugStream(streamingDataFrame, "conn-from-csv")
    KafkaSource.write(streamingDataFrame, "conn-csv")


    val kafkaTopicConnCSV = KafkaSource.read("conn-csv")
    KafkaSink.debugStream(kafkaTopicConnCSV, "conn-csv")
    startNewPipeline(kafkaTopicConnCSV, "ConnCSV")

    /*
    val kafkaTopicDNS = KafkaSource.read("dns")
    startNewPipeline(kafkaTopicDNS, "DNS")

    val kafkaTopicPCR = KafkaSource.read("pcr")
    startNewPipeline(kafkaTopicPCR, "PCR")
    */

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()
  }

  def startNewPipeline(ds: Dataset[Row], whichProvider: String): StreamingQuery = {
    KafkaSink.debugStream(ds, whichProvider)
    ds
      .toDF() //@TODO see if we can use directly the Dataset object
      .writeStream
      .format(s"providers.SinkProvider$whichProvider")
      .outputMode("update")
      .queryName(s"KafkaStreamToPipeline$whichProvider")
      .start()
  }
}
