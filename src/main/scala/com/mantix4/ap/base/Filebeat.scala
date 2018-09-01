package com.mantix4.ap.base

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
    .add("message", StringType)
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
  "@timestamp": "2018-08-21T11:49:39.670Z",
  "@metadata": {
      "beat": "filebeat",
      "type": "doc",
      "version": "6.3.2",
      "topic": "network-assets"
  },
  "source": "/opt/development/network_assets/logs/network_assets.log",
  "offset": 366078,
  "message": "[2018/08/21 05:49:33] mod=mtu|cli=172.16.0.105/43188|srv=172.16.0.104/10180|subj=cli|link=Ethernet or modem|raw_mtu=1500",
  "prospector": {
      "type": "log"
  },
  "input": {
      "type": "log"
  },
  "beat": {
      "version": "6.3.2",
      "name": "node4",
      "hostname": "node4"
  },
  "host": {
      "name": "node4"
  }
}
*/