package com.mantix4.ap.core.logs.NetworkProtocols

import com.mantix4.ap.abstracts.base.{LogBase, Sources}
import org.apache.spark.sql.types.{ArrayType, StringType, StructType}

case class SMTP (
              timestamp: String,
              uid: String,
              source_ip: String,
              source_port: Option[Int],
              dest_ip: String,
              dest_port: Option[Int],
              proto: String,
              service: String,
              direction: String,
              duration: Option[Double],
              orig_bytes: Option[Double],
              resp_bytes: Option[Double],
              conn_state: String,
              local_orig: Option[Boolean],
              local_resp: Option[Boolean],
              missed_bytes: Option[Double],
              history: String,
              orig_pkts: Option[Double],
              orig_ip_bytes: Option[Double],
              resp_pkts: Option[Double],
              resp_ip_bytes: Option[Double],
              tunnel_parents: Option[Vector[String]],
              pcr: Option[Double]
             ) extends LogBase {

  override val stream_source: Sources.Value = Sources.KAFKA

  // val schemaBase: StructType = Encoders.product[Conn].schema

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", StringType)
    .add("dest_ip", StringType)
    .add("dest_port", StringType)
    .add("proto", StringType)
    .add("service", StringType)
    .add("duration", StringType)
    .add("orig_bytes", StringType)
    .add("resp_bytes", StringType)
    .add("conn_state", StringType)
    .add("local_orig", StringType)
    .add("local_resp", StringType)
    .add("missed_bytes", StringType)
    .add("history", StringType)
    .add("orig_pkts", StringType)
    .add("orig_ip_bytes", StringType)
    .add("resp_pkts", StringType)
    .add("resp_ip_bytes", StringType)
    .add("tunnel_parents", ArrayType(StringType))

    .add("orig_l2_addr", StringType)
    .add("resp_l2_addr", StringType)

    .add("sensor", StringType)
    .add("type", StringType)

  /*


    */
}
