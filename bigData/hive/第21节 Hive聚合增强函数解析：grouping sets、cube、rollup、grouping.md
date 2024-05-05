## 一、数据准备
在SQL查询语句中，group by用于对指定字段进行分组，以实现分组统计的结果，
而在一些场景中，需要对分组字段进行不同组合的分组统计，这就需要用到聚合增强函数。

以下将介绍四个聚合增强函数。

首先在hive中准备业务数据：
```text
-- 建库
create database if not exists test;
use test;
-- 建表
create table test.t_cookie(
month string,
day string,
cookieid string)
row format delimited fields terminated by ',';
-- 数据样例内容
insert into test.t_cookie values
('2015-03','2015-03-10','cookie1'),
('2015-03','2015-03-10','cookie5'),
('2015-03','2015-03-12','cookie7'),
('2015-04','2015-04-12','cookie3'),
('2015-04','2015-04-13','cookie2'),
('2015-04','2015-04-13','cookie4'),
('2015-04','2015-04-16','cookie4'),
('2015-03','2015-03-10','cookie2'),
('2015-03','2015-03-10','cookie3'),
('2015-04','2015-04-12','cookie5'),
('2015-04','2015-04-13','cookie6'),
('2015-04','2015-04-15','cookie3'),
('2015-04','2015-04-15','cookie2'),
('2015-04','2015-04-16','cookie1');
```

需求：
分别按照月（month）、天（day）、月和天（month,day）统计来访用户cookieid个数，并获取三者的结果集（一起插入到目标宽表中）。

## 二、grouping sets
首先，如果不使用grouping sets，也可以使用union all 来对以上需求进行统计，代码如下：
```text
select month, null as day, count(cookieid) cnt from test.t_cookie group by month
union all
select null as month, day,  count(cookieid) cnt from test.t_cookie group by day
union all
select month, day, count(cookieid) cnt from test.t_cookie group by month, day;
```
使用3个select 查询进行union all 统计，使用简单，也能达到同样的效果。
但是这样效率比较低下，因为底层用到了4个map任务。
另外需注意：使用union all合并结果集需要各个查询返回字段个数、类型一致，因此需要合理的使用null来填充返回结果。

union all方法执行结果，执行时间达到了2分32秒，时间比较长。

**为了优化查询时间，这里就需要用到grouping sets函数**。

**解释：**
```text
1、就是通过指定的多个维度进行查询的。即：写了哪些维度，它就按照哪些维度进行聚合计算。
2、细节：维度要用小括号括起来，如果是1个维度，小括号可以省略，但是建议写上。
```

使用grouping sets函数代码实现如下：
```text
--Hive SQL代码  
select
    month,day,count(cookieid)
from test.t_cookie
group by month,day
grouping sets (month,day,(month,day));
```
使用grouping sets函数执行同样的需要时间MR执行时间为1分40秒，效率提升近一分钟，
这是因为使用grouping sets在这个脚本中只会对表进行一次扫描，而union all会进行4次扫描。
另外性能越好的电脑所提升的效率越快。

**另外还有一种查询效率更快的工具，即：presto**，执行代码如下：
```text
-- Presto SQL
select
    month,day,count(cookieid)
from test.t_cookie
group by
grouping sets (month,day,(month,day));
```

使用presto查询的时间仅为11秒多钟，效率提升极大。

通过对比，除了查询效率不同，在hive中使用grouping sets函数跟在presto中使用grouping sets函数的区别就是：
Hive SQL的语法中，如果使用grouping sets()，那么group by后面需要加上字段，而presto则不需要。

## 三 cube函数和rollup函数
### 1、cube函数
cube的中文意思为立方体，cube函数的作用就是实现多个任意维度的查询，也可以理解为所有维度组合。
比如有N个维度，那么所有维度的组合的个数：2^N，也就是能根据你传入的字段, 把这些字段所有的组合方式全帮你做出来，进行查询。

根据上述需求，代码实现如下:
```text
-- 使用cube函数生成指定维度的所有组合
select month,day,count(cookieid)
from test.t_cookie
group by
cube (month, day);

-- 上述sql等价于
select month,day,count(cookieid)
from test.t_cookie
group by
grouping sets ((month,day), month, day, ());
```

### 2、rollup函数
rollup函数的功能：实现需统计的字段从右到左多级递减，显示统计某一层次结构的聚合。
按也就是照指定的字段，进行维度组合查询，它相当于是 cube的子集，cube是所有维度，rollup是部分维度。

例如：统计的维度是a,b, 则组合后的维度有: (a,b), (a), ()

根据上述需求，代码实现如下:
```text
-- rollup的功能：实现从右到左递减多级的统计
select month,day,count(cookieid)
from test.t_cookie
group by
rollup (month,day);  -- (month,day),month,()

-- 等价于
select month,day,count(cookieid)
from test.t_cookie
group by
grouping sets ((month,day), (month), ());
```

## 四、grouping判断
grouping的功能：
使用grouping操作来判断当前数据是按照哪个字段来分组。
如grouping(维度字段1, 维度字段2...)。
如果分组中包含相应的字段，则将位设置为0，否则将其设置为1。

grouping函数解释：它是用来判断当前数据是按照哪个字段来分组的。

注意: 要想使用用这个函数, 要求group by后边不能写内容, 所以需要结合 grouping sets函数到presto中执行。
原因是grouping要求group by后面不能有分组字段，grouping sets在hive上运行的时候要求加分组字段。

在Presto引擎中进行执行代码如下：
```text
select 
    month,
    day,
    count(cookieid),
    grouping(month)      as m,
    grouping(day)        as d,
    grouping(month, day) as m_d
from test.t_cookie
group by
grouping sets (month, day, (month, day));
```

结果分析:
```text
grouping(month)列为0时，可以看到month列都是有值的，为1时则相反，证明当前行是按照month来进行分组统计的

grouping(day)列为0时,也看到day列有值，为1时day则相反，证明当前行时按照day来进行分组统计的

grouping(month, day)是grouping(month)、grouping(day)二进制数值组合后转换得到的数字

a. 按照month分组，则month=0，day=1，组合后为01，二进制转换为十进制得到数字1；
b. 按照day分组，则month=1，day=0，组合后为10，二进制转换为十进制得到数字2；
c. 同时按照month和day分组，则month=0，day=0，组合后为00，二进制转换为十进制得到数字0。
```

因此可以使用grouping操作来判断当前数据是按照哪个字段来分组的。

grouping结果解释
```text
1）多维度, 结果 二进制表示法.
    二进制(逢二进一)和十进制(逢十进一)快速转换法: 8421码.
    二进制的写法: 1   1  1   1  1   1   1   1
    对应的十进制:128  64 32 16  8   4   2   1

2）grouping==单维度情况: 0说明有这个维度, 1说明没有这个维度.==

3）grouping多维度情况: 把这个多个维度的值(二进制)组合起来, 然后计算十进制, 即为 最终结果.
   例如: 按照 月和天 维度统计:
   如果最终结果是0, 二进制写法为: 0000, 说明是按照 月 和 天划分的.
   如果最终结果是1, 二进制写法为: 0001, 说明是按照 月 划分的.
   如果最终结果是2, 二进制写法为: 0010, 说明是按照 天 划分的.
   如果最终结果是3, 二进制写法为: 0011, 说明是按照 (空) 划分的.

4）例如: 我们按照日期、城市、商圈、店铺四个维度进行查询, 我们想看下是否是按照 (城市,店铺)维度划分的, 写法如下

   二进制的写法: 0   1   0   0
   对应的十进制: 8   4   2   1

5）举例示例如下:

grouping(日期、城市、商圈、店铺) = 1010(二进制) = 10(十进制)      证明有(城市, 店铺)维度
grouping(日期、城市、商圈、店铺) = 1001 = 9(十进制)        证明有( 城市, 商圈)维度
grouping(日期、城市、商圈、店铺) = 0100 = 4(十进制)       证明有(日期, 商圈, 店铺)维度
```
