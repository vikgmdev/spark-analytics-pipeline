package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types.{StringType, StructType}

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
                      get_requests: Option[Double],
                      get_bulk_requests: Option[Double],
                      get_responses: Option[Double],
                      set_requests: Option[Double],
                      display_string: String,
                      up_since: String
                    ) extends LogBase {

  override val stream_source: Sources.Value = Sources.KAFKA

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", StringType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", StringType)
    .add("duration", StringType)
    .add("version", StringType)
    .add("community", StringType)
    .add("get_requests", StringType)
    .add("get_bulk_requests", StringType)
    .add("get_responses", StringType)
    .add("set_requests", StringType)
    .add("display_string", StringType)
    .add("up_since", StringType)
}
