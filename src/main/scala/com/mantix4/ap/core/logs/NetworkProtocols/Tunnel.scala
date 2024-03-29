package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object Tunnel {
  case class Tunnel (
                    timestamp: String,
                    uid: String,
                    source_ip: String,
                    source_port: Option[Int],
                    dest_ip: String,
                    dest_port: Option[Int],
                    tunnel_type: String,
                    action: String,
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("tunnel_type", StringType)
    .add("action", StringType)
    .add("sensor", StringType)
}