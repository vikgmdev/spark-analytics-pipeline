package testing.enrichment

import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.UserDefinedFunction

object EnrichPCR {

  val withPCR: UserDefinedFunction = udf((direction: String, orig_bytes: Double, resp_bytes: Double) => {
    if (direction != "inbound") {

      val numerator = (orig_bytes + 0.0) - (resp_bytes + 0.0)
      val denominator = (orig_bytes + 0.0) + (resp_bytes + 0.0)

      if (numerator != 0.0) {

        val pcr = numerator / denominator
        lit(pcr)
      } else {
        lit(None.asInstanceOf[Double])
      }
    } else {
      lit(None.asInstanceOf[Double])
    }
  })
}
