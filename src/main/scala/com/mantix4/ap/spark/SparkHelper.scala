package com.mantix4.ap.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object SparkHelper {

  def getAndConfigureSparkSession(): SparkSession = {
    val conf = new SparkConf()
      .setAppName("Analytics Pipeline")
      .set("spark.sql.streaming.checkpointLocation", "checkpoint")

    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    // return com.mantix4.ap.spark session
    getSparkSession()
  }

  def getSparkSession(): SparkSession = {
    SparkSession
      .builder()
      .getOrCreate()
  }
}