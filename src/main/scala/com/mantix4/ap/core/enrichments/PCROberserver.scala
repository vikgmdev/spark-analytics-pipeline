package com.mantix4.ap.core.enrichments

import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Conn
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.expressions.{UserDefinedFunction, Window}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{LongType, TimestampType}

object PCROberserver {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def main(dataset_to_observe: DataFrame): DataFrame = {
    // pcr_observer(dataset_to_observe, "1 minute")
    // pcr_observer(dataset_to_observe, "5 minute")
    // pcr_observer(dataset_to_observe, "15 minute")
    // pcr_observer(dataset_to_observe, "1 hour")
    // pcr_observer(dataset_to_observe, "1 day")
    pcr_aggregator_interval(dataset_to_observe)
  }

  def pcr_aggregator_interval(dataset_to_observe: DataFrame): DataFrame = {
    val over_window = Window.partitionBy($"source_ip", $"dest_ip").orderBy($"timestamp")

    val df_observed = dataset_to_observe
      .withColumn("this_time", $"timestamp".cast(TimestampType))
      .withColumn("last_time", lag($"timestamp", 1).over(over_window).cast(TimestampType))
      .withColumn("diff_interval", when($"last_time".isNotNull, $"this_time".cast(LongType) - $"last_time".cast(LongType)).otherwise(0))

    df_observed
      .select("timestamp", "source_ip", "source_port", "dest_ip", "dest_port", "proto", "orig_bytes", "last_time", "diff_interval")
      .orderBy("orig_bytes")
      .show(5000)
    df_observed.printSchema()
    val df_with_interval = df_observed
      .drop("this_time")
      .drop("last_time")

    df_with_interval
  }

  def pcr_observer(dataset_to_observe: DataFrame, interval: String): Unit = {
    /*val over_window =
      Window
        .partitionBy($"source_ip", $"dest_ip", $"direction")
        .orderBy($"timestamp")
        */
    val df_observed = dataset_to_observe
      .groupBy($"source_ip", $"dest_ip", $"direction",
        window($"timestamp", interval))
      .agg(
        avg("pcr").as("pcr_average"),
        avg("duration").as("duration_average")
      )
      .withColumn("summary_interval", lit(interval))
      .withColumn("start_window", $"window.start")
      .withColumn("end_window", $"window.end")
      .drop("window")
      .sort($"duration_average".desc)

    df_observed.show()
    df_observed.printSchema()
  }
}
