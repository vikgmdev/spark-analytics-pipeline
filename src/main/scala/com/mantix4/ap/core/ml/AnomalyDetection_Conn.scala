package com.mantix4.ap.core.ml

import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Conn.Conn
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.{OneHotEncoder, PCA, StringIndexer, VectorAssembler}
import org.apache.spark.ml.iforest.IForest
import org.apache.spark.sql.Dataset

import scala.collection.mutable.ArrayBuffer

object AnomalyDetection_Conn {
  private val spark = SparkHelper.getSparkSession()

  def main(dataset: Dataset[Conn]): Unit = {
    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    val categoricalColumns = Array("proto", "direction")
    val numericCols = Array("pcr")


    // StringIndexer
    val stringIndexer_1 = new StringIndexer().setInputCol("proto").setOutputCol("proto" + "Index")
    val stringIndexer_2 = new StringIndexer().setInputCol("direction").setOutputCol("direction" + "Index")

    // OneHotEncoder
    val encoder_1 = new OneHotEncoder().setInputCol("proto" + "Index").setOutputCol("proto" + "classVec")
    val encoder_2 = new OneHotEncoder().setInputCol("direction" + "Index").setOutputCol("direction" + "classVec")

    var assemblerInputs = Array("proto" + "classVec", "direction" + "classVec", "pcr")
    val assembler = new VectorAssembler()
      .setInputCols(assemblerInputs)
      .setOutputCol("features")

    // Train/fit and Predict anomalous instances
    // using the Isolation Forest model
    val iForest = new IForest()
      .setNumTrees(100)
      .setMaxSamples(256)
      .setContamination(0.2)
      .setBootstrap(false)
      .setMaxDepth(100)
      .setSeed(123456L)

    val stages = Array(stringIndexer_1, stringIndexer_2, encoder_1, encoder_2, assembler, iForest)

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
