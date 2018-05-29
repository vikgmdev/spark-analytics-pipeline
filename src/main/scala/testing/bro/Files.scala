package testing.bro

import base.LogBase
import org.apache.spark.sql.types._

object Files extends LogBase {

  override val topicName: String = "files"

  val cassandraTable = "files"

  case class Simple (
                  ts: String,
                  fuid: String,
                  tx_hosts: Vector[String],
                  rx_hosts: Vector[String],
                  conn_uids: Vector[String],
                  source: String,
                  depth: Option[Double],
                  analyzers: Vector[String],
                  mime_type: String,
                  filename: String,
                  duration: Option[Double],
                  local_orig: Option[Boolean],
                  is_orig: Option[Boolean],
                  seen_bytes: Option[Double],
                  total_bytes: Option[Double],
                  missing_bytes: Option[Double],
                  overflow_bytes: Option[Double],
                  timedout: Option[Boolean],
                  parent_fuid: String,
                  md5: String,
                  sha1: String,
                  sha256: String,
                  extracted: String,
                  orig_h: String,
                  resp_h: String,
                  proto: String,
                  http_user_agent: String,
                  http_referrer: String,
                  http_host: String,
                  http_uri: String,
                  filename_http: String
                 ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("fuid", StringType)
    .add("tx_hosts", ArrayType(StringType))
    .add("rx_hosts", ArrayType(StringType))
    .add("conn_uids", ArrayType(StringType))
    .add("source", StringType)
    .add("depth", DoubleType)
    .add("analyzers", ArrayType(StringType))
    .add("mime_type", StringType)
    .add("filename", StringType)
    .add("duration", DoubleType)
    .add("local_orig", BooleanType)
    .add("is_orig", BooleanType)
    .add("seen_bytes", DoubleType)
    .add("total_bytes", DoubleType)
    .add("missing_bytes", DoubleType)
    .add("overflow_bytes", DoubleType)
    .add("timedout", BooleanType)
    .add("parent_fuid", StringType)
    .add("md5", StringType)
    .add("sha1", StringType)
    .add("sha256", StringType)
    .add("extracted", StringType)
    .add("orig_h", StringType)
    .add("resp_h", StringType)
    .add("proto", StringType)
    .add("http_user_agent", StringType)
    .add("http_referrer", StringType)
    .add("http_host", StringType)
    .add("http_uri", StringType)
    .add("filename_http", StringType)
}

