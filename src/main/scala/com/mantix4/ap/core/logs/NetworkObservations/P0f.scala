package com.mantix4.ap.core.logs.NetworkObservations

import org.apache.spark.sql.types._

object P0f {
  case class P0f (
                      date: String,
                      mod: String,
                      cli: String,
                      srv: String,
                      subj: String,
                      os: String,
                      dist: String,
                      app: String,
                      lang: String,
                      params: String,
                      raw_sig: String,
                      link: String,
                      raw_mtu: String,
                      uptime: String,
                      raw_freq: String,
                      reason: String,
                      raw_hits: String
                 ) extends Serializable

  val schemaBase: StructType = new StructType()
    // Base keys in p0f log
    .add("date", StringType)
    .add("mod", StringType)
    .add("cli", StringType)
    .add("srv", StringType)
    .add("subj", StringType)

    // Values when 'os' key is present
    .add("os", StringType)
    .add("dist", StringType)

    // Values when 'app' key is present
    .add("app", StringType)
    .add("lang", StringType)

    // Values when 'os' and 'app' keys is present
    .add("params", StringType)
    .add("raw_sig", StringType)

    // Values when 'link' key is present
    .add("link", StringType)
    .add("raw_mtu", StringType)

    // Values when 'uptime' key is present
    .add("uptime", StringType)
    .add("raw_freq", StringType)

    // Values when 'reason' key is present
    .add("reason", StringType)
    .add("raw_hits", StringType)
}
