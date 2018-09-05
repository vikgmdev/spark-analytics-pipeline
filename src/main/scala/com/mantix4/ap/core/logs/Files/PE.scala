package com.mantix4.ap.core.logs.Files

import org.apache.spark.sql.types._

object PE {
  case class PE (
                  timestamp: String,
                  id: String,
                  machine: String,
                  compile_ts: String,
                  os: String,
                  subsystem: String,
                  is_exe: Option[Boolean],
                  is_64bit: Option[Boolean],
                  uses_aslr: Option[Boolean],
                  uses_dep: Option[Boolean],
                  uses_code_integrity: Option[Boolean],
                  uses_seh: Option[Boolean],
                  has_import_table: Option[Boolean],
                  has_export_table: Option[Boolean],
                  has_cert_table: Option[Boolean],
                  has_debug_data: Option[Boolean],
                  section_names: Option[Vector[String]],
                  sensor: String
                 ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("id", StringType)
    .add("machine", StringType)
    .add("compile_ts", StringType)
    .add("os", StringType)
    .add("subsystem", StringType)
    .add("is_exe", BooleanType)
    .add("is_64bit", BooleanType)
    .add("uses_aslr", BooleanType)
    .add("uses_dep", BooleanType)
    .add("uses_code_integrity", BooleanType)
    .add("uses_seh", BooleanType)
    .add("has_import_table", BooleanType)
    .add("has_export_table", BooleanType)
    .add("has_cert_table", BooleanType)
    .add("has_debug_data", BooleanType)
    .add("section_names", ArrayType(StringType))
    .add("sensor", StringType)
}


