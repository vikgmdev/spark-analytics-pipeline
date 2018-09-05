package com.mantix4.ap.core.logs.Files

import org.apache.spark.sql.types._

object X509 {
case class X509 (
                      timestamp: String,
                      id: String,
                      certificate_version: Option[Int],
                      certificate_serial: String,
                      certificate_subject: String,
                      certificate_issuer: String,
                      certificate_cn: String,
                      certificate_not_valid_before: String,
                      certificate_not_valid_after: String,
                      certificate_key_alg: String,
                      certificate_sig_alg: String,
                      certificate_key_type: String,
                      certificate_key_length: Option[Int],
                      certificate_exponent: String,
                      certificate_curve: String,
                      san_dns: Vector[String],
                      san_uri: Vector[String],
                      san_email: Vector[String],
                      san_ip: Vector[String],
                      san_other_fields:  Option[Boolean],
                      basic_constraints_ca: Option[Boolean],
                      basic_constraints_path_len: Option[Int]
                    ) extends Serializable

  val schemaBase: StructType = new StructType()
    .add("ts", StringType)
    .add("id", StringType)
    .add("certificate.version", IntegerType)
    .add("certificate.serial", StringType)
    .add("certificate.subject", StringType)
    .add("certificate.issuer", StringType)
    .add("certificate.cn", StringType)
    .add("certificate.not_valid_before", StringType)
    .add("certificate.not_valid_after", StringType)
    .add("certificate.key_alg", StringType)
    .add("certificate.sig_alg", StringType)
    .add("certificate.key_type", StringType)
    .add("certificate.key_length", IntegerType)
    .add("certificate.exponent", StringType)
    .add("certificate.curve", StringType)
    .add("san.dns", ArrayType(StringType))
    .add("san.uri", ArrayType(StringType))
    .add("san.email", ArrayType(StringType))
    .add("san.ip", ArrayType(StringType))
    .add("san.other_fields", BooleanType)
    .add("basic_constraints.ca", BooleanType)
    .add("basic_constraints.path_len", IntegerType)
}