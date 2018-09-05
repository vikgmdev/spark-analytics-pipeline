package com.mantix4.ap.core.logs.Files

import org.apache.spark.sql.types._

object Files {
case class Files (
                  timestamp: String,
                  fuid: String,
                  tx_hosts: Vector[String],
                  rx_hosts: Vector[String],
                  conn_uids: Vector[String],
                  source: String,
                  depth: Option[Int],
                  analyzers: Vector[String],
                  mime_type: String,
                  filename: String,
                  duration: Option[Double],
                  local_orig: Option[Boolean],
                  is_orig: Option[Boolean],
                  seen_bytes: Option[Int],
                  total_bytes: Option[Int],
                  missing_bytes: Option[Int],
                  overflow_bytes: Option[Int],
                  timedout: Option[Boolean],
                  parent_fuid: String,
                  md5: String,
                  sha1: String,
                  sha256: String,
                  extracted: String,
                  extracted_cutoff: Option[Boolean],
                  extracted_size: Option[Int],
                  entropy: Option[Double],
                  sensor: String
                 ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("fuid", StringType)
    .add("tx_hosts", ArrayType(StringType))
    .add("rx_hosts", ArrayType(StringType))
    .add("conn_uids", ArrayType(StringType))
    .add("source", StringType)
    .add("depth", IntegerType)
    .add("analyzers", ArrayType(StringType))
    .add("mime_type", StringType)
    .add("filename", StringType)
    .add("duration", DoubleType)
    .add("local_orig", BooleanType)
    .add("is_orig", BooleanType)
    .add("seen_bytes", IntegerType)
    .add("total_bytes", IntegerType)
    .add("missing_bytes", IntegerType)
    .add("overflow_bytes", IntegerType)
    .add("timedout", BooleanType)
    .add("parent_fuid", StringType)
    .add("md5", StringType)
    .add("sha1", StringType)
    .add("sha256", StringType)
    .add("extracted", StringType)
    .add("extracted_cutoff", BooleanType)
    .add("extracted_size", IntegerType)
    .add("entropy", DoubleType)
    .add("sensor", StringType)
}
