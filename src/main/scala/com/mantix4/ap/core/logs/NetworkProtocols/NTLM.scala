package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object NTLM {
  case class NTLM (
              timestamp: String,
              uid: String,
              source_ip: String,
              source_port: Option[Int],
              dest_ip: String,
              dest_port: Option[Int],
              username: String,
              hostname: String,
              domainname: String,
              success: Option[Boolean],
              status: String,
              done: Option[Boolean],
              sensor: String
             ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("username", StringType)
    .add("hostname", StringType)
    .add("domainname", StringType)
    .add("success", BooleanType)
    .add("status", StringType)
    .add("done", BooleanType)
    .add("sensor", StringType)
}