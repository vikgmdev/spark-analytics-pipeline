package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object RFB {
  case class RFB (
                    timestamp: String,
                    uid: String,
                    source_ip: String,
                    source_port: Option[Int],
                    dest_ip: String,
                    dest_port: Option[Int],
                    client_major_version: String,
                    client_minor_version: String,
                    server_major_version: String,
                    server_minor_version: String,
                    authentication_method: String,
                    auth: Option[Boolean],
                    share_flag: Option[Boolean],
                    desktop_name: String,
                    width: Option[Int],
                    height: Option[Int],
                    done: Option[Boolean],
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("client_major_version", StringType)
    .add("client_minor_version", StringType)
    .add("server_major_version", StringType)
    .add("server_minor_version", StringType)
    .add("authentication_method", StringType)
    .add("auth", BooleanType)
    .add("share_flag", BooleanType)
    .add("desktop_name", StringType)
    .add("width", IntegerType)
    .add("height", IntegerType)
    .add("done", BooleanType)
    .add("sensor", StringType)
}