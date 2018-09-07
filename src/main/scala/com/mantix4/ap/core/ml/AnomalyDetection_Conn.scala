package com.mantix4.ap.core.ml

import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Conn.Conn
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.{OneHotEncoder, PCA, StringIndexer, VectorAssembler}
import org.apache.spark.ml.iforest.IForest
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._
import org.apache.spark.ml._
import org.apache.spark.sql.types.{ArrayType, DoubleType, StringType, StructType}

import scala.collection.mutable.ArrayBuffer

object AnomalyDetection_Conn {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def main(dataset: Dataset[Conn]): Unit = {
    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    val categoricalColumns = Array("proto", "direction")
    val numericCols = Array("pcr")

    var assemblerInputs: Array[String] = Array()
    var stages = ArrayBuffer[PipelineStage]()

    for (categoricalCol <- categoricalColumns) {
      val stringIndexer = new StringIndexer()
        .setInputCol(categoricalCol)
        .setOutputCol(categoricalCol + "Index")
        .setHandleInvalid("skip")
      stages += stringIndexer

      val encoder = new OneHotEncoder()
        .setInputCol(categoricalCol + "Index")
        .setOutputCol(categoricalCol + "classVec")
      stages += encoder
    }

    for (categoricalCol <- categoricalColumns) {
      assemblerInputs = numericCols :+ categoricalCol + "classVec"
    }

    val assembler = new VectorAssembler()
      .setInputCols(assemblerInputs)
      .setOutputCol("features")
    stages += assembler

    // Train/fit and Predict anomalous instances
    // using the Isolation Forest model
    val iForest = new IForest()
      .setNumTrees(100)
      .setMaxSamples(256)
      .setContamination(0.2)
      .setBootstrap(false)
      .setMaxDepth(100)
      .setSeed(123456L)

    stages += iForest

    val pipeline = new Pipeline().setStages(stages.toArray)
    val pipelineModel = pipeline.fit(dataset)
    val predictions_dataset = pipelineModel.transform(dataset)

    // Log end time just for debug
    val endTime = System.currentTimeMillis()
    println(s"Training and predicting time: ${(endTime - startTime) / 1000} seconds.")

    //predictions_dataset.select("uid","features").show()

    val featured_dataset = predictions_dataset.select("uid", "proto", "direction", "pcr" ,"features")
    // val featured_dataset = predictions_dataset.select("uid", "features")
    // featured_dataset.printSchema()

    // Trains a k-means model.
    val kmeans = new KMeans().setK(70)
    val model = kmeans.fit(featured_dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = model.computeCost(featured_dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)

    val featured_dataset_clusters = model.transform(featured_dataset)
    val featured_dataset_clusters_renamed = featured_dataset_clusters.withColumnRenamed("prediction", "cluster")
    featured_dataset_clusters_renamed.groupBy("proto", "direction", "pcr" ,"cluster")
      .count()
      .sort($"count".desc)
      .show(50)
    /*

    val pca = new PCA()
      .setInputCol("features")
      .setOutputCol("pcaFeatures")
      .setK(3)
      .fit(featured_dataset_clusters)

    val result = pca.transform(featured_dataset_clusters)

    // A UDF to convert VectorUDT to ArrayType
    val vecToArray = udf( (xs: linalg.Vector) => xs.toArray )

    // Add a ArrayType Column
    val result_pca = result.withColumn("pcaFeaturesArray" , vecToArray($"pcaFeatures") )
      .select($"uid", $"pcaFeaturesArray", $"pcaFeaturesArray".getItem(0).as("x"), $"pcaFeaturesArray".getItem(1).as("y"))

    val final_df = predictions_dataset.join(result_pca, "uid")
    println("Final DF schema: ")
    final_df.show()
    */
  }
}
