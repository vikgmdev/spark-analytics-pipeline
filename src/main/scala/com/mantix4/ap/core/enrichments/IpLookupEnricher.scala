package com.mantix4.ap.core.enrichments

import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.sql.{Column, DataFrame}
import com.snowplowanalytics.maxmind.iplookups.IpLookups
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector.writer.SqlRowWriter


object IpLookupEnricher {

  implicit class DataFrameTransforms(df: DataFrame) {
    def saveToCassandra(keyspaceName: String): DataFrame = {
      println(keyspaceName)
      implicit val rowWriter: SqlRowWriter.Factory.type = SqlRowWriter.Factory
      df.rdd.saveToCassandra(keyspaceName, "conn", SomeColumns("timestamp",
        "uid",
        "source_ip",
        "source_port",
        "dest_ip",
        "dest_port",
        "proto",
        "service",
        "duration",
        "orig_bytes",
        "resp_bytes",
        "conn_state",
        "local_orig",
        "local_resp",
        "missed_bytes",
        "history",
        "orig_pkts",
        "orig_ip_bytes",
        "resp_pkts",
        "resp_ip_bytes",
        "tunnel_parents",
        "orig_l2_addr",
        "resp_l2_addr",
        "vlan",
        "inner_vlan",
        "sensor",
        "direction",
        "pcr"))
      df
    }
  }

  /*
  SomeColumns("timestamp",
    "uid",
    "source_ip",
    "source_port",
    "dest_ip",
    "dest_port",
    "proto",
    "service",
    "duration",
    "orig_bytes",
    "resp_bytes",
    "conn_state",
    "local_orig",
    "local_resp",
    "missed_bytes",
    "history",
    "orig_pkts",
    "orig_ip_bytes",
    "resp_pkts",
    "resp_ip_bytes",
    "tunnel_parents",
    "orig_l2_addr",
    "resp_l2_addr",
    "vlan",
    "inner_vlan",
    "sensor",
    "direction",
    "pcr")
  */


  // https://github.com/snowplow/scala-maxmind-iplookups
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
