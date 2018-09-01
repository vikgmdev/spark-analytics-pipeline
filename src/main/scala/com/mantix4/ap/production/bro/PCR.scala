package com.mantix4.ap.production.bro

import com.mantix4.ap.base.LogBase
import com.datastax.spark.connector.SomeColumns
import org.apache.spark.sql.types._

object PCR extends LogBase {

  override val topicName: String = "pcr"

  val cassandraTable = "pcr"

  case class Simple (
                      timestamp: String,
                      src: String,
                      pcr: Option[Double],
                      summary_interval: Option[Double],
                      sensor: String
                 ) extends Serializable

  val cassandraColumns = SomeColumns(
    "timestamp",
    "src",
    "pcr",
    "summary_interval"
  )

  val schemaBase: StructType = new StructType()
    .add("timestamp",  StringType)
    .add("src", StringType)
    .add("pcr", DoubleType)
    .add("summary_interval", DoubleType)
    .add("sensor", StringType)
    .add("type", StringType)

}

/*
CREATE TABLE pcr (
  timestamp timestamp PRIMARY KEY,
  src inet,
  pcr double,
  summary_interval int
);
*/
