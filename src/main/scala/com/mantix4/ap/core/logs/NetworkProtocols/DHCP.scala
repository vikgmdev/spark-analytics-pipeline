package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object DHCP {
  case class DHCP (
                       timestamp: String,
                       uid: String,
                       source_ip: String,
                       source_port: Option[Int],
                       dest_ip: String,
                       dest_port: Option[Int],

                       mac: String,
                       assigned_ip: String,
                       lease_time: Option[Double],
                       trans_id: Option[Int],
                       sensor: String
                     ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", StringType)
    .add("dest_ip", StringType)
    .add("dest_port", StringType)
    .add("mac", StringType)
    .add("assigned_ip", StringType)
    .add("lease_time", DoubleType)
    .add("trans_id", IntegerType)
    .add("sensor", StringType)

  /*
  .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", IntegerType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", IntegerType)
   */
}
