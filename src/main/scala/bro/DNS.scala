package bro

import java.sql.Timestamp

import com.datastax.spark.connector.SomeColumns
import org.apache.spark.sql.types._

object DNS {

  val cassandraTable = "dns"

  case class Simple (
                      timestamp: String,
                      uid: String,
                      source_ip: String,
                      source_port: Integer,
                      dest_ip: String,
                      dest_port: Integer,
                      proto: String,
                      trans_id: Integer,
                      rtt: Double,
                      query: String,
                      qclass: Integer,
                      qclass_name: String,
                      qtype: Integer,
                      qtype_name: String,
                      rcode: Integer,
                      rcode_name: String,
                      sensor: String,
                      AA: Boolean,
                      TC: Boolean,
                      RD: Boolean,
                      RA: Boolean,
                      Z: Integer,
                      answers: Set[String],
                      TTLs: Set[Double],
                      rejected: Boolean
                ) extends Serializable

  val cassandraColumns = SomeColumns(
    "timestamp",
    "uid",
    "source_ip",
    "source_port",
    "dest_ip",
    "dest_port",
    "proto",
    "trans_id",
    "rtt",
    "query",
    "qclass",
    "qclass_name",
    "qtype",
    "qtype_name",
    "rcode",
    "rcode_name",
    "AA",
    "TC",
    "RD",
    "RA",
    "Z",
    "answers",
    "TTLs",
    "rejected"
  )

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
    .add("answers", ArrayType(StringType))
    .add("TTLs", ArrayType(DoubleType))
    .add("rejected", BooleanType)
    .add("sensor", StringType)
    .add("type", StringType)
}

  /*
CREATE TABLE dns (
  timestamp timestamp PRIMARY KEY,
  uid text,
  source_ip inet,
  source_port int,
  dest_ip inet,
  dest_port int,
  proto text,
  trans_id int,
  rtt double,
  query text,
  qclass int,
  qclass_name text,
  qtype int,
  qtype_name text,
  rcode int,
  rcode_name text,
  AA boolean,
  TC boolean,
  RD boolean,
  RA boolean,
  Z int,
  answers set<text>,
  TTLs set<double>,
  rejected boolean
);
  */