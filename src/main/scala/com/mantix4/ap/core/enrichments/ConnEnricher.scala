package com.mantix4.ap.core.enrichments

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._

object ConnEnricher {

  val withDirection: UserDefinedFunction = udf((local_orig: Boolean, local_resp: Boolean) => {
    if (local_orig && !local_resp) {
      "outbound"
    } else if (!local_orig && local_resp) {
      "inbound"
    } else {
      "local"
    }
  })

  val withPCR: UserDefinedFunction = udf((direction: String, orig_bytes: Double, resp_bytes: Double) => {
    var pcr: Double = 0.0
    if (direction != "inbound") {

      val numerator = (orig_bytes + 0.0) - (resp_bytes + 0.0)
      val denominator = (orig_bytes + 0.0) + (resp_bytes + 0.0)

      if (numerator != 0.0) {

        pcr = numerator / denominator
      }
    }

    // return PCR value
    pcr
  })
}
