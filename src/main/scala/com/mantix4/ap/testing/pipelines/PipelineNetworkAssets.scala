package com.mantix4.ap.testing.pipelines

import com.mantix4.ap.base.{Filebeat, SinkBase}
import com.mantix4.ap.spark.SparkHelper
import com.mantix4.ap.testing.bro.X509
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{ArrayType, BooleanType, DoubleType, StringType}
import org.apache.spark.sql.{Column, DataFrame, Dataset}
import java.util.regex.Pattern

/**
  * must be idempotent and synchronous (@TODO check asynchronous/synchronous from Datastax's Spark connector) sink
  */
class PipelineNetworkAssets() extends SinkBase {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(df: DataFrame): Unit = {

    // Parse DataFrame to Dataset with type of log
    val dataset = getDataset(df)

    // Debug only
    dataset.show(5000)

    // Save to Cassandra
    // dataset.rdd.saveToCassandra("bro", Conn.cassandraTable, Conn.cassandraColumns)
  }

  def getDataset(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), Filebeat.schemaBase))
      .select("data.*")
      .withColumn("date", substring($"message", 2, 19))
      .withColumn("p0f_log", substring_index($"message", "] ", -1))
      .select(
        $"date",
        $"p0f_log")
      .withColumn("mod", regexp_extractAll($"p0f_log", lit("mod=\\w+|"), lit(0)))
      //.withColumn("_tmp", split($"p0f_log", "\\|"))
      //.withColumn("emp", getColumnsUDF($"_tmp"))
  }

  val getColumnsUDF = udf((details: Seq[String]) => {
    details.map(_.split("=")).map(x => (x(0), x(1)))
  })

  def regexp_extractAll = udf((job: String, exp: String, groupIdx: Int) => {
    println("the column value is" + job.toString)
    val pattern = Pattern.compile(exp.toString)
    val m = pattern.matcher(job.toString)
    //println("The value is: " + m.toString)
    var result = Seq[String]()
    m.find().toString
    /*while (m.find) {
      val temp =
        result =result:+m.group(groupIdx)
    }
    result.mkString(",")*/
  })
}