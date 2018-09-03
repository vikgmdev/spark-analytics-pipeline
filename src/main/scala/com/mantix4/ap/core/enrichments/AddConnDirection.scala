package com.mantix4.ap.core.enrichments

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._

object AddConnDirection {

  val withDirection: UserDefinedFunction = udf((local_orig: Boolean, local_resp: Boolean) => {
    if (local_orig && !local_resp) {
      "outbound"
    } else if (!local_orig && local_resp) {
      "inbound"
    } else {
      "local"
    }
  })
}
