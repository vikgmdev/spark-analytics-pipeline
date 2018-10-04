package com.mantix4.ap.core.enrichments

import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Conn
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._

object PCROberserver {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def main(dataset_to_observe: DataFrame): Unit = {
    pcr_observer(dataset_to_observe, "1 minute")
    pcr_observer(dataset_to_observe, "5 minute")
    pcr_observer(dataset_to_observe, "15 minute")
    pcr_observer(dataset_to_observe, "1 hour")
    pcr_observer(dataset_to_observe, "1 day")
  }

  def pcr_observer(dataset_to_observe: DataFrame, interval: String): Unit = {
    // val over_window = Window.partitionBy($"source_ip", $"source_port", $"dest_ip", $"dest_port")
    dataset_to_observe
      .groupBy($"source_ip", $"source_port", $"dest_ip", $"dest_port", $"direction",
        window($"timestamp", interval))
      .agg(
        avg("pcr").as("pcr_average"),
        avg("duration").as("duration_average"),
        count("*")
      )
      .withColumn("summary_interval", lit(interval))
      .withColumn("start_window", $"window.start")
      .withColumn("end_window", $"window.end")
      .drop("window")
      .sort($"duration_average".desc)
      .show()
  }
}
