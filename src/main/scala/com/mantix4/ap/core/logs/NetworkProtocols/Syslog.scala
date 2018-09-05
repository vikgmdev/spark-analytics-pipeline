package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object Syslog {
  case class Syslog (
                        timestamp: String,
                        uid: String,
                        source_ip: String,
                        source_port: Option[Int],
                        dest_ip: String,
                        dest_port: Option[Int],
                        proto: String,
                        facility: String,
                        severity: String,
                        message: String,
                          sensor: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("proto", StringType)
    .add("facility", StringType)
    .add("severity", StringType)
    .add("message", StringType)
    .add("sensor", StringType)
}
