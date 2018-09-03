package com.mantix4.ap.abstracts.base

import org.apache.spark.sql.types.StructType

abstract class LogBase extends Serializable {

  val schemaBase: StructType

  val stream_source: Sources.Value
}