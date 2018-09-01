package com.mantix4.ap.production.bro

import java.sql.Timestamp

import com.mantix4.ap.base.LogBase
import com.datastax.spark.connector.SomeColumns
import org.apache.spark.sql.types._

object Conn extends LogBase {

  override val topicName: String = "conn"

  val cassandraTable = "conn"

  case class IpLocation(
                         countryCode: String,
                         countryName: String,
                         region: Option[String],
                         city: Option[String],
                         latitude: Option[Double],
                         longitude: Option[Double]
                       )

  case class Simple (
                  timestamp: String,
                  uid: String,
                  source_ip: String,
                  source_port: Option[Int],
                  dest_ip: String,
                  dest_port: Option[Int],
                  proto: String,
                  service: String,
                  direction: String,
                  duration: Option[Double],
                  orig_bytes: Option[Double],
                  resp_bytes: Option[Double],
                  conn_state: String,
                  local_orig: Option[Boolean],
                  local_resp: Option[Boolean],
                  missed_bytes: Option[Double],
                  history: String,
                  orig_pkts: Option[Double],
                  orig_ip_bytes: Option[Double],
                  resp_pkts: Option[Double],
                  resp_ip_bytes: Option[Double],
                  pcr: Option[Double],
                  sensor: String
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
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
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
    .add("sensor", StringType)
    .add("type", StringType)
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