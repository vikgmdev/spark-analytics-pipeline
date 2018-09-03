package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types.{StringType, StructType}

case class Tunnel (
                        timestamp: String,
                        uid: String,
                        source_ip: String,
                        source_port: Option[Int],
                        dest_ip: String,
                        dest_port: Option[Int],
                        proto: String,
                        facility: String,
                        severity: String,
                        message: String
                    ) extends LogBase {

  override val stream_source: Sources.Value = Sources.KAFKA

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", StringType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", StringType)
    .add("proto", StringType)
    .add("facility", StringType)
    .add("severity", StringType)
    .add("message", StringType)
}
