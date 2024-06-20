mysql分区后每个分区成了独立的文件，虽然从逻辑上还是一张表其实已经分成了多张独立的表，
从“information_schema.INNODB_SYS_TABLES”系统表可以看到每个分区都存在独立的TABLE_ID，
由于Innodb数据和索引都是保存在".ibd"文件当中
（从INNODB_SYS_INDEXES系统表中也可以得到每个索引都是对应各自的分区(primary key和unique也不例外）），
所以分区表的索引也是随着各个分区单独存储。

在INNODB_SYS_INDEXES系统表中type代表索引的类型：
- 0: 一般的索引,
- 1: (GEN_CLUST_INDEX)不存在主键索引的表,会自动生成一个6个字节的标示值，
- 2: unique索引,
- 3: primary索引;

所以，当我们在分区表中创建索引时，其实也是在每个分区中创建索引，每个分区维护各自的索引（其实也就是local index）。

对于一般的索引(非主键或者唯一)，由于索引树中只保留了索引key和主键key(如果存在主键则是主键的key否则就是系统自动生成的6个的key)不受分区的影响。

但是，如果表中存在主键，主键索引需要保证全局的唯一性，就是所有分区中的主键的值都必须唯一（唯一键也是一样的道理），
所以，在创建分区时，如果表中存在主键或者唯一键，那么分区列必须包含主键或者唯一键的部分或者全部列
（全部列还好理解，部分列也可以个人猜测是为了各个分区和主键建立关系）。

由于需要保证全局性又要保证插入数据更新数据到具体的分区，所以就需要将分区和主键建立关系，
由于通过一般的索引进行查找其它非索引字段，需要通过主键，如果主键不能保证全局唯一性的话，那么就需要去每个分区查找了，这样性能可想而知。

```text
To enforce the uniqueness 
we only allow mapping of each unique/primary key value to one partition.
If we removed this limitation it would mean that for every insert/update 
we need to check in every partition to verify that it is unique. 
Also PK-only lookups would need to look into every partition.
```

## 索引方式：
性能依次降低

### 1. 主键分区

主键分区即字段是主键同时也是分区字段，性能最好

### 2. 部分主键+分区索引

使用组合主键里面的部分字段作为分区字段，同时将分区字段建索引（见下面详细说明）

### 3. 分区索引

没有主键，只有分区字段且分区字段建索引

### 4.分区+分区字段没有索引

只建了分区，但是分区字段没有建索引

### 总结

因为每一个表都需要有主键这样可以减少很多锁的问题，
由于上面讲过主键需要解决全局唯一性并且在插入和更新时可以不需要去扫描全部分区，造成主键和分区列必须存在关系；
所以最好的分区效果是使用主键作为分区字段其次是使用部分主键作为分区字段且创建分区字段的索引，其它分区方式都建议不采取。

## MYSQL的分区字段，必须包含在主键字段内
在对表进行分区时，如果分区字段没有包含在主键字段内，如表A的主键为ID,分区字段为createtime ，按时间范围分区，代码如下：
```text
CREATE TABLE T1 (
id          int(8) NOT NULL AUTO_INCREMENT,
create_time datetime NOT NULL,
PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
PARTITION BY RANGE(TO_DAYS (create_time))
(
PARTITION p0 VALUES LESS THAN (TO_DAYS('2010-04-15')),
PARTITION p1 VALUES LESS THAN (TO_DAYS('2010-05-01')),
PARTITION p2 VALUES LESS THAN (TO_DAYS('2010-05-15')),
PARTITION p3 VALUES LESS THAN (TO_DAYS('2010-05-31')),
PARTITION p4 VALUES LESS THAN (TO_DAYS('2010-06-15')),
PARTITION p19 VALUES LESS ThAN  MAXVALUE);
```

错误提示：#1503

MySQL主键的限制，每一个分区表中的公式中的列，必须在主键/unique key 中包括，在MYSQL的官方文档里是这么说明的
```text
18.5.1. Partitioning Keys, Primary Keys, and Unique Keys
This section discusses the relationship of partitioning keys with primary keys and unique keys. 
The rule governing this relationship can be expressed as follows: 
All columns used in the partitioning expression for a partitioned table must be part of every unique key 
that the table may have.

In other words,every unique key on the table must use every columnin the table's partitioning expression. 
(This also includes the table's primary key, since it is by definition a unique key. 
This particular case is discussed later in this section.) 
For example, each of the following table creation statements is invalid:
```

分区字段必须包含在主键字段内，至于为什么MYSQL会这样考虑，CSDN的斑竹是这么解释的：
为了确保主键的效率，否则同一主键区的东西一个在Ａ分区，一个在Ｂ分区，显然会比较麻烦。

下面讨论解决办法，毕竟在一张表里，日期做主键的还是不常见。

方法1： 顺应MYSQL的要求，就把分区字段加入到主键中，组成复合主键
```text
CREATE TABLE T1 (
id int(8) NOT NULL AUTO_INCREMENT,
create_time datetime NOT NULL,
PRIMARY KEY (id, create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
PARTITION BY RANGE(TO_DAYS (create_time))
(
PARTITION p0 VALUES LESS THAN (TO_DAYS('2010-04-15')),
PARTITION p1 VALUES LESS THAN (TO_DAYS('2010-05-01')),
PARTITION p2 VALUES LESS THAN (TO_DAYS('2010-05-15')),
PARTITION p3 VALUES LESS THAN (TO_DAYS('2010-05-31')),
PARTITION p4 VALUES LESS THAN (TO_DAYS('2010-06-15')),
PARTITION p19 VALUES LESS ThAN  MAXVALUE
);
```
测试通过，分区成功。

方法2：
既然MYSQL要把分区字段包含在主键内才能创建分区，那么在创建表的时候，先不指定主键字段，是否可以呢？？
测试如下：
```text
CREATE TABLE T1 (
id          int(8) NOT NULL ,
create_time datetime NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
PARTITION BY RANGE(TO_DAYS (create_time))
(
PARTITION p0 VALUES LESS THAN (TO_DAYS('2010-04-15')),
PARTITION p1 VALUES LESS THAN (TO_DAYS('2010-05-01')),
PARTITION p2 VALUES LESS THAN (TO_DAYS('2010-05-15')),
PARTITION p3 VALUES LESS THAN (TO_DAYS('2010-05-31')),
PARTITION p4 VALUES LESS THAN (TO_DAYS('2010-06-15')),
PARTITION p19 VALUES LESS ThAN  MAXVALUE
);
```
测试通过，分区成功。OK

继续添加上主键
```text
alter table t1 add PRIMARY KEY(ID)
```
错误1503，和前面一样的错误。

```text
alter table t1 add PRIMARY KEY(ID,createtime)
```
创建主键成功，但还是复合主键，看来是没办法了，必须听指挥了。

主键创建成功，把ID加上自增字段设置
```text
alter table t1 change id id int not null auto_increment;
alter table t1 auto_increment=1;
```

最后结论，MYSQL的分区字段，必须包含在主键字段内。

## 分区表中创建唯一索引：
例如，按create_time进行月分区的表里，唯一索引可能是orderNo，按照上面的要求，唯一索引就成为(order_no,create_time)了。但这样不满足业务需求。

解决办法：为分区表增加一个before insert触发器，在插入前查询下是否已存在即可。

```text
CREATE TRIGGER `trig_insert_t_order` BEFORE INSERT ON `t_order` FOR EACH ROW BEGIN
DECLARE v_count TINYINT UNSIGNED;
DECLARE v_mess_str varchar(100);

SELECT COUNT(1) INTO @v_count
FROM t_order
WHERE order_no = new.order_no
and new.create_time>=date_sub(SYSDATE(),INTERVAL 2 DAY);  ## 2天是否足够

IF (@v_count > 0) THEN
    SELECT concat('Duplicate entry ',new.order_no) INTO @v_mess_str;
    SIGNAL SQLSTATE '23000' SET MESSAGE_TEXT = @v_mess_str, MYSQL_ERRNO = 1022;
END IF;

END;
```

## 分区方式
分区有2种方式，水平切分和垂直切分。MySQL 数据库支持的分区类型为水平分区，它不支持垂直分区。

此外，MySQL数据库的分区索引是局部分区索引，一个分区中既存放了数据又存放了索引。

而全局分区是指，数据存放在各个分区中，但是所有数据的索引放在一个对象中。

目前，MySQL数据库还不支持全局分区索引。
