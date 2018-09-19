package com.mantix4.ap.abstracts.base

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.StreamSinkProvider
import org.apache.spark.sql.streaming.OutputMode

/**
From Holden Karau's High Performance Spark
  https://github.com/holdenk/com.mantix4.ap.abstracts.spark-structured-streaming-ml/blob/master/src/main/scala/com/high-performance-com.mantix4.ap.abstracts.spark-examples/structuredstreaming/CustomSink.scala#L66
  *
  */
class SinkProvider extends StreamSinkProvider {
  override def createSink(sqlContext: SQLContext,
                          parameters: Map[String, String],
                          partitionColumns: Seq[String],
                          outputMode: OutputMode): SinkBase = {
    println(parameters)
    println(partitionColumns)
    val className: String = parameters("pipeline")
    Class.forName(className).newInstance().asInstanceOf[SinkBase]
  }
}
