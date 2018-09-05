package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object RADIUS {
  case class RADIUS (
              timestamp: String,
              uid: String,
              source_ip: String,
              source_port: Option[Int],
              dest_ip: String,
              dest_port: Option[Int],
              username: String,
              mac: String,
              framed_addr: String,
              remote_ip: String,
              connect_info: String,
              reply_msg: String,
              result: String,
              ttl: Option[Double],
              logged: Option[Boolean],
              sensor: String
             ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("username", StringType)
    .add("mac", StringType)
    .add("framed_addr", StringType)
    .add("remote_ip", StringType)
    .add("connect_info", StringType)
    .add("reply_msg", StringType)
    .add("result", StringType)
    .add("ttl", DoubleType)
    .add("logged", BooleanType)
    .add("sensor", StringType)
}