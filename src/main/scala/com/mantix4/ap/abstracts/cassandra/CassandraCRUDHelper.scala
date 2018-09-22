package com.mantix4.ap.abstracts.cassandra

import com.datastax.spark.connector._
import com.datastax.spark.connector.writer.SqlRowWriter
import com.mantix4.ap.abstracts.spark.SparkHelper
import org.apache.spark.sql.DataFrame


object CassandraCRUDHelper {

  implicit class DataFrameTransforms(df: DataFrame) {

    def saveToCassandra(): DataFrame = {
      implicit val rowWriter: SqlRowWriter.Factory.type = SqlRowWriter.Factory
      df.rdd.saveToCassandra(SparkHelper.sensor_name, "conn", SomeColumns("timestamp",
        "uid",
        "source_ip",
        "source_port",
        "dest_ip",
        "dest_port",
        "proto",
        "service",
        "duration",
        "orig_bytes",
        "resp_bytes",
        "conn_state",
        "local_orig",
        "local_resp",
        "missed_bytes",
        "history",
        "orig_pkts",
        "orig_ip_bytes",
        "resp_pkts",
        "resp_ip_bytes",
        "tunnel_parents",
        "orig_l2_addr",
        "resp_l2_addr",
        "vlan",
        "inner_vlan",
        "sensor",
        "direction",
        "pcr"))
      df
    }
  }
}
