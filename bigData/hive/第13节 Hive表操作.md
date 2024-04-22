# 一、操作表-修改表结构
代码如下（示例）：

```text
-- 1、创建表:
drop table if exists myhive.score;
create table myhive.score
(
sid string,
cid string,
sscore int
)
row format delimited fields terminated by '\t';

-- 2、加载数据
load data local inpath '/root/test/hive_data/score.txt' overwrite into table myhive.score;

select * from myhive.score;

-- 3、修改表结构
alter table score change column sscore score int;

select * from myhive.score;

-- 4、清空表数据（只能清空内部表）
truncate table score;
```

# 二、Hive表数据加载的方式
读入数据

代码如下（示例）：

```text
-- 如何给一张表加载数据
-- 1、创建表
drop table if exists myhive.score2;
create table if not exists myhive.score2
(
sid    string,
cid    string,
sscore int
)
row format delimited fields terminated by '\t';

-- 2、表加载数据
-- 方式1-insert into命令  #0颗星
insert into score2 values ('08','02',80),('09','02',80),('10','02',80);

-- 方式2-直接通过put命令将文件上传到表目录   #1颗星，测试用
hadoop fs -put score.txt /user/hive/warehouse/myhive.db/score
select * from score2;

-- 方式3-使用load命令加载数据   #4颗星，测试和生成都可以用
load data local inpath '/root/hive_data/test/score.txt' overwrite into table myhive.score2;

-- 方式4-使用insert into select ....  #5颗星、保存结果
insert into score2
select * from score where sscore > 80;

-- 方式5-使用create table score3 as select * from score;   #1颗星 测试用
-- 先创建表，表的字段和score字段相同，同时score表的数据插入到score3表
create table score3 as select * from score;

-- 方式6-使用第三方框架   #5颗星，生产环境
sqoop框架： MySQL/Oracle ===========>  Hive表
Kettle框架: MySQL/Oracle ===========>  Hive表

-- 方式7-HDFS先有数据，后有表 ，建表时使用location关键字
create external table t_covid2(
dt string comment '日期' ,
country string comment '县',
state string comment '州',
country_code string comment  '县编码',
cases int comment '确诊人数',
deaths int comment '死亡任务'
) comment '美国新冠数据'
row format delimited fields terminated by ',' -- 自定字段之间的分隔符
location '/input/data';
```

# 三、hive数据导出
代码如下（示例）：

```text
-- 方式1-使用命令导出到Linux本地目录中
-- 使用默认分隔符 '\001'
insert overwrite local directory '/root/hive_data/export_hive' select * from score where sscore > 85;

-- 手动指定分隔符 ','
insert overwrite local directory '/root/hive_data/export_hive'
row format delimited fields terminated by ','
select * from score where sscore > 85;

-- 方式2-使用命令导出到HDFS目录中（去掉local）
insert overwrite directory '/output/hive_data'
row format delimited fields terminated by ','
select * from score where sscore > 85;

-- 方式3-使用SQL导出到其他表!!!!!!!!!!!!!!!!!!
insert overwrite into table 目标表
select 字段 from 原表 where 条件;
```

# 四、Hive查询
## 4.1 基本查询
代码如下（示例）：

```text
-- 1、全表查询
    select * from t_covid;

-- 2、指定列查询
    select state, cases from t_covid; -- 默认列名

    -- 开启Hive的本地模式，加快查询速度
    set hive.stats.column.autogather=false;
    set hive.exec.mode.local.auto=true;

-- 3、聚合函数
    -- 1）求总行数（count）
    select count(*) from t_covid_common;
    select count(1) from t_covid_common;
    select * from t_covid_common;
    show tables ;
    
    --2）求分数的最大值（max）
    select max(cases) max_cases from t_covid_common;
    
    -- 3）求分数的最小值（min）
    select min(cases) max_cases from t_covid_common;
    
    -- 4）求分数的总和（sum）
    select sum(cases) total_cases from t_covid_common;
    
    -- 5）求分数的平均值（avg）
    select round(avg(cases)) avg_cases from t_covid_common;

-- 4、Limit 分区查询
    select * from t_covid_common limit 10;
    
    -- 从索引3开始先显示，显示8条 ，索引从0开始
    select * from t_covid_common limit 3,8;
```

## 4.2. Hive的条件查询
代码如下（示例）：

```text
select * from t_covid_common
where deaths between 1000 and 2000; -- [1000,2000] 包含左边和右边

-- 查询州名以S开头的疫情数据
select * from t_covid_common where state like 'S%';

select * from t_covid_common where state rlike '[S]';-- 做用同上

-- 查询确诊病例数大于50000,同时死亡病例数大于1000的信息
select * from t_covid_common where cases > 50000 and deaths > 1000;

-- 查询除了阿拉斯加州和新墨西哥州以外的疫情数据
select * from t_covid_common where state not in('Alaska', 'New Mexico');
```

## 4.3. Hive的分组查询
代码如下（示例）：

```text
-- 分组之后，select的后边，只能跟分组字段和聚合函数
-- 每个州的确诊病例总数

select state,sum(cases) from t_covid_common 
group by state;

select state, sum(cases) total_cases from t_covid_common
group by state
having total_cases >= 500000;
```

## 4.4. Hive的join查询
代码如下（示例）：

```text
-- 7、Hive的join操作

create table teacher (tid string, tname string) row format delimited fields terminated by '\t';
load data local inpath '/root/hive_data/test/teacher.txt' overwrite into table teacher;

create table course (cid string,c_name string,tid string) row format delimited fields terminated by '\t';
load data local inpath '/root/hive_data/test/course.txt' overwrite into table course;

-- 1) 内连接
-- 求两张表的交集
select * from teacher join course ;  -- 笛卡尔集
select * from course;

select * from teacher inner join course on teacher.tid = course.tid;
select * from teacher join course on teacher.tid = course.tid;
select * from teacher, course where teacher.tid = course.tid;  -- 同上


-- 2) 外连接
-- 2.1 左外连接
-- 以左表为主，左表的数据全部输出，右表有对应的数据就输出，没有对应的就输出NULL
insert into teacher values ('04','赵六');
select * from teacher;

select * from teacher left join course on teacher.tid = course.tid;

-- 2.2 右外连接

-- 以右表为主，右表的数据全部输出，左表有对应的数据就输出，没有对应的就输出NULL
insert into course values ('04','地理','05');
select * from course;

select * from teacher right join course on teacher.tid = course.tid;

-- 2.3 全外连接

select * from teacher full join course  on teacher.tid = course.tid;
```

## 4.5. Hive的排序查询
代码如下（示例）：

```text
-- 8、Hive的排序查询
-- 8.1 order By
-- 全局排序，要求只能有一个Reduce
select * from t_covid_common
order by cases desc
limit 10;

--  distribute by + sort by
--  distribute by 对数据进行分区，sort by对每一个分区的数据进行排序
--  按照州对数据进行分区，对每一个分区的州数据按照确诊病例进行降序排序
--  state.hash值 % reduce个数
set mapreduce.job.reduces = 55;

select * from t_covid_common 
distribute by state 
sort by cases;
```

# 总结
hive的查询和mysql的sql查询有很多的相同，join查询几乎一模一样，hql和sql只是会根据各自环境的要求进行一些自我的变化。

---

# 操作数据库
```text
-- 1、当你创建一个数据库，则hive自动会在/user/hive/warehouse目录创建一个数据库目录
这个目录是在hive-site.xml中一个属性指定的
create database if not exists myhive;
show databases ;

-- 2、手动指定数据库映射的文件夹
create database if not exists myhive2 location  '/myhive2';
show databases ;

-- 3、查看数据库的元数据信息
desc database myhive;

-- 4、删除数据库
-- 4.1 可以删除空数据库
drop database myhive2;

-- 4.2 如果要删除的数据库里边有表，则必须加 cascade关键字
use myhive;
create table A(id int);
drop database myhive cascade ;
```

# 操作表

## 介绍
1、Hive创建表的操作就是指定：表名、表的列名、表的列类型

2、Hive创建表内部的结构和传统的数据库的SQL除了语法相似，内部原理完全不同

3、Hive表文件字段之间默认的分隔符是'\001'2

## Hive的表数据类型
```text
整形：   int
浮点型： float / double / decimal(10,2)
字符串： string   mysql一般是varchar
日期类型：
年月日：date
时分秒：time
年月日-时分秒：date_time
注意：如果是日期或者时间，则使用字符串可以避免一些不必要的兼容问题
复杂类型：
array：数组，集合
map  ：键值对集合
struct： 类
```

## 表分类
1、Hive表分为两类，一个是内部表，一个是外部表

内部表（管理表）
```text
语法
create  table 表名(字段信息);

特点
1、内部表认为该表独占表数据文件，该文件不能共享
2、内部表对表文件有绝对的控制权
3、当删除内部表时，表文件会跟着一起删除（同时删除元数据和表数据）
4、所有的非共享表都可以设置为内部表
```

外部表
```text
语法
create  external table 表名(字段信息);

特点
1、外部表认为该表不能独占表数据文件，文件可以共享
2、外部表对表文件没有绝对的控制权
3、当删除外部表时，表文件不会跟着一起删除（只会删除元数据（映射信息），不会表数据）
4、所有的非共享表都可以设置为内部表
5、如果表数据需要被共享，则可以设置为外部表
```

内部表操作
```text
#1、创建内部表
create table t_covid(
    dt string comment '日期' ,
    county string comment '县',
    state  string comment '州',
    county_code string comment  '县编码',
    cases int comment '确诊人数',
    deaths int comment '死亡任务'
) comment '美国新冠数据'
row format delimited fields terminated by ','; -- 自定字段之间的分隔符

# 2、给内部表加载数据-从Linux本地-复制
# 将本地的文件复制到表目录：/user/hive/warehouse/myhive.db/t_covid
load data local inpath '/root/test/covid19.dat' into table t_covid;

# 2、给内部表加载数据-从HDFS加载-剪切
# 将HDFS文件剪切到表目录：/user/hive/warehouse/myhive.db/t_covid
load data inpath '/input/covid19.dat' into table t_covid;

# 3、查看数据
select * from t_covid;
```

外部表操作
```text
-- 1、创建外部表
drop table if exists t_covid;
    create external table t_covid(
    dt string comment '日期' ,
    county string comment '县',
    state  string comment '州',
    county_code string comment  '县编码',
    cases int comment '确诊人数',
    deaths int comment '死亡任务'
) comment '美国新冠数据'
row format delimited fields terminated by ','; -- 自定字段之间的分隔符

-- 2、给内部表加载数据-从Linux本地-复制
# 将本地的文件复制到表目录：/user/hive/warehouse/myhive.db/t_covid
load data local inpath '/root/test/covid19.dat' into table t_covid;

-- 2、给内部表加载数据-从HDFS加载-剪切
# 将HDFS文件剪切到表目录：/user/hive/warehouse/myhive.db/t_covid
load data inpath '/input/covid19.dat' into table t_covid;

-- 3、查看数据
select * from t_covid;



-- ------演示-让多张表共享同一份数据文件-------
-- 1、创建外部表1映射到文件covid19.dat
drop table if exists t_covid1;
create external table t_covid1(
    dt string comment '日期' ,
    country string comment '县',
    state  string comment '州',
    country_code string comment  '县编码',
    cases int comment '确诊人数',
    deaths int comment '死亡任务'
) comment '美国新冠数据'
row format delimited fields terminated by ',' -- 自定字段之间的分隔符
location '/input/data';

select * from t_covid1;

-- 2、创建外部表2映射到文件covid19.dat
drop table if exists t_covid2;
create external table t_covid2(
    dt string comment '日期' ,
    country string comment '县',
    state  string comment '州',
    country_code string comment  '县编码',
    cases int comment '确诊人数',
    deaths int comment '死亡任务'
) comment '美国新冠数据'
row format delimited fields terminated by ',' -- 自定字段之间的分隔符
location '/input/data';

select * from t_covid2;

-- 3、创建外部表3映射到文件covid19.dat
drop table if exists t_covid3;
create external table t_covid3(
    dt string comment '日期' ,
    country string comment '县',
    state  string comment '州',
    country_code string comment  '县编码',
    cases int comment '确诊人数',
    deaths int comment '死亡任务'
) comment '美国新冠数据'
row format delimited fields terminated by ',' -- 自定字段之间的分隔符
location '/input/data';

select * from t_covid3;

-- 4、删除测试

drop table t_covid1;
select * from t_covid1;
select * from t_covid2;

drop table t_covid3;
select * from t_covid3;
```

其他操作
```text
#如何判断一张表是内部表还是外部表，通过元数据查看
desc formatted t_covid;

#查看以下信息
Table Type:         ,EXTERNAL_TABLE     #外部表
Table Type:         ,MANAGED_TABLE      #内部表

表信息都是通过mysql查看的
```

复杂类型操作
```text
-------------Hive的复杂类型-Array类型------------
-- 1、数据样例
/*
zhangsan      beijing,shanghai,tianjin,hangzhou
wangwu       changchun,chengdu,wuhan,beijing
*/
-- 2、建表
use myhive;
create external table hive_array(
    name string,
    work_locations array<string>
)
row format delimited fields terminated by '\t'  -- 字段之间的分隔符
collection items terminated by  ',';            -- 数组元素之间的分割符

-- 3、加载数据
load data local inpath '/root/hive_data/array.txt' overwrite into table hive_array; --overwrite覆盖数据

-- 4、查询数据
-- 查询所有数据
select * from hive_array;

-- 查询work_locations数组中第一个元素
select name, work_locations[0] location from hive_array;

-- 查询location数组中元素的个数
select name, size(work_locations) location_size from hive_array;

-- 查询location数组中包含tianjin的信息
select * from hive_array where array_contains(work_locations,'tianjin');

-------------Hive的复杂类型-Map类型------------
-- 1、数据样例
/*
1,zhangsan,father:xiaoming#mother:xiaohuang#brother:xiaoxu,28
2,lisi,father:mayun#mother:huangyi#brother:guanyu,22
3,wangwu,father:wangjianlin#mother:ruhua#sister:jingtian,29
4,mayun,father:mayongzhen# mother:angelababy,26
*/
-- 2、建表
create table hive_map(
    id int,
    name string,
    members map<string,string>,
    age int
)
row format delimited fields terminated by ','
collection items terminated by  '#'
map keys terminated by  ':';

-- 3、加载数据
load data local inpath '/root/hive_data/map.txt' overwrite into table hive_map;

-- 4、查询数据
-- 查询全部数据
select * from hive_map;
-- 根据键找对应的值
select id, name, members['father'] father, members['mother'] mother, age from hive_map;

-- 获取所有的键
select id, name, map_keys(members) as relation from hive_map;

-- 获取所有的值
select id, name, map_values(members) as relation from hive_map;
-- 获取键值对个数
select id,name,size(members) num from hive_map;

-- 获取有指定key的数据
select * from hive_map where array_contains(map_keys(members), 'brother');

-- 查找包含brother这个键的数据，并获取brother键对应的值
select id,name, members['brother'] brother from hive_map
where array_contains(map_keys(members), 'brother');

-- -----------Hive的复杂类型-Struct类型------------
-- 1、数据样例
/*
192.168.1.1#zhangsan:40:男
192.168.1.2#lisi:50:女
192.168.1.3#wangwu:60:女
192.168.1.4#zhaoliu:70:男
*/
-- 2、建表
create table hive_struct(
ip string,
info struct<name:string,age:int,gender:string>
)
row format delimited fields terminated by '#'
collection items terminated by  ':';


-- 3、加载数据
load data local inpath '/root/hive_data/struct.txt' overwrite into table hive_struct;

-- 4、查询数据
-- 查询全部数据
select * from hive_struct;

-- 根据字段查询
select ip,info.name,info.gender from hive_struct;
```

---
# HIVE 分区表和分桶表

## 介绍
表的分类:
- 内部表：
  - 内部常规表
  - 内部分区表
  - 内部分桶表
- 外部表:
  - 外部常规表
  - 外部分区表
  - 外部分桶表

## 分区表
分区就是分文件夹
- 1、分区表实际是就是对要进行分析的文件进行分类管理
- 2、本质是将相同特征的文件存放在同一个文件夹下，通过文件夹对数据进行分类
- 3、分区之后在查询时，可以通过添加条件，避免进行全表扫描，提高查询效率
- 4、分区表又分为静态分区和动态分区
- 5、分区表是一种优化手段，是锦上添花的东西，一张表可以没有分区，但是查询效率可能会比较低

### 静态分区
静态分区就是手动来操作分区的文件夹

```text
#----------------------单级分区-一级文件夹------------------------------

-- 1、数据样例
/*
2021-01-28,Autauga,Alabama,01001,5554,69
2021-01-28,Baldwin,Alabama,01003,17779,225
2021-01-28,Barbour,Alabama,01005,1920,40
2021-01-28,Bibb,Alabama,01007,2271,51
2021-01-28,Blount,Alabama,01009,5612,98
2021-01-29,Bullock,Alabama,01011,1079,29
2021-01-29,Butler,Alabama,01013,1788,60
2021-01-29,Calhoun,Alabama,01015,11833,231
2021-01-29,Chambers,Alabama,01017,3159,76
2021-01-29,Cherokee,Alabama,01019,1682,35
2021-01-30,Chilton,Alabama,01021,3523,79
2021-01-30,Choctaw,Alabama,01023,525,24
2021-01-30,Clarke,Alabama,01025,3150,38
2021-01-30,Clay,Alabama,01027,1319,50
*/

-- 2、创建分区表
drop table t_covid;
create external table t_covid(
    dt_value string  ,
    country string ,
    state  string ,
    country_code string ,
    cases int ,
    deaths int
)
partitioned by (dt string) -- 这里用来指定分区的字段，字段名字可以随便写
row format delimited fields terminated by ',' -- 自定字段之间的分隔符
;

-- 3、给分区表加载数据
load data local inpath '/root/hive_data/covid-28.dat' into table t_covid partition (dt='2021-01-28');
load data local inpath '/root/hive_data/covid-29.dat' into table t_covid partition (dt='2021-01-29');
load data local inpath '/root/hive_data/covid-30.dat' into table t_covid partition (dt='2021-01-30');


-- 4、查看分区表数据
select * from t_covid;  --查看所有分区数据

select * from t_covid where dt='2021-01-28'; -- 查询指定单个分区的数据
select * from t_covid where dt='2021-01-28' or dt='2021-01-29' ; -- 查询指定多个分区的数据


#----------------------单级分区-多级文件夹------------------------------
-- 1、数据样例
/*
2021-02-28,Cleburne,Alabama,01029,1258,28
2021-02-28,Coffee,Alabama,01031,4795,72
2021-02-28,Colbert,Alabama,01033,5686,104
2021-02-28,Conecuh,Alabama,01035,999,23
2021-02-28,Coosa,Alabama,01037,670,19
2021-02-28,Covington,Alabama,01039,3504,87
2021-02-28,Crenshaw,Alabama,01041,1279,47
2021-02-28,Cullman,Alabama,01043,8466,145、
2021-02-29,Dale,Alabama,01045,4235,92
2021-02-29,Dallas,Alabama,01047,3181,108
2021-02-29,DeKalb,Alabama,01049,8052,130
2021-02-29,Elmore,Alabama,01051,8449,131
2021-02-30,Escambia,Alabama,01053,3478,47
2021-02-30,Etowah,Alabama,01055,12359,228
2021-02-30,Fayette,Alabama,01057,1841,37
2021-02-30,Franklin,Alabama,01059,3829,55
2021-02-30,Geneva,Alabama,01061,2205,51
*/

-- 2、创建分区表
drop table t_covid2;
create  table t_covid2(
    dt_value string  ,
    country string ,
    state  string ,
    country_code string ,
    cases int ,
    deaths int
)
partitioned by (month string,dt string) -- 这里用来指定分区的字段，字段名字可以随便写
row format delimited fields terminated by ',' -- 自定字段之间的分隔符
;

-- 3、给分区表加载数据
-- 1月份数据

load data local inpath '/root/hive_data/covid-28.dat' into table t_covid2
partition (month='2021-01',dt='2021-01-28');

load data local inpath '/root/hive_data/covid-29.dat' into table t_covid2
partition (month='2021-01',dt='2021-01-29');

load data local inpath '/root/hive_data/covid-30.dat' into table t_covid2
partition (month='2021-01',dt='2021-01-30');

-- 2月份数据
load data local inpath '/root/hive_data/2_month/covid-28.dat' into table t_covid2
partition (month='2021-02', dt='2021-02-28');

load data local inpath '/root/hive_data/2_month/covid-29.dat' into table t_covid2
partition (month='2021-02', dt='2021-02-29');

load data local inpath '/root/hive_data/2_month/covid-30.dat' into table t_covid2
partition (month='2021-02', dt='2021-02-30');



-- 4、查询数据
select * from t_covid2; -- 查询所有分区

select * from t_covid2 where month = '2021-02'; --  查询2月份数据

select * from t_covid2 where month = '2021-02' and  dt = '2021-02-28'; --  查询2月28号份数据

-- 手动添加分区
alter table t_covid2 add partition(month='2021-03',dt='2021-03-28');


-- 查看分区文件夹信息
show partitions t_covid2;
```

### 动态分区

![动态分区数据加载流程](img/13/dynamicPartitionLoadFlow01.png)

- 开启动态分区
- 模拟数据
- 创建中间普通表
- 给普通表加载数据
- 创建最终的分区表
- 查询普通表，将数据插入到分区表

示例：按照月进行分区
```text
# 1、开启动态分区
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;

# 2、模拟数据
1       2022-01-01      zhangsan        80
2       2022-01-01      lisi    70
3       2022-01-01      wangwu  90
1       2022-01-02      zhangsan        90
2       2022-01-02      lisi    65
3       2022-01-02      wangwu  96
1       2022-01-03      zhangsan        91
2       2022-01-03      lisi    66
3       2022-01-03      wangwu  96
1       2022-02-01      zhangsan        80
2       2022-02-01      lisi    70
3       2022-02-01      wangwu  90
1       2022-02-02      zhangsan        90
2       2022-02-02      lisi    65
3       2022-02-02      wangwu  96
1       2022-02-03      zhangsan        91
2       2022-02-03      lisi    66
3       2022-02-03      wangwu  96

# 3、创建中间普通表
create table test1
(
id    int,
date_val string,
name  string,
score int
)
row format delimited fields terminated by ',';

# 4、给普通表加载数据
load data local inpath '/export/data/hivedatas/partition2.txt' overwrite into table test1;

# 5、创建最终的分区表
create table test2
(
id    int,
name  string,
score int
)
partitioned by (dt string) -- 这个分区字段的名字随便写，它来决定HDFS上文件夹的名字：day=2022-01-01
row format delimited fields terminated by ',';

-- 6、查询普通表，将数据插入到分区表: partition (dt) 可以省略
insert overwrite table test2 partition (dt)
select id, name, score, date_val from test1;
```


## 分桶表

```text
1、分桶表和分区表没什么关系
2、分桶表是将表数据分到多个文件，分区表是将数据分到多个文件夹
3、分桶表底层就是MapReduce中分区
4、分桶和分区的区别
    1) MapReduce的分区就是Hive的分桶
    2) Hive的分桶就是MapReduce的分区
    3) Hive的分区和MapReduce分区没什么关系
5、结论：分桶就是分文件
6、分桶的本质就是将大表进行拆分变成小表，小表好join
7、一张表既可以是分区表也可以是分桶表
```

作用
```text
1、提高Join的效率

2、用于数据的抽样
    select * from student tablesample(bucket x out of y on id);
    n：总桶数
    x：从第几个桶开始抽取
    y：必须是总桶数的因数或倍数（自定义）
    z：共需抽取出的桶数（z=n/y）
    例子：
    select * from student tablesample(bucket 1 out of 2 on id);
    z ：数据属于第几个桶
    1 ：第1个分桶的数据（1）
    2 ：第3个分桶的数据（1+y）
    3 ：第5个分桶的数据（3+y）
    4 ：第7个分桶的数据（5+y）
    5 ：第9个分桶的数据（7+y）
```

```text
-- 1、创建分桶表
create table course(
    cid string  ,
    c_name string ,
    tid string
)
clustered by (cid) into 3 buckets; -- 分3个文件
row format delimited fields terminated by '\t';

-- 2、创建临时表
create table course_tmp(
    cid string  ,
    c_name string ,
    tid string
)
row format delimited fields terminated by '\t';

# 3、给临时表加载数据
load data local inpath '/root/hive_data/test/course.txt' overwrite into table course_tmp;

# 4、将临时表数据插入到分桶表
insert overwrite table course
select * from course_tmp cluster by (cid);
```

## 分区+分桶
```text
-- 内部表
create table A(
    dt_value string  ,
    country string ,
    state  string ,
    country_code string ,
    cases int ,
    deaths int
)
partitioned by (dt string)   -- 分文件夹
clustered by (country_code) into 3 buckets ;  -- 文件夹内部再分文件

-- 外部表
create external table A(
    dt_value string  ,
    country string ,
    state  string ,
    country_code string ,
    cases int ,
    deaths int
)
partitioned by (dt string)   -- 分文件夹
clustered by (country_code) into 3 buckets ;  -- 文件夹内部再分文件
```

## Hive表数据加载的方式
```text
-- 如何给一张表加载数据
-- 1、创建表
drop table if exists myhive.score2;
create table if not exists myhive.score2
(
sid    string,
cid    string,
sscore int
)
row format delimited fields terminated by '\t';

-- 2、表加载数据
-- 方式1-insert into命令  # 0颗星
insert into score2 values ('08','02',80),('09','02',80),('10','02',80);

-- 方式2-直接通过put命令将文件上传到表目录   # 1颗星，测试用
hadoop fs -put score.txt /user/hive/warehouse/myhive.db/score
select * from score2;

-- 方式3-使用load命令加载数据   # 4颗星，测试和生产都可以用
load data local inpath '/root/hive_data/test/score.txt' overwrite into table myhive.score2;

-- 方式4-使用insert into select ....  # 5颗星、保存结果
insert into score2
select * from score where sscore > 80;

-- 方式5-使用create table score5 as select * from score;   # 1颗星 测试用
-- 先创建表，表的字段和score字段相同，同时score表的数据插入到score3表
create table score3 as select * from score;

-- 方式6-使用第三方框架   # 5颗星，生产环境
sqoop框架： MySQL/Oracle ===========>  Hive表
Kettle框架: MySQL/Oracle ===========>  Hive表

-- 方式7-HDFS先有数据，后有表 ，建表时使用location关键字
create external table t_covid2(
    dt string comment '日期' ,
    country string comment '县',
    state  string comment '州',
    country_code string comment  '县编码',
    cases int comment '确诊人数',
    deaths int comment '死亡任务'
) comment '美国新冠数据'
row format delimited fields terminated by ',' -- 自定字段之间的分隔符
location '/input/data';
```

## Hive查询的数据如何导出
```text
-- 方式1-使用命令导出到Linux本地目录中
-- 使用默认分隔符 '\001'
insert overwrite local directory '/root/hive_data/export_hive'  
select * from score where sscore > 85;

-- 手动指定分隔符 ','
insert overwrite local directory '/root/hive_data/export_hive'
row format delimited fields terminated by ','
select * from score where sscore > 85;

-- 方式2-使用命令导出到HDFS目录中（去掉local）
insert overwrite directory '/output/hive_data'
row format delimited fields terminated by ','
select * from score where sscore > 85;

-- 方式3-使用SQL导出到其他表                          !!!!!!!!!!!!!!!!!!
insert overwrite into table 目标表
select  字段 from 原表 where 条件;

-- 方式4-使用第三方框架导出其他的存储平台(HBase、Spark、MySQL) !!!!!!!!!!!!
sqoop
Kettle
Datax
Presto
```


