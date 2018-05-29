package testing.bro

import base.LogBase
import org.apache.spark.sql.types._

object X509 extends LogBase {

  override val topicName: String = "x509"

  val cassandraTable = "x509"

  case class Simple (
                      timestamp: String,
                      id: String,
                      certificate_version: Option[Double],
                      certificate_serial: String,
                      certificate_subject: String,
                      certificate_issuer: String,
                      certificate_not_valid_before: String,
                      certificate_not_valid_after: String,
                      certificate_key_alg: String,
                      certificate_sig_alg: String,
                      certificate_key_type: String,
                      certificate_key_length: Option[Double],
                      certificate_exponent: String,
                      certificate_curve: String,
                      san_dns: Vector[String],
                      san_uri: Vector[String],
                      san_email: Vector[String],
                      san_ip: Vector[String],
                      basic_constraints_ca: Option[Boolean],
                      basic_constraints_path_len: Option[Double]
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("id", StringType)
    .add("certificate.version", DoubleType)
    .add("certificate.serial", StringType)
    .add("certificate.subject", StringType)
    .add("certificate.issuer", StringType)
    .add("certificate.not_valid_before", StringType)
    .add("certificate.not_valid_after", StringType)
    .add("certificate.key_alg", StringType)
    .add("certificate.sig_alg", StringType)
    .add("certificate.key_type", StringType)
    .add("certificate.key_length", DoubleType)
    .add("certificate.exponent", StringType)
    .add("certificate.curve", StringType)
    .add("san.dns", ArrayType(StringType))
    .add("san.uri", ArrayType(StringType))
    .add("san.email", ArrayType(StringType))
    .add("san.ip", ArrayType(StringType))
    .add("basic_constraints.ca", BooleanType)
    .add("basic_constraints.path_len", DoubleType)
}



