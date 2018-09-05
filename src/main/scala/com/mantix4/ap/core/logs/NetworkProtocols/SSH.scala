package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object SSH {
  case class SSH (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      version: Option[Int],
                      auth_success: Option[Boolean],
                      auth_attempts: Option[Int],
                      direction: String,
                      client: String,
                      server: String,
                      cipher_alg: String,
                      mac_alg: String,
                      compression_alg: String,
                      kex_alg: String,
                      host_key_alg: String,
                      host_key: String,
                      remote_location_country_code: String,
                      remote_location_region: String,
                      remote_location_longitude: Option[Double],
                      remote_location_city: String,
                      remote_location_latitude: Option[Double],
                      sensor: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("version", IntegerType)
    .add("auth_success", BooleanType)
    .add("auth_attempts", IntegerType)
    .add("direction", StringType)
    .add("client", StringType)
    .add("server", StringType)
    .add("cipher_alg", StringType)
    .add("mac_alg", StringType)
    .add("compression_alg", StringType)
    .add("kex_alg", StringType)
    .add("host_key_alg", StringType)
    .add("host_key", StringType)
    .add("remote_location_country_code", StringType)
    .add("remote_location_region", StringType)
    .add("remote_location_longitude", DoubleType)
    .add("remote_location_city", StringType)
    .add("remote_location_latitude", DoubleType)
    .add("sensor", StringType)
}