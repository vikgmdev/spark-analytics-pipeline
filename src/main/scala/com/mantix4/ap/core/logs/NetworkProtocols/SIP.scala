package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object SIP {

  case class SIP(
                  timestamp: String,
                  uid: String,
                  source_ip: String,
                  source_port: Option[Int],
                  dest_ip: String,
                  dest_port: Option[Int],
                  trans_depth: IntegerType,
                  method: String,
                  uri: String,
                  date: String,
                  request_from: String,
                  request_to: String,
                  response_from: String,
                  response_to: String,
                  reply_to: String,
                  call_id: String,
                  seq: String,
                  subject: String,
                  request_path: Option[Vector[String]],
                  response_path: Option[Vector[String]],
                  user_agent: String,
                  status_code: Option[Int],
                  status_msg: String,
                  warning: String,
                  request_body_len: Option[Int],
                  response_body_len: Option[Int],
                  content_type: String,
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
    .add("uri", StringType)
    .add("date", StringType)
    .add("request_from", StringType)
    .add("request_to", StringType)
    .add("response_from", StringType)
    .add("response_to", StringType)
    .add("reply_to", StringType)
    .add("call_id", StringType)
    .add("seq", StringType)
    .add("subject", StringType)
    .add("request_path", ArrayType(StringType))
    .add("response_path", ArrayType(StringType))
    .add("user_agent", StringType)
    .add("status_code", IntegerType)
    .add("status_msg", StringType)
    .add("warning", StringType)
    .add("request_body_len", IntegerType)
    .add("response_body_len", IntegerType)
    .add("content_type", StringType)
    .add("sensor", StringType)
}