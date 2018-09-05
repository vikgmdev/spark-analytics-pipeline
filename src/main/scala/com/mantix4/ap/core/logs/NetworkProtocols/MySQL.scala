package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object MySQL {
  case class MySQL (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      cmd: String,
                      arg: String,
                      success: Option[Boolean],
                      rows: Option[Int],
                      response: String,
                      sensor: String
                ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("cmd", StringType)
    .add("arg", StringType)
    .add("success", BooleanType)
    .add("rows", IntegerType)
    .add("response", StringType)
    .add("sensor", StringType)
}