object SessionizeElb {
  def main(args: Array[String]) {

val conf = new SparkConf().setAppName("SessionizeElb");
val sc = new SparkContext(conf)

val sqlContext= new org.apache.spark.sql.sqlContext(sc);

import sqlContext.implicits._


val file = sc.textFile(args(0));

val windowDuration = args(1).toLong ; //in Seconds 15 mins

val dataCols = file.map(x => x.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)); //Splitted the line with space as delimiter. Ignoring spaces in between Double Quotes

val recordsOK = dataCols.filter(_.length >= 12); // Records which contain more than 12 fields

val recordsBad = dataCols.filter(_.length < 12); // Records which contain less than 12 fields

val dataColsOK = recordsOK.map(p => (p(0),p(2).split(":")(0),p(11),p(12))).toDF

val data = dataColsOK.select($"_1".as("Timestamp"),$"_2".as("client"),$"_3".as("request"),$"_4".as("user_agent")); // Renamed columns from default dataframe

import org.apache.spark.sql.functions._

val ts = unix_timestamp(substring($"Timestamp",0,19), "yyyy-MM-dd'T'HH:mm:ss").cast("timestamp").cast("long"); // Ignored MicroSeconds from the Timestamp column - hence taken substring. Converted it to Long

// Round to windowDuration seconds interval
val interval = (round(ts / windowDuration) * windowDuration).cast("timestamp").alias("interval") // this is used to transform the timestamp to interval start time.

val perWindowUser = data.groupBy(interval,$"Client",$"user_agent").agg(max(ts).cast("timestamp").as("MaxTS"),min(ts).cast("timestamp").as("MinTS"),((max(ts) - min(ts))).as("Duration(Sec)"),countDistinct($"request").as("UniqRequestCount")); // this gives the Max Time, Min Time, Duration , Unique Request Count for each user in each interval.

val perUser = perWindowUser.groupBy($"Client",$"user_agent").agg(avg($"Duration(Sec)").as("AvgDuration"),sum($"Duration(Sec)").as("TotalDuration")).sort(desc("TotalDuration")); // this gives the Avg Duration and Total Duration of each user. The result is sorted in descending order of Total Duration to get the most engaged users.

perWindow.write.parquet(args(2));
perUser.write.parquet(args(3));
sc.stop();
  }
  
}