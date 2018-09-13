package com.mantix4.ap.core.enrichments

import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.sql.{Column, DataFrame}
import com.snowplowanalytics.maxmind.iplookups.IpLookups
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf


object IpLookupEnricher {

  implicit class DataFrameTransforms(df: DataFrame) {

    val ipLookups = IpLookups(
      geoFile = Some("/opt/mantix4/maxmind/GeoLite2-City.mmdb"),
      memCache = false,
      lruCache = 10000
    )

    def addGeoIPdata(direction: String, source_ip: String, dest_ip: String): DataFrame = {

      // i is an integer
      direction match {
        case "inbound"  =>
          val lookupResult = ipLookups.performLookups(source_ip)
          println(lookupResult.ipLocation)

        case "outbound"  =>
          val lookupResult = ipLookups.performLookups(dest_ip)
          println(lookupResult.ipLocation)
      }

      /*
      val sensorColumn: Column = df.col("sensor")
      df.select("data.*")
        .withColumn("sensor", sensorColumn)
        */

      df
    }
  }

  /*

  val ipLookups = IpLookups(
    geoFile = Some("/opt/mantix4/maxmind/GeoLite2-City.mmdb"),
    memCache = false,
    lruCache = 10000
  )

  val withIpLookups: UserDefinedFunction = udf((ip_to_lookup: String) => {
    val lookupResult = ipLookups.performLookups(ip_to_lookup)

    // Geographic lookup
    println(lookupResult.ipLocation)
    println(lookupResult.ipLocation)

    lookupResult.ipLocation
  })
  */
}
