Hive 表可以被分成多个分区。
准确的说应该是，表里面的数据可以按某一个列或者几个列进行分区，比如按天分区，不同日期的数据在不同的分区。
查数据时只需查对应分区的数据，而不需要全表搜索。

## 为什么分区很重要
随着互联网发展，数据已经越来越大，PB级别的数据量已经非常常见。因此从 HDFS 查询这么巨大的数据变得比较困难。
而 Hive 的出现降低了数据查询的负担。Apache Hive 把 SQL 代码转换的 MapReduce 作业，并提交到 Hadoop 集群执行。
当我们提交一个 SQL 查询的时候，Hive 会读取全部数据集。如果表的数据非常大，那么 MapReduce 作业的执行就比较低效。
因此，在表创建分区可以明显提升数据查询效率。

具有分区的表称为分区表，分区表的数据会被分成多个分区，每个分区对应一个分区列的值，每个分区在HDFS里面其实就是表目录下的一个子目录。
如果在查询的时候只需要特定分区的数据，那么 Hive 只会遍历该分区目录下的数据。这样能够可以避免全表扫描减低 IO，提升查询性能。

## 如何创建分区表
Hive 分区表创建语句如下：
```text
CREATE TABLE table_name (column1 data_type, column2 data_type)
PARTITIONED BY (partition1 data_type, partition2 data_type,….);
```

有个 PARTITIONED BY 关键字，括号里面是分区字段。

## Hive 分区示例
让我们以例子的方式来理解 Hive 里面的数据分区。
假如有一个表，表名为 Tab1，该表存储员工详细信息，列包括 id，name，dept 和 yoj（year of joining）。
假如我们需要查询所有 2012 年入职的员工，那么 hive 会在全表搜索满足条件的数据。
但如果我们对员工数据以yoj 进行分区并把数据对应的数据存储到单独文件里面，那么将会减少查询时间。下面的例子演示了如何对数据做分区。

存储员工数据的文件名是 file1：tab1/clientdata/file1
```text
id, name, dept, yoj
1, sunny, SC, 2009
2, animesh, HR, 2009
3, sumeer, SC, 2010
4, sarthak, TP, 2010
```

现在我们把上面的数据使用 yoj 字段分成两个文件。

tab1/clientdata/2009/file2
```text
1, sunny, SC, 2009
2, animesh, HR, 2009
```
tab1/clientdata/2010/file3
```text
3, sumeer, SC, 2010
4, sarthak, TP, 2010
```

现在如果我们从表查询数据，那么只有特定分区的数据会被搜索到。分区表创建语句如下：
```text
CREATE TABLE table_tab1 (id INT, name STRING, dept STRING, yoj INT) PARTITIONED BY (year STRING);
LOAD DATA LOCAL INPATH tab1’/clientdata/2009/file2’OVERWRITE INTO TABLE studentTab PARTITION (year='2009');
LOAD DATA LOCAL INPATH tab1’/clientdata/2010/file3’OVERWRITE INTO TABLE studentTab PARTITION (year='2010');
```

## Hive 分区类型
目前为止，我们已经讨论了 Hive 分区的概念以及如何创建分区。现在我们介绍 Hive 中的两种分区类型：
* 静态分区
* 动态分区

### Hive 静态分区
* 把输入数据文件单独插入分区表的叫静态分区。
* 通常在加载文件（大文件）到 Hive 表的时候，首先选择静态分区。
* 在加载数据时，静态分区比动态分区更节省时间。
* 你可以通过 alter table add partition 语句在表中添加一个分区，并将文件移动到表的分区中。
* 我们可以修改静态分区中的分区。
* 您可以从文件名、日期等获取分区列值，而无需读取整个大文件。
* 如果要在 Hive 使用静态分区，需要把 hive.mapred.mode 设置为 strict，set hive.mapred.mode=strict。
* 静态分区是在严格模式进行下。
* 你可以在 Hive 的内部表和外部表使用静态分区。

### Hive 动态分区
* 对分区表的一次性插入称为动态分区。
* 通常动态分区表从非分区表加载数据。
* 在加载数据的时候，动态分区比静态分区会消耗更多时间。
* 如果需要存储到表的数据量比较大，那么适合用动态分区。
* 假如你要对多个列做分区，但又不知道有多少个列，那么适合使用动态分区。
* 动态分区不需要 where 子句使用 limit。
* 不能对动态分区执行修改。
* 可以对内部表和外部表使用动态分区。
* 使用动态分区之前，需要把模式修改为非严格模式。set hive.mapred.mode=nostrict。

## Hive 分区的优点与缺点
让我们来讨论一下 Hive 分区的好处与局限性。

### Hive 分区的好处
* Hive 的分区可以水分分散执行压力。
* 数据查询性能比较好。
* 不需要在整个表列中搜索单个记录。

### Hive 分区的缺点
* 可能会创建太多的小分区，也就是说可能会创建很多目录。
* 分区对于低容量数据是有效的，但有些查询比如对大的数据量进行分组需要消耗很长时间。