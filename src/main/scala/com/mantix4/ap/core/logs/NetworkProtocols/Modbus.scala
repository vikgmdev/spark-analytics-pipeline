package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object Modbus {
  case class Modbus (
                    timestamp: String,
                    uid: String,
                    source_ip: String,
                    source_port: Option[Int],
                    dest_ip: String,
                    dest_port: Option[Int],
                    func: String,
                    exception: String,
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("func", StringType)
    .add("exception", StringType)
    // .add("track_address", IntegerType) //(present if policy/protocols/modbus/track-memmap.bro is loaded)
    .add("sensor", StringType)
}