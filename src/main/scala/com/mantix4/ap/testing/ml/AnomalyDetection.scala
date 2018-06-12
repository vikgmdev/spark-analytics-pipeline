package com.mantix4.ap.testing.ml

import com.mantix4.ap.spark.SparkHelper
import com.mantix4.ap.testing.bro.DNS
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.iforest.IForest
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object AnomalyDetection {
  private val spark = SparkHelper.getSparkSession()

  def main(dataset: Dataset[DNS.Simple]): Unit = {
    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    // Index label values: 2 -> 0, 4 -> 1
    val indexer = new StringIndexer()
      .setInputCol("source_ip")
      .setOutputCol("label")

    val assembler = new VectorAssembler()
      .setInputCols(Array("source_port", "dest_ip", "dest_port", "proto", "query", "qclass", "qclass_name"))
      .setOutputCol("features")

    val iForest = new IForest()
      .setNumTrees(100)
      .setMaxSamples(256)
      .setContamination(0.35)
      .setBootstrap(false)
      .setMaxDepth(100)
      .setSeed(123456L)

    val pipeline = new Pipeline().setStages(Array(indexer, assembler, iForest))
    val model = pipeline.fit(dataset)
    val predictions = model.transform(dataset)

    val binaryMetrics = new BinaryClassificationMetrics(
      predictions.select("prediction", "label").rdd.map {
        case Row(label: Double, ground: Double) => (label, ground)
      }
    )

    // Log end time just for debug
    val endTime = System.currentTimeMillis()
    println(s"Training and predicting time: ${(endTime - startTime) / 1000} seconds.")
    println(s"The model's auc: ${binaryMetrics.areaUnderROC()}")
  }
}
