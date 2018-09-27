package com.mantix4.ap.core.ml

import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.ml.clustering.{BisectingKMeans, KMeans}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.iforest.IForest
import org.apache.spark.ml.{Pipeline, PipelineStage, _}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.ml.linalg._

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

    // Configure an ML pipeline, which consists of four stages: stringIndexer, encoder, assembler and iForest.
    val pipeline = new Pipeline()
      .setStages(stages.toArray)

    // Train/fit and Predict anomalous instances
    val pipelineModel = pipeline.fit(dataset)

    // Now create a new dataframe using the prediction from our classifier
    val predictions_dataset = pipelineModel.transform(dataset)

    // VectorAssembler - transformer that combines a given list of columns into a single vector column.
    val assembler = new VectorAssembler()
      // .setInputCols(Array("anomalyScore", "scaledFeatures"))
      .setInputCols(Array("anomalyScore"))
      .setOutputCol("iforestFeatures")

    val pipelineIForest = new Pipeline()
      .setStages(Array(assembler))

    // Train/fit and Predict anomalous instances
    val pipelineModelIForest = pipelineIForest.fit(predictions_dataset)

    // Now create a new dataframe using the prediction from our classifier
    val predictions_IForest_dataset = pipelineModelIForest.transform(predictions_dataset)

    println("Result of Isolation Forest:")
    predictions_IForest_dataset.show(false)

    // Select only "uid" that is the log's id and the features column containing the Vector predictions
    // Create new dataframe to not override the original dataset
    var featured_dataset = predictions_IForest_dataset.select("uid", "iforestFeatures")

    // predictKnumberKmeans(featured_dataset)

    var dataframe_with_clusters = predictClusteringKmeans(featured_dataset)

    // val pca_dataframe = predictPCA(dataframe_with_clusters)

    // To end join the dataframe with all the anomalous instances
    // needed for our detection with the original dataset containing the full data log
    val outlier_dataset = predictions_IForest_dataset.join(dataframe_with_clusters, "uid")

    // Log end time of the full Anomaly Detection prediction, just for debug
    var endTime = System.currentTimeMillis()
    println(s"Anomaly Detection time: ${(endTime - startTime) / 1000} seconds.")

    // Return original dataset with the new outliers columns "x", "y" and "cluster"
    outlier_dataset
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
    kModel.clusterCenters.foreach(println)

    // Predict and clustered the dataframe using the k-means model
    var dataframe_with_clusters = kModel.transform(featured_dataset)

    // UDF that calculates for each point distance from each cluster center
    val distFromCenter = udf((features: Vector, c: Int) => Vectors.sqdist(features, kModel.clusterCenters(c)))

    dataframe_with_clusters = dataframe_with_clusters.withColumn("distanceFromCenter", distFromCenter($"iforestFeatures", $"cluster"))

    /*
    val pointsDistance = dataframe_with_clusters
      .select("iforestFeatures", "cluster")
      .as[Vector]
      .map( Vector =>
        (Vector, findMinDistance(Vector, kModel.clusterCenters))
      )
      */

    /*
    println(s"Points Distance: $pointsDistance")

    val clusterDistanceTuple = pointsDistance
      .map {
        case (a, (cluster, distance)) => (cluster, distance)
      } //x: (Vector, (Int, Double))
    println(s"Cluster Distance Tuple $clusterDistanceTuple")
    */

    /*
    val averageDistance = clusterDistanceTuple.aggregateByKey((0.0, 0.0))((acc, value) => (acc._1 + value, acc._2 + 1),
      (it1, it2) => ((it1._1 + it2._1), it1._2 + it2._2))
      .map(aggregateDistanceTuple => (aggregateDistanceTuple._1, aggregateDistanceTuple._2._1 / aggregateDistanceTuple._2._2))
    val clustersAverageDF = averageDistance.map { case (cluster, distance) => AverageDistance(cluster, distance) }.toDF().repartition(1)
    clustersAverageDF.write.mode(SaveMode.Overwrite).save(machineConfig.getString("averageDistanceClusters")
    */



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

  def predictPCA(dataframe_with_clusters: DataFrame): DataFrame = {
    // Log start time just for debug
    val startTime = System.currentTimeMillis()

    // PCA - convert a set of observations on possibly correlated variables
    // converting features vector into 3-dimensional principal components (x,y,z)
    val pca = new PCA()
      .setInputCol("iforestFeatures")
      .setOutputCol("pcaFeatures")
      .setK(1)
      .fit(dataframe_with_clusters)
    println("Fit PCA model:")
    dataframe_with_clusters.show(5000, truncate = false)

    // Predict and get our 3-dimensional principal components (x,y,z)
    // create a new dataframe with the PCA predictions containing:
    // "uid", "cluster", "features", "pcaFeatures"
    var pca_dataframe = pca.transform(dataframe_with_clusters)
    println("Transform PCA model:")
    pca_dataframe.show(false)

    // A helper UDF to convert ML linalg VectorUDT to ArrayType
    val vecToArray = udf( (xs: linalg.Vector) => xs.toArray )

    // Convert the "pcaFeatures" Vector column to an ArrayType Column
    // and get only x and y values from the Array column
    pca_dataframe = pca_dataframe
      .withColumn("pcaFeaturesArray" , vecToArray($"pcaFeatures") )
      .select($"uid", $"cluster", $"pcaFeaturesArray",
        $"pcaFeaturesArray".getItem(0).as("x"))
        //$"pcaFeaturesArray".getItem(1).as("y"))

    // Log end time of the pipeline just for debug
    var endTime = System.currentTimeMillis()
    println(s"Training and predicting PCA time: ${(endTime - startTime) / 1000} seconds.")

    pca_dataframe
  }

  def findMinDistance(a: Vector, centroids: Array[Vector]): (Int, Double) = {
    var distMap: Map[Int,Double] = Map()
    for(i <- centroids)
    {
      distMap = distMap + findDistance(a, i)
    }
    var nuMCL = -1
    distMap.minBy( x => x._2 )
  }

  def findDistance(a: Vector, b: Vector): (Int, Double) = {
    // nuMCL=nuMCL+1
    (
      1,
      distance(a, b)
    )
  }

  def distance(a: Vector, b: Vector): Double =
    math.sqrt(a.toArray.zip(b.toArray).map( p => p._1 - p._2).map(d => d + d).sum)
}
