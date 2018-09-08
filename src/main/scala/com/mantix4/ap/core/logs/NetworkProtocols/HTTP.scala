package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types._

object HTTP {
  case class HTTP (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      trans_depth: Option[Int],
                      method: String,
                      host: String,
                      uri: String,
                      referrer: String,
                      version: String,
                      user_agent: String,
                      request_body_len: Option[Int],
                      response_body_len: Option[Int],
                      status_code: Option[Int],
                      status_msg: String,
                      info_code: Option[Int],
                      info_msg: String,
                      tags: Option[Vector[String]],
                      username: String,
                      password: String,
                      capture_password: Option[Boolean],
                      proxied: Option[Vector[String]],
                      range_request: Option[Boolean],
                      orig_fuids: Option[Vector[String]],
                      orig_filenames: Option[Vector[String]],
                      orig_mime_types: Option[Vector[String]],
                      resp_fuids: Option[Vector[String]],
                      resp_filenames: Option[Vector[String]],
                      resp_mime_types: Option[Vector[String]],
                      current_entity: String,
                      orig_mime_depth: Option[Int],
                      resp_mime_depth: Option[Int],
                      sensor: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("trans_depth", IntegerType)
    .add("method", StringType)
    .add("host", StringType)
    .add("uri", StringType)
    .add("referrer", StringType)
    .add("version", StringType)
    .add("user_agent", StringType)
    .add("request_body_len", IntegerType)
    .add("response_body_len", IntegerType)
    .add("status_code", IntegerType)
    .add("status_msg", StringType)
    .add("info_code", IntegerType)
    .add("info_msg", StringType)
    // .add("tags", ArrayType(StringType))
    .add("tags", StringType)
    .add("username", StringType)
    .add("password", StringType)
    .add("capture_password", BooleanType)
    // .add("proxied", ArrayType(StringType))
    .add("proxied", StringType)
    .add("range_request", BooleanType)
    // (present if base/protocols/http/entities.bro is loaded)
    // .add("orig_fuids", ArrayType(StringType))
    .add("orig_fuids", StringType)
    // .add("orig_filenames", ArrayType(StringType))
    .add("orig_filenames", StringType)
    // .add("orig_mime_types", ArrayType(StringType))
    .add("orig_mime_types", StringType)
    // .add("resp_fuids", ArrayType(StringType))
    .add("resp_fuids", StringType)
    // .add("resp_filenames", ArrayType(StringType))
    .add("resp_filenames", StringType)
    // .add("resp_mime_types", ArrayType(StringType))
    .add("resp_mime_types", StringType)
    .add("current_entity", StringType)
    .add("orig_mime_depth", IntegerType)
    .add("resp_mime_depth", IntegerType)
    //------
    .add("sensor", StringType)
}
