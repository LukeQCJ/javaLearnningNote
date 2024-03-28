# MySQL执行计划分析


> 本文来自公号 MySQL 技术，JavaGuide 对其做了补充完善。原文地址：<https://mp.weixin.qq.com/s/d5OowNLtXBGEAbT31sSH4g>

优化 SQL 的第一步应该是读懂 SQL 的执行计划。本篇文章，我们一起来学习下 MySQL `EXPLAIN` 执行计划相关知识。

## 什么是执行计划？

**执行计划** 是指一 SQL语句在经过 **MySQL查询优化器** 的优化会后，具体的执行方式。

执行计划通常用于SQL性能分析、优化等场景。通过`EXPLAIN`的结果，
可以了解到如数据表的查询顺序、数据查询操作的操作类型、哪些索引可以被命中、哪些索引实际会命中、每个数据表有多少行记录被查询等信息。

## 如何获取执行计划？

MySQL为我们提供了`EXPLAIN`命令，来获取执行计划的相关信息。

需要注意的是，`EXPLAIN`语句并不会真的去执行相关的语句，而是通过查询优化器对语句进行分析，找出最优的查询方案，并显示对应的信息。

`EXPLAIN`执行计划支持`SELECT`、`DELETE`、`INSERT`、`REPLACE`以及`UPDATE`语句。
我们一般多用于分析`SELECT`查询语句，使用起来非常简单，语法如下：

```sql
EXPLAIN + SELECT 查询语句；
```

我们简单来看下一条查询语句的执行计划：

```sql
mysql> explain SELECT * FROM dept_emp WHERE emp_no IN (SELECT emp_no FROM dept_emp GROUP BY emp_no HAVING COUNT(emp_no)>1);
+----+-------------+----------+------------+-------+-----------------+---------+---------+------+--------+----------+-------------+
| id | select_type | table    | partitions | type  | possible_keys   | key     | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+----------+------------+-------+-----------------+---------+---------+------+--------+----------+-------------+
|  1 | PRIMARY     | dept_emp | NULL       | ALL   | NULL            | NULL    | NULL    | NULL | 331143 |   100.00 | Using where |
|  2 | SUBQUERY    | dept_emp | NULL       | index | PRIMARY,dept_no | PRIMARY | 16      | NULL | 331143 |   100.00 | Using index |
+----+-------------+----------+------------+-------+-----------------+---------+---------+------+--------+----------+-------------+
```

可以看到，执行计划结果中共有 12 列，各列代表的含义总结如下表：

| **列名**        | **含义**                   |
|---------------|--------------------------|
| id            | SELECT 查询的序列标识符          |
| select_type   | SELECT 关键字对应的查询类型        |
| table         | 用到的表名                    |
| partitions    | 匹配的分区，对于未分区的表，值为 NULL    |
| type          | 表的访问方法                   |
| possible_keys | 可能用到的索引                  |
| key           | 实际用到的索引                  |
| key_len       | 所选索引的长度                  |
| ref           | 当使用【索引等值查询】时，与索引作比较的列或常量 |
| rows          | 预计要读取的行数                 |
| filtered      | 按表条件过滤后，留存的记录数的百分比       |
| Extra         | 附加信息                     |

## 如何分析 EXPLAIN 结果？

为了分析`EXPLAIN`语句的执行结果，我们需要搞懂执行计划中的重要字段。

### id

SELECT标识符，是查询中SELECT的序号，用来标识整个查询中SELECT语句的顺序。

id如果相同，从上往下依次执行。id不同，id值越大，执行优先级越高，如果行引用其他行的并集结果，则该值可以为NULL。

### select_type

查询的类型，主要用于区分普通查询、联合查询、子查询等复杂的查询，常见的值有：

- **SIMPLE**：简单查询，不包含UNION或者子查询。
- **PRIMARY**：查询中如果包含子查询或其他部分，外层的SELECT将被标记为PRIMARY。
- **SUBQUERY**：子查询中的第一个SELECT。
- **UNION**：在UNION语句中，UNION之后出现的SELECT。
- **DERIVED**：在FROM中出现的子查询将被标记为DERIVED。
- **UNION RESULT**：UNION查询的结果。

### table

查询用到的表名，每行都有对应的表名，表名除了正常的表之外，也可能是以下列出的值：

- **`<unionM,N>`** : 本行引用了 id 为 M 和 N 的行的 UNION 结果；
- **`<derivedN>`** : 本行引用了 id 为 N 的表所产生的的派生表结果。派生表有可能产生自 FROM 语句中的子查询。
- **`<subqueryN>`** : 本行引用了 id 为 N 的表所产生的的物化子查询结果。

### type（重要）

查询执行的类型，描述了查询是如何执行的。
所有值的顺序从最优到最差排序为：
system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL

常见的几种类型具体含义如下：

- **system**：如果表使用的引擎对于表行数统计是精确的（如：MyISAM），且表中只有一行记录的情况下，访问方法是system，是const的一种特例。
- **const**：表中最多只有一行匹配的记录，一次查询就可以找到，常用于使用主键或唯一索引的所有字段作为查询条件。
- **eq_ref**：当连表查询时，前一张表的行在当前这张表中只有一行与之对应。是除了system与const之外最好的join方式，常用于使用主键或唯一索引的所有字段作为连表条件。
- **ref**：使用普通索引作为查询条件，查询结果可能找到多个符合条件的行。
- **index_merge**：当查询条件使用了【多个索引】时，表示开启了Index Merge优化，此时执行计划中的key列列出了使用到的索引。
- **range**：对索引列进行【范围查询】，执行计划中的 key 列表示哪个索引被使用了。
- **index**：查询遍历了【整棵索引树】，与 ALL 类似，只不过扫描的是索引，而索引一般在内存中，速度更快。
- **ALL**：全表扫描。

### possible_keys

possible_keys列表示MySQL执行查询时可能用到的索引。
如果这一列为NULL，则表示没有可能用到的索引；
这种情况下，需要检查WHERE语句中所使用的的列，看是否可以通过给这些列中某个或多个添加索引的方法来提高查询性能。

### key（重要）

key列表示MySQL实际使用到的索引。如果为NULL，则表示未用到索引。

### key_len

key_len列表示MySQL实际使用的索引的最大长度；当使用到联合索引时，有可能是多个列的长度和。
在满足需求的前提下越短越好。如果key列显示NULL，则key_len 列也显示NULL 。

### rows

rows列表示根据表统计信息及选用情况，大致估算出找到所需的记录或所需读取的行数，数值越小越好。

### Extra（重要）

这列包含了MySQL解析查询的额外信息，通过这些信息，可以更准确的理解MySQL到底是如何执行查询的。常见的值如下：

- **Using filesort**：在排序时使用了外部的索引排序，没有用到表内索引进行排序。
- **Using temporary**：MySQL需要创建临时表来存储查询的结果，常见于ORDER BY和GROUP BY。
- **Using index**：表明查询使用了【覆盖索引，不用回表】，查询效率非常高。
- **Using index condition**：表示查询优化器选择使用了【索引条件下推】这个特性。
- **Using where**：表明查询使用了WHERE子句进行条件过滤。一般在没有使用到索引的时候会出现。
- **Using join buffer (Block Nested Loop)**：
    连表查询的方式，表示当被驱动表的没有使用索引的时候，MySQL会先将驱动表读出来放到join buffer中，再遍历被驱动表与驱动表进行查询。

这里提醒下，当 Extra列包含Using filesort或Using temporary时，MySQL的性能可能会存在问题，需要尽可能避免。

## 参考

- <https://dev.mysql.com/doc/refman/5.7/en/explain-output.html>
- <https://juejin.cn/post/6953444668973514789>

