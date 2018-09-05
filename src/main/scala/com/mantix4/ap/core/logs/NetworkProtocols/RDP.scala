package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object RDP {
  case class RDP (
                    timestamp: String,
                    uid: String,
                    source_ip: String,
                    source_port: Option[Int],
                    dest_ip: String,
                    dest_port: Option[Int],
                    cookie: String,
                    result: String,
                    security_protocol: String,
                    keyboard_layout: String,
                    client_build: String,
                    client_name: String,
                    client_dig_product_id: String,
                    desktop_width: Option[Int],
                    desktop_height: Option[Int],
                    requested_color_depth: String,
                    cert_type: String,
                    cert_count: Option[Int],
                    cert_permanent: Option[Boolean],
                    encryption_level: String,
                    encryption_method: String,
                    analyzer_id: Option[Int],
                    done: Option[Boolean],
                    ssl: Option[Boolean],
                    sensor: String
                  ) extends Serializable

  // val schemaBase: StructType = Encoders.product[Conn].schema

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("cookie", StringType)
    .add("result", StringType)
    .add("security_protocol", StringType)
    .add("keyboard_layout", StringType)
    .add("client_build", StringType)
    .add("client_name", StringType)
    .add("client_dig_product_id", StringType)
    .add("desktop_width", IntegerType)
    .add("desktop_height", IntegerType)
    .add("requested_color_depth", StringType)
    .add("cert_type", StringType)
    .add("cert_count", IntegerType)
    .add("cert_permanent", BooleanType)
    .add("encryption_level", StringType)
    .add("encryption_method", StringType)
    .add("analyzer_id", IntegerType)
    .add("done", BooleanType)
    .add("ssl", BooleanType) //(present if policy/protocols/rdp/indicate_ssl.bro is loaded)
    .add("sensor", StringType)
}