# Spark SQL DataFrame
DataFrame是一个【分布式数据集合】，它被组织成【命名列】。从概念上讲，它相当于具有良好优化技术的关系表。

DataFrame可以从不同来源的数组构造，例如Hive表，结构化数据文件，外部数据库或现有RDD。
这个API是为现代【大数据】和【数据科学】应用程序设计的，
Spark SQL的DataFrame设计灵感来自Python的Pandas和R语言的DataFrame数据结构。

## DataFrame的特性
下面是一些DataFrame的一些特征：

* 在单节点集群或者大集群，处理KB到PB级别的数据。
* 支持不同的数据格式(Avro，csv，ElasticSearch和Cassandra)和存储系统(HDFS，HIVE表，mysql等)。
* Spark SQL Catalyst 优化器。
* 可以通过Spark-Core轻松地与所有大数据工具和框架集成。
* 提供Python，Java，Scala和R等语言API。

## SparkSession
SparkSession是一个入口类，用于初始化Spark SQL的功能。

以下命令用于通过spark-shell初始化SparkSession。
```text
$ spark-shell
```

使用以下命令创建SQLContext。
```text
scala> import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SparkSession

scala> val spark=SparkSession
.builder()
.appName("My Spark SQL")
.getOrCreate()
19/04/25 14:40:31 WARN sql.SparkSession$Builder: Using an existing SparkSession; some configuration may not take effect.
spark: org.apache.spark.sql.SparkSession = org.apache.spark.sql.SparkSession@560465ea

scala> import spark.implicits._
import spark.implicits._
```
spark.implicits._主要用来隐式转换的，比如Rdd转DataFrame。

## DataFrame基本操作
DataFrame为【结构化数据】操作提供了一个【领域特定的语言（domain-specific language）】。
下面会提供一些DataFrame操作结构化数据的基本示例。

读取json文件并创建DataFrame，SQLContext.read.json方法返回的就是DataFrame。
```text
scala> val dfs = spark.read.json("hdfs:/tmp/employee.json")
dfs: org.apache.spark.sql.DataFrame = [age: string, id: string ... 1 more field]
```

注意：要先把employee.json文件上传到hdfs的tmp目录下。
```text
hdfs dfs -put employee.json /tmp
```

employee.json内容如下：
```text
[{"id" : "1201", "name" : "satish", "age" : "25"},
{"id" : "1202", "name" : "krishna", "age" : "28"},
{"id" : "1203", "name" : "amith", "age" : "39"},
{"id" : "1204", "name" : "javed", "age" : "23"},
{"id" : "1205", "name" : "prudvi", "age" : "23"}]
```

返回数据将会以age、id、name三个字段展示。
```text
dfs: org.apache.spark.sql.DataFrame = [age: string, id: string, name: string]
```

查看DataFrame数据。
```text
scala> dfs.show()
+---+----+-------+
|age|  id|   name|
+---+----+-------+
| 25|1201| satish|
| 28|1202|krishna|
| 39|1203|  amith|
| 23|1204|  javed|
| 23|1205| prudvi|
+---+----+-------+
```

使用printSchema方法查看DataFrame的数据模式。
```text
scala> dfs.printSchema()
root
|-- age: string (nullable = true)
|-- id: string (nullable = true)
|-- name: string (nullable = true)
```

使用select()函数查看某个列的数据。
```text
scala> dfs.select("name").show()
+-------+
|   name|
+-------+
| satish|
|krishna|
|  amith|
|  javed|
| prudvi|
+-------+
```

用filter函数查找年龄大于23（age> 23）的雇员。
```text
scala> dfs.filter(dfs("age")>23).show()
+---+----+-------+
|age|  id|   name|
+---+----+-------+
| 25|1201| satish|
| 28|1202|krishna|
| 39|1203|  amith|
+---+----+-------+
```

使用groupBy方法计算同一年龄的员工人数。类似SQL里面的group by语句。
```text
scala> dfs.groupBy("age").count().show()
+---+-----+
|age|count|
+---+-----+
| 28|    1|
| 23|    2|
| 25|    1|
| 39|    1|
+---+-----+
```

# Spark SQL数据源
Spark SQL支持读取很多种数据源，比如parquet文件，json文件，文本文件，数据库等。
下面列出了具体的一些数据源：
```text
Parquet文件
Orc文件
Json文件
Hive表
JDBC
```

先把people.json导入到hdfs的tmp目录下。people.json内容如下：
```text
{"name":"Michael","age":20}
{"name":"Andy", "age":30}
{"name":"Justin", "age":19}
```

## 通用的load/save函数
spark提供了通用的load和save函数用于加载和保存数据。支持多种数据格式：json，parquet，jdbc，orc，libsvm，csv，text等。
```text
scala> val peopleDF = spark.read.format("json").load("hdfs:/tmp/people.json")
peopleDF: org.apache.spark.sql.DataFrame = [age: bigint, name: string]

scala> peopleDF.select("name","age").write.format("parquet").save("/tmp/peoplenew.parquet")
```
默认的是parquet，可以通过spark.sql.sources.default，修改默认配置。

## Parquet文件
```text
scala> val parquetFileDF=spark.read.parquet("hdfs:/tmp/peoplenew.parquet")
parquetFileDF: org.apache.spark.sql.DataFrame = [name: string, age: bigint]

scala> peopleDF.write.parquet("/tmp/people.parquet")
```

## Orc文件
```text
scala> val df=spark.read.json("hdfs:/tmp/people.json")
df: org.apache.spark.sql.DataFrame = [age: bigint, name: string]

scala> df.write.mode("append").orc("/tmp/people")
```
在hdfs查看/tmp/people目录
```text
$ hdfs dfs -ls /tmp/people
Found 2 items
-rw-r--r--   3 ccpgdev supergroup          0 2019-04-25 17:24 /tmp/people/_SUCCESS
-rw-r--r--   3 ccpgdev supergroup        343 2019-04-25 17:24 /tmp/people/part-00000-3eea0d3e-4349-4cc0-90c7-45c423069284-c000.snappy.orc
```

spark sql 读取orc文件
```text
scala> spark.read.orc("/tmp/people").show()
+---+-------+
|age|   name|
+---+-------+
| 20|Michael|
| 30|   Andy|
| 19| Justin|
+---+-------+
```

## Json文件
```text
scala> val df=spark.read.json("hdfs:/tmp/people.json")
df: org.apache.spark.sql.DataFrame = [age: bigint, name: string]
scala> df.write.mode("overwrite").json("/tmp/peoplejson/")
scala> spark.read.json("/tmp/peoplejson/").show()
+---+-------+
|age|   name|
+---+-------+
| 20|Michael|
| 30|   Andy|
| 19| Justin|
+---+-------+

$ hdfs dfs -ls /tmp/peoplejson
Found 2 items
-rw-r--r--   3 ccpgdev supergroup          0 2019-04-25 17:28 /tmp/peoplejson/_SUCCESS
-rw-r--r--   3 ccpgdev supergroup         80 2019-04-25 17:28 /tmp/peoplejson/part-00000-52a02853-e85b-45eb-ba66-4ab92c3f7142-c000.json
```

## Hive表
Spark 1.6及以前的版本使用hive表需要hiveContext。Spark 2.0开始只需要创建sparkSession增加enableHiveSupport()即可。

先在Hive的tmp库下新建一张表tmp_building_num，并插入一些数据。
```text
hive> create table tmp_building_num(x int);
OK
Time taken: 0.127 seconds

hive> insert into tmp_building_num values(1);
Query ID = ccpgdev_20190425174242_bff1a3ed-b02c-47e7-bb11-8a05eb5c70c1
...省略日志...
Stage-Stage-1: Map: 1   Cumulative CPU: 4.73 sec   HDFS Read: 3196 HDFS Write: 78 SUCCESS
Total MapReduce CPU Time Spent: 4 seconds 730 msec
OK
Time taken: 22.339 seconds

hive> select * from tmp_building_num;
OK
1
Time taken: 0.154 seconds, Fetched: 1 row(s)

scala> import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SparkSession
scala> val spark = SparkSession.
| builder().
| enableHiveSupport().
| getOrCreate()
19/04/25 17:38:48 WARN sql.SparkSession$Builder: Using an existing SparkSession; some configuration may not take effect.
spark: org.apache.spark.sql.SparkSession = org.apache.spark.sql.SparkSession@498f1f63

scala> spark.sql("select count(1) from tmp.tmp_building_num").show();
+--------+
|count(1)|
+--------+
|     1  |
+--------+
```

## JDBC
数据写入mysql。
```text
scala> df.repartition(1).write.mode("append").option("user", "root")
.option("password", "password").jdbc("jdbc:mysql://localhost:3306/test","alluxio",new Properties())
```

从mysql里读数据。
```text
scala> val fromMysql = spark.read.option("user", "root")
.option("password", "password").jdbc("jdbc:mysql://localhost:3306/test","alluxio",new Properties())
```




