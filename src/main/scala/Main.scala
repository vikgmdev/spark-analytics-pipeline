
import production.bro._
import kafka.{KafkaSink, KafkaSource}
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{Dataset, Row}
import spark.SparkHelper

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
      .format(s"base.SinkProvider")
      .option("pipeline", s"production.pipelines.Pipeline$withProvider")
      .outputMode(OutputMode.Update())
      .queryName(s"KafkaStreamToPipeline$withProvider")
      .start()
  }
}
