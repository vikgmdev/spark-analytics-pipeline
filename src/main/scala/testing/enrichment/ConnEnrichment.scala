package testing.enrichment

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame}

object ConnEnrichment {

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
