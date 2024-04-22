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
