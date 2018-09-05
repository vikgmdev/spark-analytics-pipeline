package com.mantix4.ap.core.logs.NetworkObservations

import org.apache.spark.sql.types._

object Software {
  case class Software (
                    timestamp: String,
                    host: String,
                    host_p: Option[Int],
                    software_type: String,
                    name: String,
                    version_major: Option[Int],
                    version_minor: Option[Int],
                    version_minor2: Option[Int],
                    version_minor3: Option[Int],
                    version_addl: String,
                    unparsed_version: String,
                    force_log: Option[Boolean],
                    url: String,
                    sensor: String
                  ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("host", StringType)
    .add("host_p", IntegerType)
    .add("software_type", StringType)
    .add("name", StringType)
    .add("version.major", IntegerType)
    .add("version.minor", IntegerType)
    .add("version.minor2", IntegerType)
    .add("version.minor3", IntegerType)
    .add("version.addl", StringType)
    .add("unparsed_version", StringType)
    .add("force_log", BooleanType)
    .add("url", StringType) //(present if policy/protocols/http/detect-webapps.bro is loaded)
    .add("sensor", StringType)
}