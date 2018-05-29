package testing.bro

import base.LogBase
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
                      rejected: Option[Boolean],
                      cyrin_class: String,
                      cyrin_rule: String,
                      cyrin_id: String,
                      cyrin_child: String,
                      cyrin_severity: String,
                      cyrin_confidence: String,
                      cyrin_type: String,
                      cyrin_stage: String
                ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", IntegerType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", IntegerType)
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
    .add("answers", ArrayType(StringType))
    .add("TTLs", ArrayType(DoubleType))
    .add("rejected", BooleanType)
    .add("cyrin_class", StringType)
    .add("cyrin_rule", StringType)
    .add("cyrin_id", StringType)
    .add("cyrin_child", StringType)
    .add("cyrin_severity", StringType)
    .add("cyrin_confidence", StringType)
    .add("cyrin_type", StringType)
    .add("cyrin_stage", StringType)
}