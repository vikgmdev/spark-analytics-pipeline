package com.mantix4.ap.core.ml

import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.ml.clustering.{BisectingKMeans, KMeans}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.iforest.IForest
import org.apache.spark.ml.{Pipeline, PipelineStage, _}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.ml.linalg._
import org.apache.spark.sql.expressions.UserDefinedFunction

import scala.collection.mutable.ArrayBuffer

object AnomalyDetectionK {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def main[T](dataset: Dataset[T], categoricalColumns: Array[String], numericCols: Array[String]): DataFrame = {

    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    // Initialize Array of Pipeline's Stages
    var stages = ArrayBuffer[PipelineStage]()

    // Add Pipeline stages to normalize categorical and numeric features
    stages = setupFeaturesNormalizerPipeline(stages, categoricalColumns, numericCols)

    // Add Isolation Forest prediction to the pipeline
    stages = setupIsolationForestPipeline(stages)

    // VectorAssembler - transformer that combines a given list of columns into a single vector column.
    /*val assembler = new VectorAssembler()
      // .setInputCols(Array("anomalyScore", "scaledFeatures"))
      .setInputCols(Array("anomalyScore"))
      .setOutputCol("iforestFeatures")

    // Add Stage to the Array
    stages += assembler
    */

    // Configure an ML pipeline, which consists of four stages: stringIndexer, encoder, assembler and iForest.
    val pipeline = new Pipeline()
      .setStages(stages.toArray)

    // Train/fit and Predict anomalous instances
    val pipelineModel = pipeline.fit(dataset)

    // Now create a new dataframe using the prediction from our classifier
    val predictions_dataset = pipelineModel.transform(dataset)

    // Select only "uid" that is the log's id and the features column containing the Vector predictions
    // Create new dataframe to not override the original dataset
    // var featured_dataset = predictions_dataset.select("uid", "iforestFeatures")

    // predictKnumberKmeans(featured_dataset)

    // var dataframe_with_clusters = predictClusteringKmeans(featured_dataset)

    // To end join the dataframe with all the anomalous instances
    // needed for our detection with the original dataset containing the full data log
    // val outlier_dataset = predictions_dataset.join(dataframe_with_clusters, "uid")

    // Log end time of the full Anomaly Detection prediction, just for debug
    var endTime = System.currentTimeMillis()
    println(s"Anomaly Detection time: ${(endTime - startTime) / 1000} seconds.")

    // Return original dataset with the new outliers columns
    // outlier_dataset
    getCleanerOutlierDF(predictions_dataset, categoricalColumns)
  }

  def getCleanerOutlierDF(dataFrame: DataFrame, categoricalColumns: Array[String]): DataFrame = {
    // Initialize Array to store column names
    var columnsToDrop = ArrayBuffer[String]()

    // Add to a column's Array the new columns added by StringIndexer and OneHotEncoder
    for (categoricalCol <- categoricalColumns) {
      columnsToDrop :+ categoricalCol + "Index"
      columnsToDrop :+ categoricalCol + "classVec"
    }

    println(columnsToDrop.toArray: _*)

    // Drop features columns
    dataFrame
      .drop(columnsToDrop.toArray: _*)
      .drop("features")
      .drop("scaledFeatures")
      .drop("prediction")
  }

  def setupFeaturesNormalizerPipeline(stages: ArrayBuffer[PipelineStage], categoricalColumns: Array[String], numericCols: Array[String]): ArrayBuffer[PipelineStage] = {
    // Map Categorical columns features to numeric values
    for (categoricalCol <- categoricalColumns) {

      // StringIndexer - encodes a string column of labels to a column of label indices.
      val stringIndexer = new StringIndexer()
        .setInputCol(categoricalCol)
        .setOutputCol(categoricalCol + "Index")
        .setHandleInvalid("keep")

      // Add Stage to the Array
      stages += stringIndexer

      // One-hot encoding - maps a column of label indices to a column of binary vectors.
      val encoder = new OneHotEncoder()
        .setInputCol(categoricalCol + "Index")
        .setOutputCol(categoricalCol + "classVec")

      // Add Stage to the Array
      stages += encoder
    }

    // Initialize Array to store column names
    var assemblerInputs: Array[String] = Array()

    // Add to a column's Array the new columns added by StringIndexer and OneHotEncoder
    for (categoricalCol <- categoricalColumns) {
      assemblerInputs = numericCols :+ categoricalCol + "classVec"
    }

    // VectorAssembler - transformer that combines a given list of columns into a single vector column.
    val assembler = new VectorAssembler()
      .setInputCols(assemblerInputs)
      .setOutputCol("features")

    // Add Stage to the Array
    stages += assembler

    val scaler = new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setWithStd(true)
      .setWithMean(false)

    // Add Stage to the Array
    stages += scaler

    // return the stages array
    stages
  }

  def setupIsolationForestPipeline(stages: ArrayBuffer[PipelineStage]): ArrayBuffer[PipelineStage] = {
    // Train/fit and Predict anomalous instances
    // using the Isolation Forest model
    val iForest = new IForest()
      .setFeaturesCol("scaledFeatures")
      .setNumTrees(100)
      .setMaxSamples(100)
      .setContamination(0.2) // Marking 20% as odd
      .setBootstrap(false)
      .setMaxDepth(100)
      .setSeed(123456L)

    // Add Stage to the Array
    stages += iForest

    // return the stages array
    stages
  }

  def predictClusteringKmeans(featured_dataset: DataFrame): DataFrame = {
    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    // K-means - clustering algorithms that clusters the data points into a predefined number of clusters
    // k-means by default use the Vector features column to predict clusters in a dataframe
    // TODO: Use DBScan algorithm to predict the number of clusters instead of predefined it
    val kmeans = new KMeans()
      .setK(16)
      .setMaxIter(30)
      .setFeaturesCol("iforestFeatures")
      .setPredictionCol("cluster") // To avoid code confusions, rename the "prediction" column added by K-means to "cluster"

    // Trains a k-means model.
    val kModel = kmeans.fit(featured_dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = kModel.computeCost(featured_dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    val cluster_centers = kModel.clusterCenters
    cluster_centers.foreach(println)

    // Predict and clustered the dataframe using the k-means model
    var dataframe_with_clusters = kModel.transform(featured_dataset)

    val distanceFromCenter = udf((features: Vector, nc: Int) => {
      //distance(features, kModel.clusterCenters(c))
      val b = cluster_centers(nc)
      // math.sqrt(features.toArray.zip(b.toArray).map( p => p._1 - p._2).map(d => d + d).sum)
      val c = features.toArray.zip(b.toArray)
      math.sqrt( c.map( c => ( c._1 - c._2 ) * ( c._1 - c._2 )).reduce((x, y) => x + y ))
    })

    dataframe_with_clusters = dataframe_with_clusters.withColumn("distanceFromCenter", distanceFromCenter($"iforestFeatures", $"cluster"))

    println("Average distance: ")
    dataframe_with_clusters.groupBy("cluster").avg("distanceFromCenter").sort("cluster").show(false)


    println("==================== clustering output (cluster | count) ====================")
    dataframe_with_clusters.groupBy("cluster").count().sort("cluster").show(false)

    // Log end time of the pipeline just for debug
    var endTime = System.currentTimeMillis()
    println(s"Training and predicting Clustering time: ${(endTime - startTime) / 1000} seconds.")

    // Return the dataframe with clustering prediction column
    dataframe_with_clusters
  }

  def predictKnumberKmeans(featured_dataset: DataFrame): Unit = {
    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    // K-means - clustering algorithms that clusters the data points into a predefined number of clusters
    // k-means by default use the Vector features column to predict clusters in a dataframe
    // TODO: Use DBScan algorithm to predict the number of clusters instead of predefined it
    val kmeans = new KMeans()
      .setMaxIter(30)
      .setFeaturesCol("iforestFeatures")
      .setPredictionCol("cluster") // To avoid code confusions, rename the "prediction" column added by K-means to "cluster"

    for(i<- 10 to 25){
      // Trains a k-means model.
      val kModel = kmeans.setK(i).fit(featured_dataset)

      // Evaluate clustering by computing Within Set Sum of Squared Errors.
      val WSSSE = kModel.computeCost(featured_dataset)
      println(s"Within Set Sum of Squared Errors = $WSSSE for $i clusters")

      // Shows the result.
      //println("Cluster Centers: ")
      //kModel.clusterCenters.foreach(println)
    }

    // Log end time of the pipeline just for debug
    var endTime = System.currentTimeMillis()
    println(s"Predicting K number of Kmeans Clustering time: ${(endTime - startTime) / 1000} seconds.")
  }
}
