package com.mantix4.ap.testing.bro

import com.mantix4.ap.base.LogBase
import org.apache.spark.sql.types._

object NetworkAssets extends LogBase {

  override val topicName: String = "network-assets"

  val cassandraTable = "x509"

  case class Simple (
                      timestamp: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
}



