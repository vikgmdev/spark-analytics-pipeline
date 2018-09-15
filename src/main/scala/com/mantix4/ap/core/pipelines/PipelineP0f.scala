package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.{Filebeat, Pipeline}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkObservations.P0f
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}

class PipelineP0f() extends Pipeline[P0f.P0f](P0f.schemaBase) {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[P0f.P0f]): Unit = {
    // Debug only
    dt.show(5000, truncate = true)
  }

  override def customParsing(df: DataFrame): DataFrame = {
    df
  }
}