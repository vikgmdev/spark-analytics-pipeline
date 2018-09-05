package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object DNP3 {
  case class DNP3 (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      fc_request: String,
                      fc_reply: String,
                      iin: Option[Int],
                      sensor: String
                ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("fc_request", StringType)
    .add("fc_reply", StringType)
    .add("iin", IntegerType)
    .add("sensor", StringType)
}