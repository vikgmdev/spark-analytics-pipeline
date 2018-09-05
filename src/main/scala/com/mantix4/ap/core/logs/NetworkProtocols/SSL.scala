package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object SSL {
  case class SSL (
            timestamp: String,
            uid: String,
            source_ip: String,
            source_port: Option[Int],
            dest_ip: String,
            dest_port: Option[Int],
            version_num: Option[Int],
            version: String,
            cipher: String,
            curve: String,
            server_name: String,
            session_id: String,
            resumed: Option[Boolean],
            client_ticket_empty_session_seen: Option[Boolean],
            client_key_exchange_seen: Option[Boolean],
            server_appdata: Option[Int],
            client_appdata: Option[Boolean],
            last_alert: String,
            next_protocol: String,
            analyzer_id: Option[Int],
            established: Option[Boolean],
            logged: Option[Boolean],
            cert_chain_fuids: Option[Vector[String]],
            issuer: String,
            subject: String,
            client_issuer: String,
            client_subject: String,
            client_cert_chain_fuids: Option[Vector[String]],
            notary_last_seen: Option[Int],
            notary_valid: Option[Boolean],
            notary_first_seen: Option[Int],
            notary_times_seen: Option[Int],
            sensor: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("version_num", IntegerType)
    .add("version", StringType)
    .add("cipher", StringType)
    .add("curve", StringType)
    .add("server_name", StringType)
    .add("session_id", StringType)
    .add("resumed", BooleanType)
    .add("client_ticket_empty_session_seen", BooleanType)
    .add("client_key_exchange_seen", BooleanType)
    .add("server_appdata", IntegerType)
    .add("client_appdata", BooleanType)
    .add("last_alert", StringType)
    .add("next_protocol", StringType)
    .add("analyzer_id", IntegerType)
    .add("established", BooleanType)
    .add("logged", BooleanType)
    .add("cert_chain_fuids", ArrayType(StringType))
    .add("issuer", StringType)
    .add("subject", StringType)
    .add("client_issuer", StringType)
    .add("client_subject", StringType)
    .add("client_cert_chain_fuids", ArrayType(StringType))
    .add("notary.last_seen", IntegerType)
    .add("notary.valid", BooleanType)
    .add("notary.first_seen", IntegerType)
    .add("notary.times_seen", IntegerType)
    .add("sensor", StringType)
}








