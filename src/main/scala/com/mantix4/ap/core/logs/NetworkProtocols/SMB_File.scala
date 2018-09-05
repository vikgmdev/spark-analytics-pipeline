package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object SMB_File {
  case class SMB_File (
              timestamp: String,
              uid: String,
              source_ip: String,
              source_port: Option[Int],
              dest_ip: String,
              dest_port: Option[Int],
              action: String,
              fuid: String,
              prev_name: String,
              name: String,
              times_accessed: String,
              times_created: String,
              times_changed: String,
              times_modified: String,
              path:String,
              size: Option[Int],
              sensor:String
             ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("action", StringType)
    .add("fuid", StringType)
    .add("prev_name", StringType)
    .add("name", StringType)
    .add("times_accessed", StringType)
    .add("times_created", StringType)
    .add("times_changed", StringType)
    .add("times_modified", StringType)
    .add("path",StringType)
    .add("size", IntegerType)
    .add("sensor", StringType)
}