package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types._

object Kerberos {
  case class Kerberos (
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
                      error_code: Option[Int],
                      error_msg: String,
                      from: String,
                      till: String,
                      cipher: String,
                      forwardable: Option[Boolean],
                      renewable: Option[Boolean],
                      logged: Option[Boolean],
                      client_cert_subject: String,
                      client_cert_fuid: String,
                      server_cert_subject: String,
                      server_cert_fuid: String,
                      sensor: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("request_type", StringType)
    .add("client", StringType)
    .add("service", StringType)
    .add("success", BooleanType)
    .add("error_code", IntegerType)
    .add("error_msg", StringType)
    .add("from", StringType)
    .add("till", StringType)
    .add("cipher", StringType)
    .add("forwardable", BooleanType)
    .add("renewable", BooleanType)
    .add("logged", BooleanType)
    .add("client_cert_subject", StringType)
    .add("client_cert_fuid", StringType)
    .add("server_cert_subject", StringType)
    .add("server_cert_fuid", StringType)
    .add("sensor", StringType)
}