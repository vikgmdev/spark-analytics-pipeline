package com.mantix4.ap.abstracts.base

import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

abstract case class Pipeline[T <: Product : TypeTag]() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def startPipeline(dt: Dataset[T]): Unit

  def customParsing(df: DataFrame): DataFrame

  def getDataframeType(df: DataFrame): DataFrame
  // def getDataframeType(df: DataFrame): Dataset[T]

  override def addBatch(batchId: Long, df: DataFrame): Unit = {
    val dataframe = getDataframeType(df)
    val dataset = getDataset(dataframe)
    this.startPipeline(dataset)
  }

  def getDataset(df: DataFrame): Dataset[T] = {
    // Select new column with the real log data
    val parsed_dataframe = this.customParsing(df)

    // Convert to a class dataset
    parsed_dataframe.as[T]
  }

  /*
  def getDataset(df: DataFrame): Dataset[T] = {
    // val logBase: LogBase = Class[T].asInstanceOf[LogBase]
    val logBase: LogBase = ClassTag[T].asInstanceOf[LogBase]

    logBase.stream_source match {

      case Sources.KAFKA =>
        df.withColumn("data",
          from_json($"value".cast(StringType), logBase.schemaBase))

      case Sources.FILEBEAT =>
        df.withColumn("filebeat_log",
            from_json($"value".cast(StringType), Filebeat.schemaBase))
          .select("filebeat_log.*")
          .withColumn("data",
            from_json($"json".cast(StringType), logBase.schemaBase))
    }

    // Select new column with the real log data
    df.select("data.*")

    val parsed_dataframe = this.customParsing(df)

    // Convert to a class dataset
    parsed_dataframe.as[T]
  }
  */
}