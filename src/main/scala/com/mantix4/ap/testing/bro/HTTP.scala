package com.mantix4.ap.testing.bro

import com.mantix4.ap.base.LogBase
import org.apache.spark.sql.types._

object HTTP extends LogBase {

  override val topicName: String = "http"

  val cassandraTable = "http"

  case class Simple (
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
                      user_agent: String,
                      request_body_len: Option[Double],
                      response_body_len: Option[Double],
                      status_code: Option[Double],
                      status_msg: String,
                      info_code: Option[Double],
                      info_msg: String,
                      filename: String,
                      tags: Vector[String],
                      username: String,
                      password: String,
                      proxied: Vector[String],
                      orig_fuids: Vector[String],
                      orig_mime_types: Vector[String],
                      resp_fuids: Vector[String],
                      resp_mime_types: Vector[String],
                      cyrin_rule: String,
                      cyrin_id: String,
                      cyrin_child: String,
                      cyrin_severity: String,
                      cyrin_confidence: String,
                      cyrin_class: String,
                      cyrin_type: String,
                      cyrin_stage: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", StringType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", StringType)
    .add("trans_depth", StringType)
    .add("method", StringType)
    .add("host", StringType)
    .add("uri", StringType)
    .add("referrer", StringType)
    .add("user_agent", StringType)
    .add("request_body_len", StringType)
    .add("response_body_len", StringType)
    .add("status_code", StringType)
    .add("status_msg", StringType)
    .add("info_code", StringType)
    .add("info_msg", StringType)
    .add("filename", StringType)
    .add("tags", ArrayType(StringType))
    .add("username", StringType)
    .add("password", StringType)
    .add("proxied", ArrayType(StringType))
    .add("orig_fuids", ArrayType(StringType))
    .add("orig_mime_types", ArrayType(StringType))
    .add("resp_fuids", ArrayType(StringType))
    .add("resp_mime_types", ArrayType(StringType))
    .add("cyrin_rule", StringType)
    .add("cyrin_id", StringType)
    .add("cyrin_child", StringType)
    .add("cyrin_severity", StringType)
    .add("cyrin_confidence", StringType)
    .add("cyrin_class", StringType)
    .add("cyrin_type", StringType)
    .add("cyrin_stage", StringType)
}



