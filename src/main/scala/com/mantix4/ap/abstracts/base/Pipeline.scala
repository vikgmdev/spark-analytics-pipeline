package com.mantix4.ap.abstracts.base

import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

abstract case class Pipeline[T <: Product : TypeTag](schemaBase: StructType) extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def startPipeline(dt: Dataset[T]): Unit

  def customParsing(df: DataFrame): DataFrame

  override def addBatch(batchId: Long, df: DataFrame): Unit = {
    val dataset = getDataset(df)
    this.startPipeline(dataset)
  }

  def getDataset(df: DataFrame): Dataset[T] = {
    // Select new column with the real log data
    val parsed_dataframe = this.customParsing(df)

    // Convert to a class dataset
    parsed_dataframe.as[T]
  }
}