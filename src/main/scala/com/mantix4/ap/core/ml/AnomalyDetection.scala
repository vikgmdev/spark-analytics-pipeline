package com.mantix4.ap.core.ml

import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.Conn.Conn
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.{OneHotEncoder, PCA, StringIndexer, VectorAssembler}
import org.apache.spark.ml.iforest.IForest
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.functions._
import org.apache.spark.ml._

import scala.collection.mutable.ArrayBuffer

object AnomalyDetection {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  def main(dataset: Dataset[Conn], categoricalColumns: Array[String], numericCols: Array[String]): DataFrame = {

    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    // Initialize Array of Pipeline's Stages
    var stages = ArrayBuffer[PipelineStage]()

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

    // Train/fit and Predict anomalous instances
    // using the Isolation Forest model
    val iForest = new IForest()
      .setNumTrees(100)
      .setMaxSamples(100)
      .setContamination(0.2) // Marking 20% as odd
      .setBootstrap(false)
      .setMaxDepth(100)
      .setSeed(123456L)

    // Add Stage to the Array
    stages += iForest

    // Configure an ML pipeline, which consists of four stages: stringIndexer, encoder, assembler and iForest.
    val pipeline = new Pipeline()
      .setStages(stages.toArray)

    // Train/fit and Predict anomalous instances using the Isolation Forest model
    val pipelineModel = pipeline.fit(dataset)

    // Now create a new dataframe using the prediction from our classifier
    val predictions_dataset = pipelineModel.transform(dataset)

    // Log end time of the pipeline just for debug
    var endTime = System.currentTimeMillis()
    println(s"Training and predicting time: ${(endTime - startTime) / 1000} seconds.")

    // Select only "uid" that is the log's id and the features column containing the Vector predictions
    // Create new dataframe to not override the original dataset
    var featured_dataset = predictions_dataset.select("uid", "features")

    // K-means - clustering algorithms that clusters the data points into a predefined number of clusters
    // k-means by default use the Vector features column to predict clusters in a dataframe
    // TODO: Use DBScan algorithm to predict the number of clusters instead of predefined it
    val kmeans = new KMeans().setK(5)

    // Trains a k-means model.
    val model = kmeans.fit(featured_dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = model.computeCost(featured_dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)

    // Predict and clustered the dataframe using the k-means model
    var dataframe_with_clusters = model.transform(featured_dataset)

    // To avoid code confusions, rename the "prediction" column added by K-means to "cluster"
    dataframe_with_clusters = dataframe_with_clusters.withColumnRenamed("prediction", "cluster")

    // PCA - convert a set of observations on possibly correlated variables
    // converting features vector into 3-dimensional principal components (x,y,z)
    val pca = new PCA()
      .setInputCol("features")
      .setOutputCol("pcaFeatures")
      .setK(3)
      .fit(dataframe_with_clusters)

    // Predict and get our 3-dimensional principal components (x,y,z)
    // create a new dataframe with the PCA predictions containing:
    // "uid", "cluster", "features", "pcaFeatures"
    var pca_dataframe = pca.transform(dataframe_with_clusters)

    // A helper UDF to convert ML linalg VectorUDT to ArrayType
    val vecToArray = udf( (xs: linalg.Vector) => xs.toArray )

    // Convert the "pcaFeatures" Vector column to an ArrayType Column
    // and get only x and y values from the Array column
    pca_dataframe = pca_dataframe
      .withColumn("pcaFeaturesArray" , vecToArray($"pcaFeatures") )
      .select($"uid", $"cluster", $"pcaFeaturesArray",
        $"pcaFeaturesArray".getItem(0).as("x"),
        $"pcaFeaturesArray".getItem(1).as("y"))

    // To end join the dataframe with all the anomalous instances
    // needed for our detection with the original dataset containing the full data log
    val outlier_dataset = predictions_dataset.join(pca_dataframe, "uid")

    // Log end time of the full Anomaly Detection prediction, just for debug
    endTime = System.currentTimeMillis()
    println(s"Anomaly Detection time: ${(endTime - startTime) / 1000} seconds.")

    // Return original dataset with the new outliers columns "x", "y" and "cluster"
    outlier_dataset
  }
}
