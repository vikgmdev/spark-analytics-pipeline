package com.mantix4.ap.abstracts.base

import org.apache.spark.sql.{DataFrame}
import org.apache.spark.sql.execution.streaming.Sink

trait SinkBase extends Sink {

  override def addBatch(batchId: Long, df: DataFrame): Unit
}