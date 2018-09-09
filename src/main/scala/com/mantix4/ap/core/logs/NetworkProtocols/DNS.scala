package com.mantix4.ap.core.logs.NetworkProtocols

import org.apache.spark.sql.types._

object DNS {
  case class DNS (
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
                      answers: Option[Vector[String]],
                      ttls: Option[Vector[Double]],
                      rejected: Option[Boolean],
                      total_answers: Option[Int],
                      total_replies: Option[Int],
                      saw_query: Option[Boolean],
                      saw_reply: Option[Boolean],
                      sensor: String,
                      query_length: Option[Int],
                      query_length_norm: Option[Int],
                      answer_length: Option[Int]
                ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("timestamp", StringType)
    .add("uid", StringType)
    .add("source_ip", StringType)
    .add("source_port", IntegerType)
    .add("dest_ip", StringType)
    .add("dest_port", IntegerType)
    .add("proto", StringType)
    .add("trans_id", IntegerType)
    .add("rtt", DoubleType)
    .add("query", StringType)
    .add("qclass", IntegerType)
    .add("qclass_name", StringType)
    .add("qtype", IntegerType)
    .add("qtype_name", StringType)
    .add("rcode", IntegerType)
    .add("rcode_name", StringType)
    .add("AA", BooleanType)
    .add("TC", BooleanType)
    .add("RD", BooleanType)
    .add("RA", BooleanType)
    .add("Z", IntegerType)
    .add("answers", StringType)
    // .add("answers", ArrayType(StringType))
    .add("TTLs", StringType)
    // .add("TTLs",ArrayType(DoubleType))
    .add("rejected", BooleanType)
    .add("total_answers", IntegerType)
    .add("total_replies", IntegerType)
    .add("saw_query", BooleanType)
    .add("saw_reply", BooleanType)
    .add("sensor", StringType)
}