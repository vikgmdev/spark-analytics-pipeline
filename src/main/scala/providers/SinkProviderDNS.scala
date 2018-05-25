package providers

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.StreamSinkProvider
import org.apache.spark.sql.streaming.OutputMode
import pipelines.PipelineDNS

/**
From Holden Karau's High Performance Spark
  https://github.com/holdenk/spark-structured-streaming-ml/blob/master/src/main/scala/com/high-performance-spark-examples/structuredstreaming/CustomSink.scala#L66
  *
  */
class SinkProviderDNS extends StreamSinkProvider {
  override def createSink(sqlContext: SQLContext,
                          parameters: Map[String, String],
                          partitionColumns: Seq[String],
                          outputMode: OutputMode): PipelineDNS = {
    new PipelineDNS()
  }
}
