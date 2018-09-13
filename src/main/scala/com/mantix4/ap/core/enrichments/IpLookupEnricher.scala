package com.mantix4.ap.core.enrichments

import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.sql.{Column, DataFrame}
import com.snowplowanalytics.maxmind.iplookups.IpLookups
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf


object IpLookupEnricher {

  // https://stackoverflow.com/questions/30075106/using-maxmind-geoip-in-spark-serialized
  /*

  implicit class DataFrameTransforms(df: DataFrame) {

    def addGeoIPdata(direction: String, source_ip: String, dest_ip: String): DataFrame = {
      val ipLookups = IpLookups(
        geoFile = Some("/opt/mantix4/maxmind/GeoLite2-City.mmdb"),
        memCache = false,
        lruCache = 10000
      )

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
  */
}
