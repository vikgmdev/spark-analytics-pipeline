package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types.{ArrayType, StringType, StructType}

case class HTTP (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      trans_depth: Option[Double],
                      method: String,
                      host: String,
                      uri: String,
                      referrer: String,
                      version: String,
                      user_agent: String,
                      request_body_len: Option[Double],
                      response_body_len: Option[Double],
                      status_code: Option[Double],
                      status_msg: String,
                      info_code: Option[Double],
                      info_msg: String,
                      tags: Vector[String],
                      username: String,
                      password: String,
                      capture_password: Option[Boolean],
                      proxied: Vector[String],
                      orig_fuids: Vector[String],
                      orig_filenames: Vector[String],
                      orig_mime_types: Vector[String],
                      resp_fuids: Vector[String],
                      resp_filenames: Vector[String],
                      resp_mime_types: Vector[String]
                    ) extends LogBase {

  override val stream_source: Sources.Value = Sources.KAFKA

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", StringType)
    .add("dest_ip", StringType)
    .add("dest_port", StringType)
    .add("trans_depth", StringType)
    .add("method", StringType)
    .add("host", StringType)
    .add("uri", StringType)
    .add("referrer", StringType)
    .add("version", StringType)
    .add("user_agent", StringType)
    .add("request_body_len", StringType)
    .add("response_body_len", StringType)
    .add("status_code", StringType)
    .add("status_msg", StringType)
    .add("info_code", StringType)
    .add("info_msg", StringType)
    .add("tags", ArrayType(StringType))
    .add("username", StringType)
    .add("password", StringType)
    .add("capture_password", StringType)
    .add("range_request", StringType)
    .add("proxied", ArrayType(StringType))
    .add("range_request", StringType)
    .add("orig_fuids", ArrayType(StringType))
    .add("orig_filenames", ArrayType(StringType))
    .add("orig_mime_types", ArrayType(StringType))
    .add("resp_fuids", ArrayType(StringType))
    .add("resp_filenames", ArrayType(StringType))
    .add("resp_mime_types", ArrayType(StringType))
}
