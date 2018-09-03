package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types.{ArrayType, StringType, StructType}

case class SSL (
                        timestamp: String,
                        uid: String,
                        source_ip: String,
                        source_port: Option[Int],
                        dest_ip: String,
                        dest_port: Option[Int],
                        version: String,
                        cipher: String,
                        curve: String,
                        server_name: String,
                        resumed: Option[Boolean],
                        last_alert: String,
                        next_protocol: String,
                        established: Option[Boolean],
                        cert_chain_fuids: Vector[String],
                        client_cert_chain_fuids: Vector[String],
                        subject: String,
                        issuer: String,
                        client_subject: String,
                        client_issuer: String,
                        validation_status: String,
                        cyrin_rule: String,
                        cyrin_id: String,
                        cyrin_child: String,
                        cyrin_severity: String,
                        cyrin_confidence: String,
                        cyrin_class: String,
                        cyrin_type: String,
                        cyrin_stage: String
                    ) extends LogBase {

  override val stream_source: Sources.Value = Sources.KAFKA

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", StringType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", StringType)
    .add("version", StringType)
    .add("cipher", StringType)
    .add("curve", StringType)
    .add("server_name", StringType)
    .add("resumed", StringType)
    .add("last_alert", StringType)
    .add("next_protocol", StringType)
    .add("established", StringType)
    .add("cert_chain_fuids", ArrayType(StringType))
    .add("client_cert_chain_fuids", ArrayType(StringType))
    .add("subject", StringType)
    .add("issuer", StringType)
    .add("client_subject", StringType)
    .add("client_issuer", StringType)
    .add("validation_status", StringType)
    .add("cyrin_rule", StringType)
    .add("cyrin_id", StringType)
    .add("cyrin_child", StringType)
    .add("cyrin_severity", StringType)
    .add("cyrin_confidence", StringType)
    .add("cyrin_class", StringType)
    .add("cyrin_type", StringType)
    .add("cyrin_stage", StringType)
}
