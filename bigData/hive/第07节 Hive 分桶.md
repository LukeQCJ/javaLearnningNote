## 什么是分桶
Hive 使用另一种技术把数据划分成【多个可管理的部分】。这种技术就是之前提到过的分桶技术。

## 为什么要分桶
Hive 分区可以把hive表的数据分成多个文件/目录。但有些事情，分区也可能无能为力。

比如：
* 分区数不能太多，分区数量有限制
* 每个分区数据大小不一定相等

分区不可能适用于所有场景。
比如，基于地理位置（比如：国家）对表进行分区时，某些较大的国家将会具有较大分区，可能有四五个国家的数量占了所有数据的 70-80%。
而小国家的数据创建的分区则比较小。那么这时，给表做分区就不是个好主意。为了解决分区不平衡的问题，Hive 提出了分桶的概念。
它是另一种针对表数据划分的技术。

## Hive 分桶的特性
数据分桶原理是基于对分桶列做哈希计算，然后对哈希的结果和分桶数取模。分桶特性如下：

* 哈希函数取决于分桶列的类型。
* 具有相同分桶列的记录将始终存储在同一个桶中。
* 使用 clustered by 将表分成桶。
* 通常，在表目录中，每个桶只是一个文件，并且桶的编号是从 1 开始的。
* 可以先分区再分桶，也可以直接分桶。
* 此外，分桶表创建的数据文件大小几乎是一样的。

## 分桶的好处
* 与非分桶表相比，分桶表提供了高效采样。通过采样，我们可以尝试对一小部分数据进行查询，以便在原始数据集非常庞大时进行测试和调试。
* 由于数据文件的大小是几乎一样的，map 端的 join 在分桶表上执行的速度会比分区表快很多。
  在做 map 端 join 时，处理左侧表的 map 知道要匹配的右表的行在相关的桶中，因此只需要检索该桶即可。
* 分桶表查询速度快于非分桶表。
* 分桶的还提供了灵活性，可以使每个桶中的记录按一列或多列进行排序。 
  这使得 map 端 join 更加高效，因为每个桶之间的 join 变为更加高效的合并排序（merge-sort）。

## 分桶的局限性
分桶并不能确保数据加载的恰当性。数据加载到分桶的逻辑需要由我们自己处理。

## Hive 分桶示例
为了理解 Hive 分桶的其他特性，让我们来看一下下面的例子，为用户表创建分桶。
```text
first_name	last_name	address	country	city	state	post	phone1	phone2	email	web
Rebbecca	Didio	171 E 24th St	AU	Leith	TA	7315	03-8174-9123	0458-665-290	rebbecca.didio@hadoopdoc.com	http://hadoopdoc.com
```

下面我们将创建一个分区分桶表，以 country 字段为分区，以 state 字段分桶，并且以 city 字段升序排序。

### 分通表创建
我们可以利用 create table 语句里面的 clustered by 子句和 sorted by 子句来创建分桶表。

HiveQL 代码如下：
```text
CREATE TABLE bucketed_user(
  firstname VARCHAR(64),
  lastname  VARCHAR(64),
  address   STRING,
  city VARCHAR(64),
  state  VARCHAR(64),
  post      STRING,
  phone1    VARCHAR(64),
  phone2    STRING,
 email     STRING,
 web       STRING
)
COMMENT 'A bucketed sorted user table'
PARTITIONED BY (country VARCHAR(64))
CLUSTERED BY (state) SORTED BY (city) INTO 32 BUCKETS
STORED AS SEQUENCEFILE;
```
从上面创建表代码可知，我们为每个分区创建了 32 个桶。
需要注意的是分桶字段和排序字段需要出现在列定义中，而分区字段不能出现在列定义列表里面。

### 插入数据到分桶表
我们为分桶表或者分区表加载数据时，不能直接使用 load data 命令。
必须要用 insert overwrite table ... select ... from 语句从其他表把数据加载到分桶分区表。
所以我们需要在 Hive 创建一个临时表来先把数据加载进去，
然后再用 insert overwrite table ... select ... from 语句把数据插入到 分区分桶表。

假如我们已经创建了一个临时表，表名为 temp_user 。并且使用 load data 命令把数据先转载到临时表。
接下来就可以把数据插入到正式表了，HiveQL 如下：
```text
set hive.enforce.bucketing = true;
INSERT OVERWRITE TABLE bucketed_user PARTITION (country)
       SELECT firstname,
        lastname,
        address ,
        city,
        state,
        post,
        phone1,
       phone2,
       email,
       web,
       country
FROM temp_user;
```

一些重要的点：
* 在做分区的时候，hive.enforce.bucketing = true 和 hive.exec.dynamic.partition=true 发挥的作用有点像。
  前者是自动分桶，后者是自动分区。所以在插入数据的时候，我们可以通过设置 hive.enforce.bucketing 这个属性等于 true 
  来启用自动分区。
* 自动分桶会自动把 reduce 任务数设置成等于分桶数量，而分桶数量是在 create table 的语句中设置的。
  如上面创建语句中为 32 个分桶。另外还会根据 CLUSTERED BY 设置的字段分桶。
* 如果我们不在 Hive 会话中把 hive.enforce.bucketing 设置为 true，那么我们必须在上面的 INSERT ... SELECT语句的前面，
  需要把 reduce 任务数手动设置为 32（set mapred.reduce.tasks = 32），也就是说要确保 reduce 任务数和分桶数是一致的。

## 完整的代码
我们把完整的代码保存在 bucketed_user_creation.hql 文件。假如示例中的数据保存在 user_table.txt 中。
```text
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.max.dynamic.partitions.pernode=1000;
set hive.enforce.bucketing = true;
DROP TABLE IF EXISTS bucketed_user;
CREATE TEMPORARY TABLE temp_user(
      firstname VARCHAR(64),
     lastname  VARCHAR(64),
     address   STRING,
     country   VARCHAR(64),
     city      VARCHAR(64),
     state     VARCHAR(64),
     post      STRING,
     phone1    VARCHAR(64),
     phone2    STRING,
     email     STRING,
     web       STRING
 )
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
STORED AS TEXTFILE;
LOAD DATA LOCAL INPATH '/home/user/user_table.txt' INTO TABLE temp_user;
CREATE TABLE bucketed_user(
    firstname VARCHAR(64),
    lastname  VARCHAR(64),
    address   STRING,
    city     VARCHAR(64),
    state   VARCHAR(64),
    post      STRING,
    phone1    VARCHAR(64),
    phone2    STRING,
    email     STRING,
    web       STRING
 )
COMMENT 'A bucketed sorted user table'
PARTITIONED BY (country VARCHAR(64))
CLUSTERED BY (state) SORTED BY (city) INTO 32 BUCKETS
STORED AS SEQUENCEFILE;
set hive.enforce.bucketing = true;
INSERT OVERWRITE TABLE bucketed_user PARTITION (country)
SELECT firstname ,
       lastname ,
       address,
       city,
       state,
       post,
       phone1,
       phone2,
       email,
       web,
       country
FROM temp_user;
```

## 执行 bucketed_user_creation.hql 文件
HiveQL 代码可以被保存在文件，并在 hive 执行。
```text
$ hive -f bucketed_user_creation.hql
Logging initialized using configuration in jar:file:/home/user/bigdata/apache-hive-0.14.0-bin/lib/hive-common-0.14.0.jar!/hive-log4j.properties
OK
Time taken: 12.144 seconds
OK
Time taken: 0.146 seconds
Loading data to table default.temp_user
Table default.temp_user stats: [numFiles=1, totalSize=283212]
OK
Time taken: 0.21 seconds
OK
Time taken: 0.5 seconds
Query ID = user_20141222163030_3f024f2b-e682-4b08-b25c-7775d7af4134
Total jobs = 1
Launching Job 1 out of 1
Number of reduce tasks determined at compile time: 32
In order to change the average load for a reducer (in bytes):
set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
set mapreduce.job.reduces=<number>
Starting Job = job_1419243806076_0002, Tracking URL = http://tri03ws-
386:8088/proxy/application_1419243806076_0002/
Kill Command = /home/user/bigdata/hadoop-2.6.0/bin/hadoop job  -kill job_1419243806076_0002
Hadoop job information for Stage-1: number of mappers: 1; number of reducers: 32
2014-12-22 16:30:36,164 Stage-1 map = 0%,  reduce = 0%
2014-12-22 16:31:09,770 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 1.66 sec
2014-12-22 16:32:10,368 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 1.66 sec
2014-12-22 16:32:28,037 Stage-1 map = 100%,  reduce = 13%, Cumulative CPU 3.19 sec
2014-12-22 16:32:36,480 Stage-1 map = 100%,  reduce = 14%, Cumulative CPU 7.06 sec
2014-12-22 16:32:40,317 Stage-1 map = 100%,  reduce = 19%, Cumulative CPU 7.63 sec
2014-12-22 16:33:40,691 Stage-1 map = 100%,  reduce = 19%, Cumulative CPU 12.28 sec
2014-12-22 16:33:54,846 Stage-1 map = 100%,  reduce = 31%, Cumulative CPU 17.45 sec
2014-12-22 16:33:58,642 Stage-1 map = 100%,  reduce = 38%, Cumulative CPU 21.69 sec
2014-12-22 16:34:52,731 Stage-1 map = 100%,  reduce = 56%, Cumulative CPU 32.01 sec
2014-12-22 16:35:21,369 Stage-1 map = 100%,  reduce = 63%, Cumulative CPU 35.08 sec
2014-12-22 16:35:22,493 Stage-1 map = 100%,  reduce = 75%, Cumulative CPU 41.45 sec
2014-12-22 16:35:53,559 Stage-1 map = 100%,  reduce = 94%, Cumulative CPU 51.14 sec
2014-12-22 16:36:14,301 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 54.13 sec
MapReduce Total cumulative CPU time: 54 seconds 130 msec
Ended Job = job_1419243806076_0002
Loading data to table default.bucketed_user partition (country=null)
Time taken for load dynamic partitions : 2421
Loading partition {country=AU}
Loading partition {country=country}
Loading partition {country=US}
Loading partition {country=UK}
Loading partition {country=CA}
Time taken for adding to write entity : 17
Partition default.bucketed_user{country=AU} stats: [numFiles=32, numRows=500, totalSize=78268, rawDataSize=67936]
Partition default.bucketed_user{country=CA} stats: [numFiles=32, numRows=500, totalSize=76564, rawDataSize=66278]
Partition default.bucketed_user{country=UK} stats: [numFiles=32, numRows=500, totalSize=85604, rawDataSize=75292]
Partition default.bucketed_user{country=US} stats: [numFiles=32, numRows=500, totalSize=75468, rawDataSize=65383]
Partition default.bucketed_user{country=country} stats: [numFiles=32, numRows=1, totalSize=2865, rawDataSize=68]
MapReduce Jobs Launched:
Stage-Stage-1: Map: 1  Reduce: 32 Cumulative CPU: 54.13 sec   HDFS Read: 283505 HDFS Write: 316247 SUCCESS
Total MapReduce CPU Time Spent: 54 seconds 130 msec
OK
Time taken: 396.486 seconds
```

从以上日志我们可以了解到，MapReduce 作业有 32 个 reduce 任务对应 32 个分桶，并且自动创建了 4 个分区。