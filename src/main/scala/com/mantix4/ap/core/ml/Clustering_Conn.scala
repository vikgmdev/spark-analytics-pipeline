package com.mantix4.ap.core.ml

/*
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Conn.Conn

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler, OneHotEncoder}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.ml.clustering.KMeans

object Clustering_Conn {
  private val spark = SparkHelper.getSparkSession()

  def main(dataset: Dataset[Conn]): Unit = {
    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    val categoricalColumns = Array("proto", "direction")
    val numericCols = Array("pcr")

    var assemblerInputs: Array[String] = Array()
    var stages = Array()

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

    val pipeline = new Pipeline().setStages(stages)
    val pipelineModel = pipeline.fit(dataset)
    val predictions_dataset = pipelineModel.transform(dataset)

    // Log end time just for debug
    val endTime = System.currentTimeMillis()
    println(s"Training and predicting time: ${(endTime - startTime) / 1000} seconds.")

    predictions_dataset.select("features").show()

    // Trains a k-means model.
    val kmeans = new KMeans().setK(70)
    val model = kmeans.fit(dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)

    val features = Array("proto", "direction", "pcr", "prediction")
    features :+ "prediction"
    val transformed = model.transform(dataset).select("proto", "direction", "pcr", "prediction")
    transformed.collect()
    transformed.show()

    transformed
      .groupBy("proto", "direction", "pcr", "prediction")
      .count()
      .sort("prediction")
      .show(50)
  }
}
*/