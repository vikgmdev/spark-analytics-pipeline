package enrichment

import bro.Conn
import com.snowplowanalytics.maxmind.iplookups.IpLookups
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{from_json, udf}
import org.apache.spark.sql.types.StringType
import spark.SparkHelper
import org.apache.spark.sql._

object GeoIP {

  /*
  implicit class DataFrameTransforms(df: DataFrame) {
    private val spark = SparkHelper.getSparkSession()
    import spark.implicits._

    val ipLookups = IpLookups(
      geoFile = Some("/opt/mantix4/cluster/maxmind/GeoLite2-City.mmdb"),
      ispFile = None,
      domainFile = None,
      connectionTypeFile = None, memCache = false, lruCache = 20000)

    def addLocation(direction: Column, source_ip: Column, dest_ip: Column): DataFrame = {
      var ipToLookup: String = ""

      direction.toString() match {
        case "outbound" => ipToLookup = source_ip.toString()
        case "inbound" => ipToLookup = source_ip.toString()
        case "local" => ipToLookup = source_ip.toString()
      }

      val location = ipLookups.performLookups(ipToLookup).ipLocation

        val lookupResult = ipLookups.performLookups(df.select($"orig_ip").toString())

        val countryName = (lookupResult._1).map(_.countryName).getOrElse("")
        val city = (lookupResult._1).map(_.city).getOrElse(None).getOrElse("")
        val latitude = (lookupResult._1).map(_.latitude).getOrElse(None).toString
        val longitude = (lookupResult._1).map(_.longitude).getOrElse(None).toString

      df.withColumn("countryCode", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("countryName", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("region", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("city", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("latitude", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("longitude", from_json($"value".cast(StringType), Conn.schemaOutput))
    }
  }
  */
}
