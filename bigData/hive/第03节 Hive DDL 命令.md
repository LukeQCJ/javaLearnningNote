DDL 即数据定义语言，比如创建或者删除数据库命令，创建或删除表命令等。

下面来详细看一下 Hive DDL 相关命令，并给出示例。

## 库操作

### 创建库
#### 语法：
```text
CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_name
　　[COMMENT database_comment]　　　　　　//关于数据块的描述
　　[LOCATION hdfs_path]　　　　　　　　　　//指定数据库在HDFS上的存储位置
　　[WITH DBPROPERTIES (property_name=property_value, ...)];　　　　//指定数据块属性
```

默认地址：**/user/hive/warehouse/db_name.db/table_name/partition_name/…**

#### 示例：
创建普通数据库
```text
hive> create database t1;
OK
Time taken: 0.057 seconds
hive> show databases;
OK
t1
default
Time taken: 0.018 seconds, Fetched: 21 row(s)
```

创建库的时候检查存在与否
```text
hive> create database if not exists tmp;
OK
Time taken: 0.014 seconds
```

创建库的时候带注释
```text
hive> create database if not exists t2 comment 'learning hive';
OK
Time taken: 0.039 seconds
```

利用 desc 查看数据库信息
```text
hive  > desc database t2;
OK
t2      learning hive   hdfs://myhive/user/hive/warehouse/t2.db      root      USER
Time taken: 0.025 seconds, Fetched: 1 row(s)
```

创建带属性的库
```text
hive> create database if not exists t3 with dbproperties('creator'='hadoop','date'='2019-06-25');
OK
Time taken: 0.047 seconds
```

### 查看库
#### 语法：
```text
show databases;
```

#### 示例：
查看有哪些数据库
```text
hive> show databases;
OK
t1
t2
t3
default
Time taken: 0.015 seconds, Fetched: 4 row(s)
```

### 查看数据库的详细属性信息
#### 语法：
```text
desc database [extended] dbname;
```

#### 示例：
```text
hive> desc database extended t3;
OK
t3  hdfs://myhive/user/hive/warehouse/t3.db  root  USER   {date=2019-06-25, creator=hadoop}
Time taken: 0.017 seconds, Fetched: 1 row(s)
```

### 查看正在使用哪个库
```text
hive> select current_database();
OK
default
Time taken: 0.722 seconds, Fetched: 1 row(s)
```

### 删除库
#### 语法：
```text
drop database dbname;
drop database if exists dbname;
```
默认情况下，hive 不允许删除包含表的数据库，有两种解决办法：
1、 手动删除库下所有表，然后删除库。
2、 使用 cascade 关键字
```text
drop database if exists dbname cascade;
```

### 切换库
#### 语法：
```text
use database_name
```

#### 示例：
```text
hive> select current_database();
OK
default
Time taken: 0.092 seconds, Fetched: 1 row(s)
hive> use t3;
OK
Time taken: 0.019 seconds
hive> select current_database();
OK
t3
Time taken: 0.089 seconds, Fetched: 1 row(s)
```

## 表操作

### 语法：
```text
CREATE [EXTERNAL] TABLE [IF NOT EXISTS] table_name
　　[(col_name data_type [COMMENT col_comment], ...)]
　　[COMMENT table_comment]
　　[PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]
　　[CLUSTERED BY (col_name, col_name, ...)
　　　　[SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS]
　　[ROW FORMAT row_format]
　　[STORED AS file_format]
　　[LOCATION hdfs_path]
```

各个关键词含义如下：
```text
CREATE TABLE
创建一个指定名字的表。如果相同名字的表已经存在，则抛出异常；用户可以用 IF NOT EXIST 选项来忽略这个异常。

EXTERNAL
关键字可以让用户创建一个外部表，在建表的同时指定一个指向实际数据的路径（LOCATION）

LIKE
允许用户复制现有的表结构，但是不复制数据

COMMENT
可以为表与字段增加描述

PARTITIONED BY
指定分区

ROW FORMAT
　　DELIMITED [FIELDS TERMINATED BY char] [COLLECTION ITEMS TERMINATED BY char]
　　　　MAP KEYS TERMINATED BY char] [LINES TERMINATED BY char]
　　　　| SERDE serde_name [WITH SERDEPROPERTIES
　　　　(property_name=property_value, property_name=property_value, …)]
用户在建表的时候可以自定义 SerDe 或者使用自带的 SerDe。如果没有指定 ROW FORMAT 或者 ROW FORMAT DELIMITED，
将会使用自带的 SerDe。在建表的时候，用户还需要为表指定列，用户在指定表的列的同时也会指定自定义的 SerDe，
Hive 通过 SerDe 确定表的具体的列的数据。

STORED AS
　　SEQUENCEFILE //序列化文件
　　| TEXTFILE //普通的文本文件格式
　　| RCFILE　　//行列存储相结合的文件
| parquet
　　| INPUTFORMAT input_format_classname OUTPUTFORMAT output_format_classname //自定义文件格式

如果文件数据是纯文本，可以使用 STORED AS TEXTFILE。如果数据需要压缩，使用 STORED AS SEQUENCE

LOCATION
指定表在HDFS的存储路径
```

### 示例：
创建默认的内部表
```text
hive> create table student(id int, name string, sex string, age int,department string) 
> row format delimited fields terminated by ",";
OK
Time taken: 0.218 seconds
```


外部表
```text
hive> create external table student_ext
> (id int, name string, sex string, age int,department string) 
> row format delimited fields terminated by "," 
> location "/hive/student";
OK
Time taken: 0.161 seconds
```

分区表
```text
hive> create external table student_ptn(id int, name string, sex string, age int,department string)
> partitioned by (city string)
> row format delimited fields terminated by ","
> location "/hive/student_ptn";
OK
Time taken: 0.161 seconds
```

添加分区
```text
hive> alter table student_ptn add partition(city="beijing");
OK
Time taken: 0.222 seconds
hive> alter table student_ptn add partition(city="shenzhen");
OK
Time taken: 0.169 seconds
```

分桶表
```text
hive> create external table student_bck(id int, name string, sex string, age int,department string)
> clustered by (id) sorted by (id asc, name desc) into 4 buckets
> row format delimited fields terminated by ","
> location "/hive/student_bck";
OK
Time taken: 0.174 seconds
```

使用 create table as select 创建表
```text
hive > load data local inpath "/home/hadoop/student.txt" into table student;
Time taken 0.715 seconds
hive > select * from student;
+-------------+---------------+--------------+--------------+---------------------+
| student.id  | student.name  | student.sex  | student.age  | student.department  |
+-------------+---------------+--------------+--------------+---------------------+
| 95002       | 刘晨            | 女            | 19           | IS                  |
| 95017       | 王风娟           | 女            | 18           | IS                  |
| 95018       | 王一            | 女            | 19           | IS                  |
| 95013       | 冯伟            | 男            | 21           | CS                  |
| 95014       | 王小丽           | 女            | 19           | CS                  |
| 95019       | 邢小丽           | 女            | 19           | IS                  |
| 95020       | 赵钱            | 男            | 21           | IS                  |
| 95003       | 王敏            | 女            | 22           | MA                  |
| 95004       | 张立            | 男            | 19           | IS                  |
| 95012       | 孙花            | 女            | 20           | CS                  |
| 95010       | 孔小涛           | 男            | 19           | CS                  |
| 95005       | 刘刚            | 男            | 18           | MA                  |
| 95006       | 孙庆            | 男            | 23           | CS                  |
| 95007       | 易思玲           | 女            | 19           | MA                  |
| 95008       | 李娜            | 女            | 18           | CS                  |
| 95021       | 周二            | 男            | 17           | MA                  |
| 95022       | 郑明            | 男            | 20           | MA                  |
| 95001       | 李勇            | 男            | 20           | CS                  |
| 95011       | 包小柏           | 男            | 18           | MA                  |
| 95009       | 梦圆圆           | 女            | 18           | MA                  |
| 95015       | 王君            | 男            | 18           | MA                  |
+-------------+---------------+--------------+--------------+---------------------+
21 rows selected (0.342 seconds)
hive > create table student_ctas as select * from student where id < 95012;
Time taken 34.514 seconds
hive > select * from student_ctas;
+------------------+--------------------+-------------------+-------------------+--------------------------+
| student_ctas.id  | student_ctas.name  | student_ctas.sex  | student_ctas.age  | student_ctas.department  |
+------------------+--------------------+-------------------+-------------------+--------------------------+
| 95002            | 刘晨                 | 女                 | 19                | IS                       |
| 95003            | 王敏                 | 女                 | 22                | MA                       |
| 95004            | 张立                 | 男                 | 19                | IS                       |
| 95010            | 孔小涛                | 男                 | 19                | CS                       |
| 95005            | 刘刚                 | 男                 | 18                | MA                       |
| 95006            | 孙庆                 | 男                 | 23                | CS                       |
| 95007            | 易思玲                | 女                 | 19                | MA                       |
| 95008            | 李娜                 | 女                 | 18                | CS                       |
| 95001            | 李勇                 | 男                 | 20                | CS                       |
| 95011            | 包小柏                | 男                 | 18                | MA                       |
| 95009            | 梦圆圆                | 女                 | 18                | MA                       |
+------------------+--------------------+-------------------+-------------------+--------------------------+
11 rows selected (0.445 seconds)
```

### 查看表
查看当期使用的数据库有哪些表
```text
hive > show tables;
OK
ss
student
student_bck
student_ext
student_ptn
Time taken: 0.019 seconds, Fetched: 5 row(s)
```

查看非当前使用的数据库中有哪些表
```text
hive> show tables in default;
OK
customers
Time taken: 0.02 seconds, Fetched: 1 row(s)
```

查看数据库中以xxx开头的表
```text
hive> show tables like 'student*';
OK
student
student_bck
student_ext
student_ptn
Time taken: 0.018 seconds, Fetched: 4 row(s)
```

查看表信息
```text
hive> desc student_bck;
OK
id                      int                                         
name                    string                                      
sex                     string                                      
age                     int                                         
department              string                                      
Time taken: 0.079 seconds, Fetched: 5 row(s)
```

查看表详细信息（格式不友好）
```text
hive> desc extended student;
OK
id                      int                                         
name                    string                                      
sex                     string                                      
age                     int                                         
department              string                                      
Detailed Table Information      Table(tableName:student, dbName:t3, owner:root, createTime:1561444295, lastAccessTime:0, retention:0, sd:StorageDescriptor(cols:[FieldSchema(name:id, type:int, comment:null), FieldSchema(name:name, type:string, comment:null), FieldSchema(name:sex, type:string, comment:null), FieldSchema(name:age, type:int, comment:null), FieldSchema(name:department, type:string, comment:null)], location:hdfs://cctvdbaapp00:8020/user/hive/warehouse/t3.db/student, inputFormat:org.apache.hadoop.mapred.TextInputFormat, outputFormat:org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat, compressed:false, numBuckets:-1, serdeInfo:SerDeInfo(name:null, serializationLib:org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe, parameters:{field.delim=,, serialization.format=,}), bucketCols:[], sortCols:[], parameters:{}, skewedInfo:SkewedInfo(skewedColNames:[], skewedColValues:[], skewedColValueLocationMaps:{}), storedAsSubDirectories:false), partitionKeys:[], parameters:{transient_lastDdlTime=1561444295}, viewOriginalText:null, viewExpandedText:null, tableType:MANAGED_TABLE)
Time taken: 0.076 seconds, Fetched: 7 row(s)
```

查看表详细信息（格式友好）
```text
hive> desc formatted student;
OK
# col_name              data_type               comment
id                      int                                         
name                    string                                      
sex                     string                                      
age                     int                                         
department              string
# Detailed Table Information
Database:               t3                       
Owner:                  root                     
CreateTime:             Tue Jun 25 14:31:35 CST 2019     
LastAccessTime:         UNKNOWN                  
Protect Mode:           None                     
Retention:              0                        
Location:               hdfs://myhive/user/hive/warehouse/t3.db/student       
Table Type:             MANAGED_TABLE            
Table Parameters:                
transient_lastDdlTime   1561444295
# Storage Information
SerDe Library:          org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe       
InputFormat:            org.apache.hadoop.mapred.TextInputFormat         
OutputFormat:           org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat       
Compressed:             No                       
Num Buckets:            -1                       
Bucket Columns:         []                       
Sort Columns:           []                       
Storage Desc Params:             
field.delim             ,                   
serialization.format    ,                   
Time taken: 0.394 seconds, Fetched: 31 row(s)
```

查看表分区信息
```text
hive> show partitions student_ptn;
OK
city=beijing
city=shenzhen
Time taken: 0.079 seconds, Fetched: 2 row(s)
```

查看表的详细建表语句
```text
hive> show create table student_ptn;
OK
CREATE EXTERNAL TABLE `student_ptn`(
`id` int,
`name` string,
`sex` string,
`age` int,
`department` string)
PARTITIONED BY (
`city` string)
ROW FORMAT SERDE
'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
WITH SERDEPROPERTIES (
'field.delim'=',',
'serialization.format'=',')
STORED AS INPUTFORMAT
'org.apache.hadoop.mapred.TextInputFormat'
OUTPUTFORMAT
'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION
'hdfs://cctvdbaapp00:8020/hive/student_ptn'
TBLPROPERTIES (
'transient_lastDdlTime'='1561444415')
Time taken: 0.121 seconds, Fetched: 21 row(s)
```

### 修改表

#### 修改表名
```text
hive> alter table student rename to new_student;
```

#### 增加字段
```text
alter table new_student add columns (score int);
```

#### 修改一个字段的定义
```text
alter table new_student change name new_name string;
```

#### 删除字段
```text
hive 不支持删除字段
```

#### 替换所有字段
```text
alter table new_student replace columns (id int, name string, address string);
```

### 添加分区

#### 新增一个分区
```text
alter table student_ptn add partition(city="chongqing");
```

#### 新增多个分区
```text
alter table student_ptn add partition(city="chongqing2") partition(city="chongqing3") partition(city="chongqing4");
```

#### 添加动态分区
select 的最后一个字段会被作为动态分区字段
```text
insert overwrite table student_ptn_age partition(age)
select id,name,sex,department，age from student_ptn;
```

#### 修改分区
一般来说，修改分区一般都是指修改分区的【数据存储目录】。

在【添加分区】的时候，直接指定当前分区的数据存储目录
```text
alter table student_ptn add if not exists 
partition(city='beijing') location '/student_ptn_beijing' 
partition(city='cc') location '/student_cc';
```

修改【已经指定好的分区】的数据存储目录
```text
alter table student_ptn partition (city='beijing') set location '/student_ptn_beijing';
```
此时原先的分区文件夹仍存在，但是在往分区添加数据时，只会添加到新的分区目录

#### 删除分区
```text
alter table student_ptn drop partition (city='beijing');
```

### 删除表
```text
drop table new_student;
```

### 清空表
```text
truncate table student_ptn;
```

### 其他辅助命令
```text
show databases;show databases like 'my*' --查看数据库列表

show tables;show tables in db_name --查看数据表

show create table table_name --查看数据表的建表语句

show functions;-- 查看 hive 函数列表

show partitions table_name;show partitions table_name partition (city='bj') --查看 hive 表分区

desc table_name;desc extended table_name;desc formatted table_name; --查看表的详细信息（元数据信息）

desc database db_name;desc database extended db_name --查看数据库的详细属性信息

truncate table table_name --清空数据表
```