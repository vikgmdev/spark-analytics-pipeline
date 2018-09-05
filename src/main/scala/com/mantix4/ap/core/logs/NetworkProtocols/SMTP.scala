package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object SMTP {
  case class SMTP (
                    timestamp: String,
                    uid: String,
                    source_ip: String,
                    source_port: Option[Int],
                    dest_ip: String,
                    dest_port: Option[Int],
                    cc: Option[Vector[String]],
                    first_received: String,
                    msg_id: String,
                    x_originating_ip: String,
                    helo: String,
                    subject: String,
                    trans_depth: Option[Int],
                    from: String,
                    to: Option[Vector[String]],
                    is_webmail: Option[Boolean],
                    fuids: Option[Vector[String]],
                    last_reply: String,
                    date: String,
                    path: Option[Vector[String]],
                    in_reply_to: String,
                    tls: Option[Boolean],
                    process_received_from: Option[Boolean],
                    has_client_activity: Option[Boolean],
                    mailfrom: String,
                    second_received: String,
                    rcptto: Option[Vector[String]],
                    user_agent: String,
                    reply_to: String,
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("cc", ArrayType(StringType))
    .add("first_received", StringType)
    .add("msg_id", StringType)
    .add("x_originating_ip", StringType)
    .add("helo", StringType)
    .add("subject", StringType)
    .add("trans_depth", IntegerType)
    .add("from", StringType)
    .add("to", ArrayType(StringType))
    .add("is_webmail", BooleanType)
    .add("fuids", ArrayType(StringType))
    .add("last_reply", StringType)
    .add("date", StringType)
    .add("path", ArrayType(StringType))
    .add("in_reply_to", StringType)
    .add("tls", BooleanType)
    .add("process_received_from", BooleanType)
    .add("has_client_activity", BooleanType)
    .add("mailfrom", StringType)
    .add("second_received", StringType)
    .add("rcptto", ArrayType(StringType))
    .add("user_agent", StringType)
    .add("reply_to", StringType)
    .add("sensor", StringType)
}