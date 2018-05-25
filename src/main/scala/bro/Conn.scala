package bro

import java.sql.Timestamp

import com.datastax.spark.connector.SomeColumns
import org.apache.spark.sql.types._

object Conn {

  val cassandraTable = "conn"

  case class IpLocation(
                         countryCode: String,
                         countryName: String,
                         region: Option[String],
                         city: Option[String],
                         latitude: Float,
                         longitude: Float
                       )

  case class Simple (
                  timestamp: String,
                  uid: String,
                  source_ip: String,
                  source_port: Int,
                  dest_ip: String,
                  dest_port: Int,
                  proto: String,
                  service: String,
                  direction: String,
                  duration: Double,
                  orig_bytes: Double,
                  resp_bytes: Double,
                  conn_state: String,
                  local_orig: Boolean,
                  local_resp: Boolean,
                  missed_bytes: Double,
                  history: String,
                  sensor: String,
                  orig_pkts: Double,
                  orig_ip_bytes: Double,
                  resp_pkts: Double,
                  resp_ip_bytes: Double,
                  pcr: Double
                 ) extends Serializable

  val cassandraColumns = SomeColumns(
    "timestamp",
    "uid",
    "source_ip",
    "source_port",
    "dest_ip",
    "dest_port",
    "proto",
    "direction",
    "service",
    "duration",
    "orig_bytes",
    "resp_bytes",
    "conn_state",
    "local_orig",
    "local_resp",
    "missed_bytes",
    "history",
    "orig_pkts",
    "orig_ip_bytes",
    "resp_pkts",
    "resp_ip_bytes",
    "pcr"
  )

  val schemaBase: StructType = new StructType()
    .add("data",  new StructType()
      .add("ts", StringType)
      .add("uid", StringType)
      .add("id.orig_h", StringType)
      .add("id.orig_p", IntegerType)
      .add("id.resp_h", StringType)
      .add("id.resp_p", IntegerType)
      .add("proto", StringType)
      .add("service", StringType)
      .add("duration", DoubleType)
      .add("orig_bytes", DoubleType)
      .add("resp_bytes", DoubleType)
      .add("conn_state", StringType)
      .add("local_orig", BooleanType)
      .add("local_resp", BooleanType)
      .add("missed_bytes", DoubleType)
      .add("history", StringType)
      .add("orig_pkts", DoubleType)
      .add("orig_ip_bytes", DoubleType)
      .add("resp_pkts", DoubleType)
      .add("resp_ip_bytes", DoubleType)
      .add("pcr", DoubleType)
      .add("sensor", StringType))
    .add("sensor", StringType)

  val schemaOutput: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", IntegerType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", IntegerType)
    .add("proto", StringType)
    .add("service", StringType)
    .add("duration", DoubleType)
    .add("orig_bytes", DoubleType)
    .add("resp_bytes", DoubleType)
    .add("conn_state", StringType)
    .add("local_orig", BooleanType)
    .add("local_resp", BooleanType)
    .add("missed_bytes", DoubleType)
    .add("history", StringType)
    .add("orig_pkts", DoubleType)
    .add("orig_ip_bytes", DoubleType)
    .add("resp_pkts", DoubleType)
    .add("resp_ip_bytes", DoubleType)
    .add("pcr", DoubleType)
}

  /*
CREATE TABLE conn (
  timestamp timestamp PRIMARY KEY,
  uid text,
  source_ip inet,
  source_port int,
  dest_ip inet,
  dest_port int,
  proto varchar,
  service varchar,
  direction varchar,
  duration double,
  orig_bytes double,
  resp_bytes double,
  conn_state varchar,
  local_orig boolean,
  local_resp boolean,
  missed_bytes double,
  history varchar,
  orig_pkts double,
  orig_ip_bytes double,
  resp_pkts double,
  resp_ip_bytes double,
  pcr double
);
  */