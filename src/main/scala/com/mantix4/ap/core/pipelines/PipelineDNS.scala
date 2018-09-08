package com.mantix4.ap.core.pipelines

import com.mantix4.ap.abstracts.base.Pipeline
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders}
import com.mantix4.ap.abstracts.spark.SparkHelper
import com.mantix4.ap.core.logs.NetworkProtocols.DNS
import com.mantix4.ap.core.ml.AnomalyDetection

class PipelineDNS() extends Pipeline[DNS.DNS] {
  private val spark = SparkHelper.getSparkSession()
  import spark.implicits._

  override def startPipeline(dt: Dataset[DNS.DNS]): Unit = {
    // Debug only
    dt.show(100,truncate = false)

    // features = ['Z', 'rejected', 'proto', 'query', 'qclass_name', 'qtype_name', 'rcode_name', 'query_length']
    // features = ['Z', 'rejected', 'proto', 'query', 'qclass_name', 'qtype_name', 'rcode_name', 'query_length', 'answer_length', 'entropy']
    /*
    if log_type == 'dns':
            bro_df['query_length'] = bro_df['query'].str.len()
            bro_df['answer_length'] = bro_df['answers'].str.len()
            bro_df['entropy'] = bro_df['query'].map(lambda x: entropy(x))
     */

    /*
    # Add query length
        bro_df['query_length'] = bro_df['query'].str.len()

        # Normalize this field
        ql = bro_df['query_length']
        bro_df['query_length_norm'] = (ql - ql.min()) / (ql.max()-ql.min())
     */

    /*
    # Now use dataframe group by cluster
        show_fields = ['query', 'Z', 'proto', 'qtype_name', 'x', 'y', 'cluster']
        cluster_groups = bro_df[show_fields].groupby('cluster')
     */

  }

  override def customParsing(df: DataFrame): DataFrame = {
    df
      // Rename column normalization
      .withColumnRenamed("AA", "aa")
      .withColumnRenamed("TC", "tc")
      .withColumnRenamed("RD", "rd")
      .withColumnRenamed("RA", "ra")
      .withColumnRenamed("Z", "z")
      .withColumnRenamed("TTLs", "ttls")

      // Change column's to the righ type
      .withColumn("answers", split(col("answers"), ","))
      .withColumn("ttls", split(col("ttls"), ",").cast(ArrayType(DoubleType)))
  }

  override def getDataframeType(df: DataFrame): DataFrame = {
    df.withColumn("data",
      from_json($"value".cast(StringType), DNS.schemaBase))
      .select("data.*")
  }
}