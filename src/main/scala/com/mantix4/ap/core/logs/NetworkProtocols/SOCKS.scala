package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object SOCKS {
  case class SOCKS (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      version: Option[Int],
                      user: String,
                      password: String,
                      status: String,
                      request_host: String,
                      request_name: String,
                      request_p: Option[Int],
                      bound_name: Option[Int],
                      bound_host: Option[Int],
                      bound_p: Option[Int],
                      sensor: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("version", IntegerType)
    .add("user", StringType)
    .add("password", StringType)
    .add("status", StringType)
    .add("request_host", StringType)
    .add("request_name", StringType)
    .add("request_p", IntegerType)
    .add("bound_name", IntegerType)
    .add("bound_host", IntegerType)
    .add("bound_p", IntegerType)
    .add("sensor", StringType)
}