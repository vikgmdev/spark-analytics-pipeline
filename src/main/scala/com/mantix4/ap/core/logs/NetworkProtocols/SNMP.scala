package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object SNMP {
  case class SNMP (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      duration: Option[Double],
                      version: String,
                      community: String,
                      get_requests: Option[Int],
                      get_bulk_requests: Option[Int],
                      get_responses: Option[Int],
                      set_requests: Option[Int],
                      display_string: String,
                      up_since: String,
                      sensor: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("duration", DoubleType)
    .add("version", StringType)
    .add("community", StringType)
    .add("get_requests", IntegerType)
    .add("get_bulk_requests", IntegerType)
    .add("get_responses", IntegerType)
    .add("set_requests", IntegerType)
    .add("display_string", StringType)
    .add("up_since", StringType)
    .add("sensor", StringType)
}
