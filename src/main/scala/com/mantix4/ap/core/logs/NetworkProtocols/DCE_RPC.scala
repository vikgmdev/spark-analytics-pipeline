package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object DCE_RPC {
  case class DCE_RPC (
              timestamp: String,
              uid: String,
              source_ip: String,
              source_port: Option[Int],
              dest_ip: String,
              dest_port: Option[Int],
              rtt: Option[Double],
              named_pipe: String,
              endpoint: String,
              operation: String,
              sensor: String
             ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", StringType)
    .add("dest_ip", StringType)
    .add("dest_port", StringType)
    .add("rtt", DoubleType)
    .add("named_pipe", StringType)
    .add("endpoint", StringType)
    .add("operation", StringType)
    .add("sensor", StringType)

  /*
  .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", IntegerType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", IntegerType)
   */
}