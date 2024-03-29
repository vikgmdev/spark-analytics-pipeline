package com.mantix4.ap.core.logs.NetworkObservations

import org.apache.spark.sql.types._

object Known_Devices {
  case class Known_Devices (
                    timestamp: String,
                    mac: String,
                    dhcp_host_name: String,
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("mac", StringType)
    .add("dhcp_host_name", IntegerType)
    .add("sensor", StringType)
}
