package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types._

object SMB_Cmd {
  case class SMB_Cmd (
              timestamp: String,
              uid: String,
              source_ip: String,
              source_port: Option[Int],
              dest_ip: String,
              dest_port: Option[Int],
              command: String,
              sub_command: String,
              argument: String,
              status: String,
              rtt: Option[Double],
              version: String,
              username: String,
              tree: String,
              tree_service: String,
              referenced_file_action: String,
              referenced_file_id_resp_p: Option[Int],
              referenced_file_fuid: String,
              referenced_file_prev_name: String,
              referenced_file_name: String,
              referenced_file_times_accessed: String,
              referenced_file_id_resp_h: String,
              referenced_file_uid: String,
              referenced_file_id_orig_h: String,
              referenced_file_times_created: String,
              referenced_file_times_changed: String,
              referenced_file_ts: String,
              referenced_file_id_orig_p: Option[Int],
              referenced_file_times_modified: String,
              referenced_file_path:String,
              referenced_file_size: Option[Int],
              sensor: String
             ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("command", StringType)
    .add("sub_command", StringType)
    .add("argument", StringType)
    .add("status", StringType)
    .add("rtt", DoubleType)
    .add("version", StringType)
    .add("username", StringType)
    .add("tree", StringType)
    .add("tree_service", StringType)
    .add("referenced_file.action", StringType)
    .add("referenced_file.id.resp_p", IntegerType)
    .add("referenced_file.fuid", StringType)
    .add("referenced_file.prev_name", StringType)
    .add("referenced_file.name", StringType)
    .add("referenced_file.times.accessed", StringType)
    .add("referenced_file.id.resp_h", StringType)
    .add("referenced_file.uid", StringType)
    .add("referenced_file.id.orig_h", StringType)
    .add("referenced_file.times.created", StringType)
    .add("referenced_file.times.changed", StringType)
    .add("referenced_file.ts", StringType)
    .add("referenced_file.id.orig_p", IntegerType)
    .add("referenced_file.times.modified", StringType)
    .add("referenced_file.path",StringType)
    .add("referenced_file.size", IntegerType)
    .add("sensor", StringType)
}