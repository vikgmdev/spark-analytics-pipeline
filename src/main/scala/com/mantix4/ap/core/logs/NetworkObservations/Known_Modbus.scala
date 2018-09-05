package com.mantix4.ap.core.logs.NetworkObservations

import org.apache.spark.sql.types._

object Known_Modbus {
  case class Known_Modbus (
                    timestamp: String,
                    host: String,
                    device_type: String,
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("host", StringType)
    .add("device_type", StringType)
    .add("sensor", StringType)
}
