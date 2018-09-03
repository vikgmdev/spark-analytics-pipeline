package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types.{StringType, StructType}

case class IRC (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      request_type: String,
                      client: String,
                      service: String,
                      success: Option[Boolean],
                      error_msg: String,
                      from: String,
                      till: String,
                      cipher: String,
                      forwardable: Option[Boolean],
                      renewable: Option[Boolean],
                      client_cert_subject: String,
                      client_cert_fuid: String,
                      server_cert_subject: String,
                      server_cert_fuid: String
                    ) extends LogBase {

  override val stream_source: Sources.Value = Sources.KAFKA

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", StringType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", StringType)
    .add("request_type", StringType)
    .add("client", StringType)
    .add("service", StringType)
    .add("success", StringType)
    .add("error_msg", StringType)
    .add("from", StringType)
    .add("till", StringType)
    .add("cipher", StringType)
    .add("forwardable", StringType)
    .add("renewable", StringType)
    .add("client_cert_subject", StringType)
    .add("client_cert_fuid", StringType)
    .add("server_cert_subject", StringType)
    .add("server_cert_fuid", StringType)
}
