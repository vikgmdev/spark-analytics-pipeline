package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object IRC {
  case class IRC (
                    timestamp: String,
                    uid: String,
                    source_ip: String,
                    source_port: Option[Int],
                    dest_ip: String,
                    dest_port: Option[Int],
                    nick: String,
                    user: String,
                    command: String,
                    value: String,
                    addl: String,
                    dcc_file_name: String,
                    dcc_file_size: Option[Int],
                    dcc_mime_type: String,
                    fuid: String,
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("nick", StringType)
    .add("user", StringType)
    .add("command", StringType)
    .add("value", StringType)
    .add("addl", StringType)
    .add("dcc_file_name", StringType)
    .add("dcc_file_size", IntegerType)
    .add("dcc_mime_type", StringType)
    .add("fuid", StringType)
    .add("sensor", StringType)
}