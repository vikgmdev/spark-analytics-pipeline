package com.mantix4.ap.core.enrichments

import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.UserDefinedFunction

object AddPCR {

  val withPCR: UserDefinedFunction = udf((direction: String, orig_bytes: Double, resp_bytes: Double) => {
    if (direction != "inbound") {

      val numerator = (orig_bytes + 0.0) - (resp_bytes + 0.0)
      val denominator = (orig_bytes + 0.0) + (resp_bytes + 0.0)

      if (numerator != 0.0) {

        val pcr = numerator / denominator
        pcr
      } else {
        0
      }
    } else {
      0
    }
  })
}