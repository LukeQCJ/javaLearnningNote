对于分区表，可以建立不分区索引，也就是说表分区，但是索引不分区。

以下着重介绍分区表的分区索引。

索引与表一样，也可以分区。
索引分为两类：locally partition index（局部分区索引）、globally partition index（全局分区索引）。

# 1. 局部分区索引（locally partition index）

【局部分区索引】随表对索引完成相应的分区，即索引会使用与表相同的机制进行分区，每个表分区都有一个索引分区，并且只索引该表分区。

## 1.1 局部索引分类
▶ 局部前缀索引（local prefixed index）：以分区键作为索引定义的第一列。

▶ 局部非前缀索引（local nonprefixed index）：分区键没有作为索引定义的第一列。

注意：判断局部索引是前缀还是非前缀的只需要看分区键是否作为索引定义的第一列。

示例语句：
```text
--范围分区

--创建表
create table student_range_part(
stu_id   varchar2(4),
stu_name varchar2(100), -- 姓名
sex      varchar2(1),  -- 性别 1 男  2 女  0 未知
credit   integer default 0
)
partition by range (credit)
(
partition student_part1 values less than (60) tablespace kdhist_data,
partition student_part2 values less than (70) tablespace kdhist_data,
partition student_part3 values less than (80) tablespace kdhist_data,
partition student_part4 values less than (maxvalue) tablespace kdhist_data
);

--创建局部前缀索引；分区键（credit）作为索引定义的第一列
create index local_prefixed_index on student_range_part (credit, stu_id) local;

--创建局部非前缀索引；分区键未作为索引定义的第一列
create index local_nonprefixed_index on student_range_part (stu_id, credit) local;
```

## 1.2 局部索引示例
--①
```text
select * from student_range_part where credit = &credit and stu_id = &stu_id;
```

--②
```text
select * from student_range_part where stu_id = &stu_id;
```

对于以上两个查询来说，如果查询第一步是走索引的话，则：
- 局部前缀索引 local_prefixed_index 只对 ① 有用；
- 局部非前缀索引 local_nonprefixed_index 则对 ① 和 ② 均有用；

如果你有多个类似 ① 和 ② 的查询的话，则可以考虑建立局部非前缀索引；如果平常多使用查询 ① 的话，则可以考虑建立局部前缀索引；

小结：
```text
分区表一般使用局部索引。重点在于如何选择分区表和局部索引类型。
```

# 2. 全局分区索引（globally partition index）
索引按范围（Range）或散列（Hash，Oracle 10g中引入）进行分区，一个分区索引（全局）可能指向任何（或全部的）表分区。

对于全局分区索引来说，索引的实际分区数可能不同于表的分区数量；

全局索引的分区机制有别于底层表，例如表可以按 credit 列划分为10个分区，表上的一个全局索引可以按 stu_id 列划分为5个分区。

与局部索引不同，全局索引只有一类，即【全局前缀索引（prefixed global index）】，索引分区键必须作为索引定义的第一列，否则执行会报错。

```text
--范围分区

--创建表
create table student_range_part(
stu_id   varchar2(4),
stu_name varchar2(100), --姓名
sex      varchar2(1),  --性别 1 男  2 女  0 未知
credit   integer default 0
)
partition by range (credit)
(
partition student_part1 values less than (60) tablespace kdhist_data,
partition student_part2 values less than (70) tablespace kdhist_data,
partition student_part3 values less than (80) tablespace kdhist_data,
partition student_part4 values less than (maxvalue) tablespace kdhist_data
);

--创建按age进行范围分区的全局分区索引
create index global_index on student_range_part(credit) global
partition by range (credit)
(
partition index_part1 values less than (60),
partition index_part2 values less than (80),
partition index_partmax values less than (maxvalue)
);
```

注意：
- 全局索引要求最高分区（即最后一个分区）必须有一个值为 maxvalue 的最大上限值，这样可以确保底层表的所有行都能放在这个索引中；
- 一般情况下，大多数分区操作（如删除一个旧分区）都会使全局索引无效，除非重建全局索引，否则无法使用。

## 全局索引示例
全局索引一般用于【数据仓库】，许多数据仓库系统都存在大量的数据出入，如典型的数据“滑入滑出”（即删除表中最旧的分区，并为新加载的数据增加一个新分区）。

- ① 去除老数据：
    最旧的分区要么被删除，要么创建一个新表，将最旧的分区数据存入，从而对旧数据进行归档；
- ② 加载新数据并建立索引：
    将新数据加载到一个“工作”表中，建立索引并进行验证；
- ③ 关联新数据：
    一旦加载并处理了新数据，数据所在的表会与分区表中的一个空分区交换，将表中的这些新加载的数据变成分区表中的一个分区（分区表会变得更大）；

对于全局索引来说，这样增删分区的过程，意味着该全局索引的失效，需重建全局索引；

在 Oracle 9i 之后，可以在分区操作期间使用 UPDATE GLOBAL INEXES 子句来维护【全局索引】，
这意味着当在分区上执行删除、分解或其他操作时，Oracle会对原先建立的全局索引执行必要的修改，以保证它是最新的。

```text
--删除student_range_part表中的index_part1分区，同时同步维护全局索引
alter table student_range_part drop partition index_part1 update global indexes;
```

使用 UPDATE GLOBAL INEXES子句后，在删除一个分区时，必须删除可能指向该分区的所有全局索引条目；

执行表与分区的交换时，必须删除指向原数据的所有全局索引条目，再插入指向刚加载的数据的新条目；

如此一来 ALTER 命令执行的工作量会大幅增加；

**小结：**
- 分区操作执行完成后重建全局索引方式占用的数据库资源更少，因此完成的相对“更快”，但是会带来显著的“停机时间”（重建索引时会有一个可观的不可用窗口）；
- 在分区操作执行的同时执行 UPDATE GLOBAL INEXES 子句方式会占用更多的资源，且可能需要花费更长的时间才能完成操作，但好处是不会带来任何的停机时间。

即使是数据仓库，除非特殊需求，否则这个创建【局部索引】即可。