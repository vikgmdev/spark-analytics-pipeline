package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types.{ArrayType, StringType, StructType}

object SMB_Tree {
  case class SMB_Tree (
              timestamp: String,
              uid: String,
              source_ip: String,
              source_port: Option[Int],
              dest_ip: String,
              dest_port: Option[Int],
              path: String,
              service: String,
              native_file_system: String,
              share_type: String,
              sensor: String
             ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", StringType)
    .add("dest_ip", StringType)
    .add("dest_port", StringType)
    .add("path", StringType)
    .add("service", StringType)
    .add("native_file_system", StringType)
    .add("share_type", StringType)
    .add("sensor", StringType)
}