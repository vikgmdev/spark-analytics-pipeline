package com.mantix4.ap.core.ml

import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Conn.Conn
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.{OneHotEncoder, PCA, StringIndexer, VectorAssembler}
import org.apache.spark.ml.iforest.IForest
import org.apache.spark.sql.Dataset

object AnomalyDetection_Conn {
  private val spark = SparkHelper.getSparkSession()

  def main(dataset: Dataset[Conn]): Unit = {
    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    val categoricalColumns = Array("proto", "direction")
    val numericCols = Array("pcr")

    var assemblerInputs: Array[String] = Array()
    var stages: Array[PipelineStage] = Array()

    for (categoricalCol <- categoricalColumns) {
      val stringIndexer = new StringIndexer()
        .setInputCol(categoricalCol)
        .setOutputCol(categoricalCol + "Index")
      stages :+ stringIndexer

      val encoder = new OneHotEncoder()
        .setInputCol(categoricalCol + "Index")
        .setOutputCol(categoricalCol + "classVec")
      stages :+ encoder
    }

    for (categoricalCol <- categoricalColumns) {
      assemblerInputs = numericCols :+ categoricalCol + "classVec"
    }

    val assembler = new VectorAssembler()
      .setInputCols(assemblerInputs)
      .setOutputCol("features")
    stages :+ assembler

    // Train/fit and Predict anomalous instances
    // using the Isolation Forest model
    val iForest = new IForest()
      .setNumTrees(100)
      .setMaxSamples(256)
      .setContamination(0.2)
      .setBootstrap(false)
      .setMaxDepth(100)
      .setSeed(123456L)

    stages :+ iForest

    val pipeline = new Pipeline().setStages(stages)
    val pipelineModel = pipeline.fit(dataset)
    val predictions_dataset = pipelineModel.transform(dataset)

    // Log end time just for debug
    val endTime = System.currentTimeMillis()
    println(s"Training and predicting time: ${(endTime - startTime) / 1000} seconds.")

    predictions_dataset.select("features").show()

    /*

    // Trains a k-means model.
    val kmeans = new KMeans().setK(70)
    val model = kmeans.fit(predictions_dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = model.computeCost(predictions_dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)
    */

    /*
    val pca = new PCA()
      .setInputCol("features")
      .setOutputCol("pcaFeatures")
      .setK(5)
      .fit(predictions_dataset)

    val result = pca.transform(df).select("pcaFeatures")
    result.show(false)*/
  }
}
