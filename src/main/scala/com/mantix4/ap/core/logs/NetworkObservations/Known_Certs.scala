package com.mantix4.ap.core.logs.NetworkObservations

import org.apache.spark.sql.types._

object Known_Certs {
  case class Known_Certs (
                    timestamp: String,
                    host: String,
                    port_num: Option[Int],
                    subject: String,
                    issuer_subject: String,
                    serial: String,
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("host", StringType)
    .add("port_num", IntegerType)
    .add("subject", StringType)
    .add("issuer_subject", StringType)
    .add("serial", StringType)
    .add("sensor", StringType)
}