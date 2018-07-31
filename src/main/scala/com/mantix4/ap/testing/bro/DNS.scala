package com.mantix4.ap.testing.bro

import com.mantix4.ap.base.LogBase
import org.apache.spark.sql.types._

object DNS extends LogBase {

  override val topicName: String = "dns"

  val cassandraTable = "dns"

  case class Simple (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Option[Int],
                      dest_ip: String,
                      dest_port: Option[Int],
                      proto: String,
                      trans_id: Option[Int],
                      rtt: Option[Double],
                      query: String,
                      qclass: Option[Int],
                      qclass_name: String,
                      qtype: Option[Int],
                      qtype_name: String,
                      rcode: Option[Int],
                      rcode_name: String,
                      aa: Option[Boolean],
                      tc: Option[Boolean],
                      rd: Option[Boolean],
                      ra: Option[Boolean],
                      z: Option[Int],
                      answers: Vector[String],
                      ttls: Vector[Option[Double]],
                      rejected: Option[Boolean]
                ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", StringType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", StringType)
    .add("proto", StringType)
    .add("trans_id", StringType)
    .add("rtt", StringType)
    .add("query", StringType)
    .add("qclass", StringType)
    .add("qclass_name", StringType)
    .add("qtype", StringType)
    .add("qtype_name", StringType)
    .add("rcode", StringType)
    .add("rcode_name", StringType)
    .add("AA", StringType)
    .add("TC", StringType)
    .add("RD", StringType)
    .add("RA", StringType)
    .add("Z", StringType)
    .add("answers", ArrayType(StringType))
    .add("TTLs", ArrayType(StringType))
    .add("rejected", StringType)
}