package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object Conn {
  case class Conn (
                    timestamp: String,
                    uid: String,
                    source_ip: String,
                    source_port: Option[Int],
                    dest_ip: String,
                    dest_port: Option[Int],
                    proto: String,
                    service: String,
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
                    //tunnel_parents: Option[Vector[String]],
                    orig_l2_addr: String,
                    resp_l2_addr: String,
                    vlan: Option[Int],
                    inner_vlan: Option[Int],
                    sensor: String,
                    direction: String,
                    pcr: Option[Double]
                  ) extends Serializable

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
    //.add("tunnel_parents", ArrayType(StringType))
    .add("orig_l2_addr", StringType)
    .add("resp_l2_addr", StringType)
    .add("vlan", IntegerType)
    .add("inner_vlan", IntegerType)
    .add("sensor", StringType)
}

{'sensor':'sensor_NOCCC',
  'resp_bytes': 161,
  'uid': 'CETkJrbj7HHggPfXe',
  'tunnel_parents': [],
  'duration': 24.617,
  'orig_l2_addr': '00:1e:be:79:7c:2f',
  'service': 'ssl',
  'proto': 'tcp',
  'resp_pkts': 12,
  'orig_pkts': 16,
  'timestamp': '1528182350.751834',
  'resp_ip_bytes': 962,
  'local_resp': True,
  'orig_ip_bytes': 1466,
  'vlan': 0,
  'local_orig': False,
  'missed_bytes': 0,
  'orig_bytes': 309,
  'inner_vlan': 0,
  'conn_state': 'SF',
  'source_port': 55081,
  'dest_ip': '10.200.3.50',
  'resp_l2_addr': '00:00:0c:07:ac:fe',
  'source_ip': '71.95.230.54',
  'dest_port': 443,
  'history': 'ShADadFf'}