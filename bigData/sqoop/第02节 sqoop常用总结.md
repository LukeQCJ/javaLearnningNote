## 介绍
```text
1、Sqoop的导入导出
导入： RDBMS -----> Hadoop平台
导出:  Hadoop平台 -----> RDBMS

2、Sqoop的重点是写导入导出的命令
3、Sqoop的底层是没有Reduce的MR
Sqoop这是机械性将数据源的数据一条条进行搬移，不需要对数据做额外的聚合，所以不需要Reduce
```

## 测试
```text
# 测试你的sqoop是否能查看MySQL中所有的数据库
sqoop list-databases \
--connect jdbc:mysql://hadoop0	1:3306/ \
--username root \
--password 123456
```

## 应用1-导入
数据导入到HDFS-全量。
```text
# split-by后边的字段是数字类型
sqoop import \
--connect jdbc:mysql://192.168.88.80:3306/userdb \
--table emp \
--username root \
--password 123456 \
--target-dir /sqoop/result3 \
--delete-target-dir \
--fields-terminated-by '\t' \
--split-by id \
--m 2

# 如果你要指定多个maptask来完成数据的导入，也就是--m参数的值不是1，则必须添加一个参数--split-by
# 该参数用来指定你原表的数据如何分配给多个线程来实现导入
# --split-by id 内部原理是获取id的最小值和id的最大值，进行平均划分
SELECT MIN(`id`), MAX(`id`) FROM `emp`


# 如果split-by后边的字段是字符串类型，则需要添加以下内容
-Dorg.apache.sqoop.splitter.allow_text_splitter=true \
```

## 数据导入到HDFS-增量
```text
sqoop import \
--connect jdbc:mysql://192.168.88.80:3306/userdb \
--username root \
--password 123456 \
--target-dir /sqoop/result5 \
--query 'select id,name,deg from emp WHERE  id>1203 and $CONDITIONS' \
--delete-target-dir \
--fields-terminated-by '\t' \
--m 1
```

## (重点)数据导入-全量-到Hive-全量
```text
-- 手动在hive中建一张表
create table test.emp_hive
(
id     int,
name   string,
deg    string,
salary int,
dept   string
)
row format delimited fields terminated by '\t'
stored as orc;


-- 导入数据
sqoop import \
--connect jdbc:mysql://hadoop01:3306/userdb \
--table emp \
--username root \
--password 123456 \
--fields-terminated-by '\t' \
--hive-database test \
--hive-table emp_hive \
-m 1
```

## (重点)数据导入-全量-到Hive-增量
```text
#!/bin/bash

yes_day=$(date -d "yesterday" +%Y-%m-%d)

/usr/bin/hive -f a.sql

wait

/usr/bin/sqoop import \
--connect jdbc:mysql://192.168.88.80:3306/userdb \
--username root \
--password 123456 \
--query "select * from customertest where last_mod between '${yes_day} 00:00:00' and '${yes_day} 23:59:59' and \$CONDITIONS" \
--fields-terminated-by '\t' \
--hcatalog-database test \
--hcatalog-table customertest \
-m 1
```
