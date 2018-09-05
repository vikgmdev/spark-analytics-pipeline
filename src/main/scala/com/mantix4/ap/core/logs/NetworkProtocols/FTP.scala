package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types._

object FTP {
  case class FTP (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      user: String,
                      password: String,
                      command: String,
                      arg: String,
                      mime_type: String,
                      file_size: Option[Int],
                      reply_code: Option[Int],
                      reply_msg: String,
                      data_channel_passive: Option[Boolean],
                      data_channel_orig_h: String,
                      data_channel_resp_h: String,
                      data_channel_resp_p: Option[Int],
                      cwd: String,
                      passive: Option[Boolean],
                      capture_password: Option[Boolean],
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
    .add("user", StringType)
    .add("password", StringType)
    .add("command", StringType)
    .add("arg", StringType)
    .add("mime_type", StringType)
    .add("file_size", IntegerType)
    .add("reply_code", IntegerType)
    .add("reply_msg", StringType)
    .add("data_channel.passive", BooleanType)
    .add("data_channel.orig_h", StringType)
    .add("data_channel.resp_h", StringType)
    .add("data_channel.resp_p", IntegerType)
    .add("cwd", StringType)
    //.add("cmdarg", StringType)
    //.add("pending_commands", StringType)
    .add("passive", BooleanType)
    .add("capture_password", BooleanType)
    .add("fuid", StringType) // (present if base/protocols/ftp/files.bro is loaded)
    .add("sensor", StringType)
}