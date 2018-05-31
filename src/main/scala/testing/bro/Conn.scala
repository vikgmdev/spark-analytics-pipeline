package testing.bro

import base.LogBase
import org.apache.spark.sql.types._

object Conn extends LogBase {

  override val topicName: String = "conn"

  val cassandraTable = "conn"

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
                  tunnel_parents: Vector[String]
                 ) extends Serializable

  val schemaBase: StructType = new StructType()
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
    .add("tunnel_parents", ArrayType(StringType))
}