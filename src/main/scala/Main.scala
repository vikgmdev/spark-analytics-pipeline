
import production.bro._
import kafka.{KafkaSink, KafkaSource}
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{Dataset, Row}
import spark.SparkHelper

object Main {

  def main(args: Array[String]) {
    val spark = SparkHelper.getAndConfigureSparkSession()

    startNewPipeline(KafkaSource.read(Conn.topicName), "Conn")

    startNewPipeline(KafkaSource.read(DNS.topicName), "DNS")

    startNewPipeline(KafkaSource.read(PCR.topicName), "PCR")

    //Wait for all streams to finish
    spark.streams.awaitAnyTermination()
  }

  def startNewPipeline(ds: Dataset[Row], whichProvider: String): StreamingQuery = {
    KafkaSink.debugStream(ds, whichProvider)
    ds
      .toDF()
      .writeStream
      .format(s"base.SinkProvider")
      .option("pipeline", s"production.pipelines.Pipeline$whichProvider")
      .outputMode(OutputMode.Update())
      .queryName(s"KafkaStreamToPipeline$whichProvider")
      .start()
  }
}
