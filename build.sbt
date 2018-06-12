resolvers += "Spark Packages Repo" at "https://dl.bintray.com/com.mantix4.ap.spark-packages/maven"

name := "Spark-Analytics-Pipeline"

version := "0.1"

scalaVersion := "2.11.12"

val sparkVersion = "2.2.0"

libraryDependencies += "log4j" % "log4j" % "1.2.14"

libraryDependencies += "org.apache.com.mantix4.ap.spark" %% "com.mantix4.ap.spark-core" % sparkVersion
libraryDependencies += "org.apache.com.mantix4.ap.spark" %% "com.mantix4.ap.spark-sql" % sparkVersion
libraryDependencies += "org.apache.com.mantix4.ap.spark" %% "com.mantix4.ap.spark-mllib" % sparkVersion
libraryDependencies += "org.apache.com.mantix4.ap.spark" % "com.mantix4.ap.spark-sql-kafka-0-10_2.11" % sparkVersion
libraryDependencies += "com.datastax.com.mantix4.ap.spark" %% "com.mantix4.ap.spark-cassandra-connector" % "2.0.2"

// GeoIP dependency
libraryDependencies += "com.snowplowanalytics" %% "scala-maxmind-iplookups" % "0.4.0"

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case "log4j.properties" => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") =>
    MergeStrategy.filterDistinctLines
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}