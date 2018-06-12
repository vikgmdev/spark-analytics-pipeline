package com.mantix4.ap.base

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.execution.streaming.Sink

trait SinkBase extends Sink {

  def startPipeline(df: DataFrame): Unit

  override def addBatch(batchId: Long, df: DataFrame): Unit = {
    this.startPipeline(df)
  }
}