package enrichment

import bro.Conn
import com.snowplowanalytics.maxmind.iplookups.model.IpLocation
import com.snowplowanalytics.maxmind.iplookups.{IpLookups, model}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{from_json, udf}
import org.apache.spark.sql.types.StringType
import spark.SparkHelper
import org.apache.spark.sql._

import scalaz._

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
      var isLocal: Boolean = false

      /*direction.toString() match {
        case "outbound" => ipToLookup = source_ip.toString()
        case "inbound" => ipToLookup = source_ip.toString()
        case "local" => isLocal = true
      }*/


      // val lookupResult = ipLookups.performLookups(ipToLookup)
      val lookupResult = ipLookups.performLookups(ipToLookup)

      // Geographic lookup
      var countryName = lookupResult.ipLocation.map(_.countryName)
      println(lookupResult.ipLocation).map(_.countryName) // => Some(Success("United States"))
      println(lookupResult.ipLocation).map(_.regionName)  // => Some(Success("Florida"))

      /*var countryCode: String
      var countryName: String
      var region: Option[String]
      var city: Option[String]
      var latitude: Float
      var longitude

      df.withColumn("countryCode", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("countryName", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("region", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("city", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("latitude", from_json($"value".cast(StringType), Conn.schemaOutput))
        .withColumn("longitude", from_json($"value".cast(StringType), Conn.schemaOutput))*/
    }
  }*/
}
