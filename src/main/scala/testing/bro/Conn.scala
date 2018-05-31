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
                  duration: Option[Int],
                  orig_bytes: Option[Int],
                  resp_bytes: Option[Int],
                  conn_state: String,
                  local_orig: Option[Boolean],
                  local_resp: Option[Boolean],
                  missed_bytes: Option[Int],
                  history: String,
                  orig_pkts: Option[Int],
                  orig_ip_bytes: Option[Int],
                  resp_pkts: Option[Int],
                  resp_ip_bytes: Option[Int],
                  tunnel_parents: Option[Vector[String]]
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
    .add("duration", IntegerType)
    .add("orig_bytes", IntegerType)
    .add("resp_bytes", IntegerType)
    .add("conn_state", StringType)
    .add("local_orig", BooleanType)
    .add("local_resp", BooleanType)
    .add("missed_bytes", IntegerType)
    .add("history", StringType)
    .add("orig_pkts", IntegerType)
    .add("orig_ip_bytes", IntegerType)
    .add("resp_pkts", IntegerType)
    .add("resp_ip_bytes", IntegerType)
    .add("tunnel_parents", ArrayType(StringType))
}