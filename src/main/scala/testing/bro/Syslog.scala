package testing.bro

import base.LogBase
import org.apache.spark.sql.types._

object Syslog extends LogBase {

  override val topicName: String = "syslog"

  val cassandraTable = "syslog"

  case class Simple (
                        timestamp: String,
                        uid: String,
                        source_ip: String,
                        source_port: Option[Int],
                        dest_ip: String,
                        dest_port: Option[Int],
                        proto: String,
                        facility: String,
                        severity: String,
                        message: String
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("uid", StringType)
    .add("id.orig_h", StringType)
    .add("id.orig_p", StringType)
    .add("id.resp_h", StringType)
    .add("id.resp_p", StringType)
    .add("proto", StringType)
    .add("facility", StringType)
    .add("severity", StringType)
    .add("message", StringType)
}

