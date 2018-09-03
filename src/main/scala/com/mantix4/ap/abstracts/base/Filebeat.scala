package com.mantix4.ap.abstracts.base

import org.apache.spark.sql.types._

object Filebeat {

  val schemaBase: StructType = new StructType()
    .add("@timestamp", StringType)
    .add("@metadata", new StructType()
      .add("beat", StringType)
      .add("type", StringType)
      .add("version", StringType)
      .add("topic", StringType))
    .add("source", StringType)
    .add("offset", IntegerType)
    .add("json", StringType)
    .add("prospector", new StructType()
      .add("type", StringType))
    .add("input", new StructType()
      .add("type", StringType))
    .add("beat", new StructType()
      .add("version", StringType)
      .add("name", StringType)
      .add("hostname", StringType))
    .add("host", new StructType()
      .add("name", StringType))
}

/*
{
  "@timestamp":"2018-09-02T11:28:44.163Z",
  "@metadata":{
    "beat":"filebeat",
    "type":"doc",
    "version":"6.3.2",
    "topic":"p0f-topic"
    },
  "offset":61207,
  "prospector":{
    "type":"log"
    },
  "input":{
    "type":"log"
    },
  "beat":{
    "name":"node4",
    "hostname":"node4",
    "version":"6.3.2"
    },
  "host":{
    "name":"node4"
    },
  "json":{
    "srv":"31.13.78.17/443",
    "mod":"syn+ack",
    "subj":"srv",
    "date":"2018/09/02 11:24:02",
    "cli":"192.168.10.24/56802",
    "os":"???",
    "dist":"43",
    "raw_sig":"4:85+?:0:1410:mss*20,8:mss,sok,ts,nop,ws:df:0",
    "params":"excess_dist"
  },
  "source":"/opt/development/network_assets/logs/network_assets-p0f-2.log"
}
*/