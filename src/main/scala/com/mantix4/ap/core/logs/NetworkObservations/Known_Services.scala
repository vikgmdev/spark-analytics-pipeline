package com.mantix4.ap.core.logs.NetworkObservations

import org.apache.spark.sql.types._

object Known_Services {
  case class Known_Services (
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
    .add("port_num", IntegerType)
    .add("port_proto", StringType)
    .add("service", ArrayType(StringType))
    .add("sensor", StringType)
}
