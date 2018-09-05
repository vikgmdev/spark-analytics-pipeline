package com.mantix4.ap.core.logs.NetworkObservations

import org.apache.spark.sql.types._

object Known_Hosts {
  case class Known_Hosts (
                    timestamp: String,
                    host: String,
                    port_num: Option[Int],
                    port_proto: String,
                    service: Option[Vector[String]],
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("host", StringType)
    .add("sensor", StringType)
}
