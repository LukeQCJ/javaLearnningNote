我们都知道标准 SQL 里面有许多内置函数可以使用，比如字符串函数，日期函数，聚合函数等。
Hive 也有类似的内置函数，下面详细介绍 Hive 里面的内置函数。

## 关系运算
### 1、等值比较: =
```text
语法：A=B
操作类型：所有基本类型
描述: 如果表达式A与表达式B相等，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where 1=1;
1
```

### 2、不等值比较: <>
```text
语法: A <> B
操作类型: 所有基本类型
描述: 如果表达式A为NULL，或者表达式B为NULL，返回NULL；如果表达式A与表达式B不相等，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where 1 <> 2;
1
```

### 3、小于比较: <
```text
语法: A < B
操作类型：所有基本类型
描述: 如果表达式A为NULL，或者表达式B为NULL，返回NULL；如果表达式A小于表达式B，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where 1 < 2;
1
```

### 4、小于等于比较: <=
```text
语法: A <= B
操作类型: 所有基本类型
描述: 如果表达式A为NULL，或者表达式B为NULL，返回NULL；如果表达式A小于或者等于表达式B，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where 1 < = 1;
1
```

### 5、大于比较: >
```text
语法: A > B
操作类型: 所有基本类型
描述: 如果表达式A为NULL，或者表达式B为NULL，返回NULL；如果表达式A大于表达式B，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where 2 > 1;
1
```

### 6、大于等于比较: >=
```text
语法: A >= B
操作类型: 所有基本类型
描述: 如果表达式A为NULL，或者表达式B为NULL，返回NULL；如果表达式A大于或者等于表达式B，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where 1 >= 1;
1
```
注意：String 的比较要注意(常用的时间比较可以先 to_date 之后再比较)
```text
hive> select * from iteblog;
2011111209 00:00:00     2011111209

hive> select a, b, a<b, a>b, a=b from iteblog;
2011111209 00:00:00     2011111209      false   true    false
```

### 7、空值判断: IS NULL
```text
语法: A IS NULL
操作类型: 所有类型
描述: 如果表达式A的值为NULL，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where null is null;
1
```

### 8、非空判断: IS NOT NULL
```text
语法: A IS NOT NULL
操作类型: 所有类型
描述: 如果表达式A的值为NULL，则为FALSE；否则为TRUE
```
```text
hive> select 1 from iteblog where 1 is not null;
1
```

### 9、LIKE比较: LIKE
```text
语法: A LIKE B
操作类型: strings
描述: 如果字符串A或者字符串B为NULL，则返回NULL；如果字符串A符合表达式B 的正则语法，则为TRUE；否则为FALSE。
B中字符”_”表示任意单个字符，而字符”%”表示任意数量的字符。
```
```text
hive> select 1 from iteblog where 'football' like 'foot%';
1
hive> select 1 from iteblog where 'football' like 'foot____';
1
//注意：否定比较时候用NOT A LIKE B
hive> select 1 from iteblog where NOT 'football' like 'fff%';
1
```

### 10、JAVA的LIKE操作: RLIKE
```text
语法: A RLIKE B
操作类型: strings
描述: 如果字符串A或者字符串B为NULL，则返回NULL；如果字符串A符合JAVA正则表达式B的正则语法，则为TRUE；否则为FALSE。
```
```text
hive> select 1 from iteblog where 'footbar’ rlike '^f.*r$’;
1
// 注意：判断一个字符串是否全为数字：

hive> select 1 from iteblog where '123456' rlike '^\\d+$';
1
hive> select 1 from iteblog where '123456aa' rlike '^\\d+$';
```

### 11、REGEXP操作: REGEXP
```text
语法: A REGEXP B
操作类型: strings
描述: 功能与RLIKE相同
```
```text
hive> select 1 from iteblog where 'footbar' REGEXP '^f.*r$';
1
```

## 数学运算
### 1、加法操作: +
```text
语法: A + B
操作类型：所有数值类型
说明：返回A与B相加的结果。结果的数值类型等于A的类型和B的类型的最小父类型（详见数据类型的继承关系）。
比如，int + int 一般结果为int类型，而 int + double 一般结果为double类型
```
```text
hive> select 1 + 9 from iteblog;
10

hive> create table iteblog as select 1 + 1.2 from iteblog;
hive> describe iteblog;
_c0     double
```

### 2、减法操作: -
```text
语法: A – B
操作类型：所有数值类型
说明：返回A与B相减的结果。结果的数值类型等于A的类型和B的类型的最小父类型（详见数据类型的继承关系）。
比如，int – int 一般结果为int类型，而 int – double 一般结果为double类型
```
```text
hive> select 10 – 5 from iteblog;
5
hive> create table iteblog as select 5.6 – 4 from iteblog;
hive> describe iteblog;
_c0     double
```

### 3、乘法操作: *
```text
语法: A * B
操作类型：所有数值类型
说明：返回A与B相乘的结果。结果的数值类型等于A的类型和B的类型的最小父类型（详见数据类型的继承关系）。
注意，如果A乘以B的结果超过默认结果类型的数值范围，则需要通过cast将结果转换成范围更大的数值类型
```
```text
hive> select 40 * 5 from iteblog;
200
```

### 4、除法操作: /
```text
语法: A / B
操作类型：所有数值类型
说明：返回A除以B的结果。结果的数值类型为double
```
```text
hive> select 40 / 5 from iteblog;
8.0
```
注意：hive中最高精度的数据类型是double,只精确到小数点后16位，在做除法运算的时候要特别注意
```text
hive>select ceil(28.0/6.999999999999999999999) from iteblog limit 1;    
4
hive>select ceil(28.0/6.99999999999999) from iteblog limit 1;           
5
```

### 5、取余操作: %
```text
语法: A % B
操作类型：所有数值类型
说明：返回A除以B的余数。结果的数值类型等于A的类型和B的类型的最小父类型（详见数据类型的继承关系）。
```
```text
hive> select 41 % 5 from iteblog;
1
hive> select 8.4 % 4 from iteblog;
0.40000000000000036
```
注意：精度在hive中是个很大的问题，类似这样的操作最好通过round指定精度
```text
hive> select round(8.4 % 4 , 2) from iteblog;
0.4
```

### 6、位与操作: &
```text
语法: A & B
操作类型：所有数值类型
说明：返回A和B按位进行与操作的结果。结果的数值类型等于A的类型和B的类型的最小父类型（详见数据类型的继承关系）。
```
```text
hive> select 4 & 8 from iteblog;
0
hive> select 6 & 4 from iteblog;
4
```

### 7、位或操作: |
```text
语法: A | B
操作类型：所有数值类型
说明：返回A和B按位进行或操作的结果。结果的数值类型等于A的类型和B的类型的最小父类型（详见数据类型的继承关系）。
```
```text
hive> select 4 | 8 from iteblog;
12
hive> select 6 | 8 from iteblog;
14
```

### 8、位异或操作: ^
```text
语法: A ^ B
操作类型：所有数值类型
说明：返回A和B按位进行异或操作的结果。结果的数值类型等于A的类型和B的类型的最小父类型（详见数据类型的继承关系）。
```
```text
hive> select 4 ^ 8 from iteblog;
12
hive> select 6 ^ 4 from iteblog;
2
```

### 9．位取反操作: ~
```text
语法: ~A
操作类型：所有数值类型
说明：返回A按位取反操作的结果。结果的数值类型等于A的类型。
```
```text
hive> select ~6 from iteblog;
-7
hive> select ~4 from iteblog;
-5
```

## 逻辑运算
### 1、逻辑与操作: AND
```text
语法: A AND B
操作类型：boolean
说明：如果A和B均为TRUE，则为TRUE；否则为FALSE。如果A为NULL或B为NULL，则为NULL
```
```text
hive> select 1 from iteblog where 1=1 and 2=2;
1
```

### 2、逻辑或操作: OR
```text
语法: A OR B
操作类型：boolean
说明：如果A为TRUE，或者B为TRUE，或者A和B均为TRUE，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where 1=2 or 2=2;
1
```

### 3、逻辑非操作: NOT
```text
语法: NOT A
操作类型：boolean
说明：如果A为FALSE，或者A为NULL，则为TRUE；否则为FALSE
```
```text
hive> select 1 from iteblog where not 1=2;
1
```

## 数值计算
### 1、取整函数: round
```text
语法: round(double a)
返回值: BIGINT
说明: 返回double类型的整数值部分 （遵循四舍五入）
```
```text
hive> select round(3.1415926) from iteblog;
3
hive> select round(3.5) from iteblog;
4
hive> create table iteblog as select round(9542.158) from iteblog;
hive> describe iteblog;
_c0     bigint
```

### 2、指定精度取整函数: round
```text
语法: round(double a, int d)
返回值: DOUBLE
说明: 返回指定精度d的double类型
```
```text
hive> select round(3.1415926,4) from iteblog;
3.1416
```

### 3、向下取整函数: floor
```text
语法: floor(double a)
返回值: BIGINT
说明: 返回等于或者小于该double变量的最大的整数
```
```text
hive> select floor(3.1415926) from iteblog;
3
hive> select floor(25) from iteblog;
25
```

### 4、向上取整函数: ceil
```text
语法: ceil(double a)
返回值: BIGINT
说明: 返回等于或者大于该double变量的最小的整数
```
```text
hive> select ceil(3.1415926) from iteblog;
4
hive> select ceil(46) from iteblog;
46
```

### 5、向上取整函数: ceiling
```text
语法: ceiling(double a)
返回值: BIGINT
说明: 与ceil功能相同
```
```text
hive> select ceiling(3.1415926) from iteblog;
4
hive> select ceiling(46) from iteblog;
46
```

### 6、取随机数函数: rand
```text
语法: rand(),rand(int seed)
返回值: double
说明: 返回一个0到1范围内的随机数。如果指定种子seed，则会等到一个稳定的随机数序列
```
```text
hive> select rand() from iteblog;
0.5577432776034763
hive> select rand() from iteblog;
0.6638336467363424

hive> select rand(100) from iteblog;
0.7220096548596434
hive> select rand(100) from iteblog;
0.7220096548596434
```

### 7、自然指数函数: exp
```text
语法: exp(double a)
返回值: double
说明: 返回自然对数e的a次方
```
```text
hive> select exp(2) from iteblog;
7.38905609893065
```

自然对数函数: ln
```text
语法: ln(double a)
返回值: double
说明: 返回a的自然对数
```
```text
hive> select ln(7.38905609893065) from iteblog;
2.0
```

### 8、以10为底对数函数: log10
```text
语法: log10(double a)
返回值: double
说明: 返回以10为底的a的对数
```
```text
hive> select log10(100) from iteblog;
2.0
```

### 9、以2为底对数函数: log2
```text
语法: log2(double a)
返回值: double
说明: 返回以2为底的a的对数
```
```text
hive> select log2(8) from iteblog;
3.0
```

### 10、对数函数: log
```text
语法: log(double base, double a)
返回值: double
说明: 返回以base为底的a的对数
```
```text
hive> select log(4,256) from iteblog;
4.0
```

### 11、幂运算函数: pow
```text
语法: pow(double a, double p)
返回值: double
说明: 返回a的p次幂
```
```text
hive> select pow(2,4) from iteblog;
16.0
```

### 12、幂运算函数: power
```text
语法: power(double a, double p)
返回值: double
说明: 返回a的p次幂,与pow功能相同
```
```text
hive> select power(2,4) from iteblog;
16.0
```

### 13、开平方函数: sqrt
```text
语法: sqrt(double a)
返回值: double
说明: 返回a的平方根
```
```text
hive> select sqrt(16) from iteblog;
4.0
```

### 14、二进制函数: bin
```text
语法: bin(BIGINT a)
返回值: string
说明: 返回a的二进制代码表示
```
```text
hive> select bin(7) from iteblog;
111
```

### 15、十六进制函数: hex
```text
语法: hex(BIGINT a)
返回值: string
说明: 如果变量是int类型，那么返回a的十六进制表示；如果变量是string类型，则返回该字符串的十六进制表示
```
```text
hive> select hex(17) from iteblog;
11
hive> select hex(‘abc’) from iteblog;
616263
```

### 16、反转十六进制函数: unhex
```text
语法: unhex(string a)
返回值: string
说明: 返回该十六进制字符串所代码的字符串
```
```text
hive> select unhex(‘616263’) from iteblog;
abc
hive> select unhex(‘11’) from iteblog;
-
hive> select unhex(616263) from iteblog;
abc
```

### 17、进制转换函数: conv
```text
语法: conv(BIGINT num, int from_base, int to_base)
返回值: string
说明: 将数值num从from_base进制转化到to_base进制
```
```text
hive> select conv(17,10,16) from iteblog;
11
hive> select conv(17,10,2) from iteblog;
10001
```

### 18、绝对值函数: abs
```text
语法: abs(double a) abs(int a)
返回值: double int
说明: 返回数值a的绝对值
```
```text
hive> select abs(-3.9) from iteblog;
3.9
hive> select abs(10.9) from iteblog;
10.9
```

### 19、正取余函数: pmod
```text
语法: pmod(int a, int b),pmod(double a, double b)
返回值: int double
说明: 返回正的a除以b的余数
```
```text
hive> select pmod(9,4) from iteblog;
1
hive> select pmod(-9,4) from iteblog;
3
```

### 20、正弦函数: sin
```text
语法: sin(double a)
返回值: double
说明: 返回a的正弦值
```
```text
hive> select sin(0.8) from iteblog;
0.7173560908995228
```

### 21、反正弦函数: asin
```text
语法: asin(double a)
返回值: double
说明: 返回a的反正弦值
```
```text
hive> select asin(0.7173560908995228) from iteblog;
0.8
```

### 22、余弦函数: cos
```text
语法: cos(double a)
返回值: double
说明: 返回a的余弦值
```
```text
hive> select cos(0.9) from iteblog;
0.6216099682706644
```

### 23、反余弦函数: acos
```text
语法: acos(double a)
返回值: double
说明: 返回a的反余弦值
```
```text
hive> select acos(0.6216099682706644) from iteblog;
0.9
```

### 24、positive函数: positive
```text
语法: positive(int a), positive(double a)
返回值: int double
说明: 返回a
```
```text
hive> select positive(-10) from iteblog;
-10
hive> select positive(12) from iteblog;
12
```

### 25、negative函数: negative
```text
语法: negative(int a), negative(double a)
返回值: int double
说明: 返回-a
```
```text
hive> select negative(-5) from iteblog;
5
hive> select negative(8) from iteblog;
-8
```

## 日期函数

### 1、UNIX时间戳转日期函数: from_unixtime
```text
语法: from_unixtime(bigint unixtime[, string format])
返回值: string
说明: 转化UNIX时间戳（从1970-01-01 00:00:00 UTC到指定时间的秒数）到当前时区的时间格式
```
```text
hive> select from_unixtime(1323308943,'yyyyMMdd') from iteblog;
20111208
```

### 2、获取当前UNIX时间戳函数: unix_timestamp
```text
语法: unix_timestamp()
返回值: bigint
说明: 获得当前时区的UNIX时间戳
```
```text
hive> select unix_timestamp() from iteblog;
1323309615
```

### 3、日期转UNIX时间戳函数: unix_timestamp
```text
语法: unix_timestamp(string date)
返回值: bigint
说明: 转换格式为”yyyy-MM-dd HH:mm:ss”的日期到UNIX时间戳。如果转化失败，则返回0。
```
```text
hive> select unix_timestamp('2011-12-07 13:01:03') from iteblog;
1323234063
```

### 4、指定格式日期转UNIX时间戳函数: unix_timestamp
```text
语法: unix_timestamp(string date, string pattern)
返回值: bigint
说明: 转换pattern格式的日期到UNIX时间戳。如果转化失败，则返回0。
```
```text
hive> select unix_timestamp('20111207 13:01:03','yyyyMMdd HH:mm:ss') from iteblog;
1323234063
```

### 5、日期时间转日期函数: to_date
```text
语法: to_date(string timestamp)
返回值: string
说明: 返回日期时间字段中的日期部分。
```
```text
hive> select to_date('2011-12-08 10:03:01') from iteblog;
2011-12-08
```

### 6、日期转年函数: year
```text
语法: year(string date)
返回值: int
说明: 返回日期中的年。
```
```text
hive> select year('2011-12-08 10:03:01') from iteblog;
2011
hive> select year('2012-12-08') from iteblog;
2012
```

### 7、日期转月函数: month
```text
语法: month (string date)
返回值: int
说明: 返回日期中的月份。
```
```text
hive> select month('2011-12-08 10:03:01') from iteblog;
12
hive> select month('2011-08-08') from iteblog;
8
```

### 8、日期转天函数: day
```text
语法: day (string date)
返回值: int
说明: 返回日期中的天。
```
```text
hive> select day('2011-12-08 10:03:01') from iteblog;
8
hive> select day('2011-12-24') from iteblog;
24
```

### 9、日期转小时函数: hour
```text
语法: hour (string date)
返回值: int
说明: 返回日期中的小时。
```
```text
hive> select hour('2011-12-08 10:03:01') from iteblog;
10
```

### 10、日期转分钟函数: minute
```text
语法: minute (string date)
返回值: int
说明: 返回日期中的分钟。
```
```text
hive> select minute('2011-12-08 10:03:01') from iteblog;
3
```

### 11、日期转秒函数: second
```text
语法: second (string date)
返回值: int
说明: 返回日期中的秒。
```
```text
hive> select second('2011-12-08 10:03:01') from iteblog;
1
```

### 12、日期转周函数: weekofyear
```text
语法: weekofyear (string date)
返回值: int
说明: 返回日期在当前的周数。
```
```text
hive> select weekofyear('2011-12-08 10:03:01') from iteblog;
49
```

### 13、日期比较函数: datediff
```text
语法: datediff(string enddate, string startdate)
返回值: int
说明: 返回结束日期减去开始日期的天数。
```
```text
hive> select datediff('2012-12-08','2012-05-09') from iteblog;
213
```

### 14、日期增加函数: date_add
```text
语法: date_add(string startdate, int days)
返回值: string
说明: 返回开始日期startdate增加days天后的日期。
```
```text
hive> select date_add('2012-12-08',10) from iteblog;
2012-12-18
```

### 15、日期减少函数: date_sub
```text
语法: date_sub (string startdate, int days)
返回值: string
说明: 返回开始日期startdate减少days天后的日期。
```
```text
hive> select date_sub('2012-12-08',10) from iteblog;
2012-11-28
```

## 条件函数

### 1、If函数: if
```text
语法: if(boolean testCondition, T valueTrue, T valueFalseOrNull)
返回值: T
说明: 当条件testCondition为TRUE时，返回valueTrue；否则返回valueFalseOrNull
```
```text
hive> select if(1=2,100,200) from iteblog;
200
hive> select if(1=1,100,200) from iteblog;
100
```

### 2、非空查找函数: COALESCE
```text
语法: COALESCE(T v1, T v2, …)
返回值: T
说明: 返回参数中的【第一个非空值】；如果所有值都为NULL，那么返回NULL
```
```text
hive> select COALESCE(null,'100','50′) from iteblog;
100
```

### 3、条件判断函数：CASE
```text
语法: CASE a WHEN b THEN c [WHEN d THEN e]* [ELSE f] END
返回值: T
说明：如果a等于b，那么返回c；如果a等于d，那么返回e；否则返回f
```
```text
hive> Select case 100 when 50 then 'tom' when 100 then 'mary' else 'tim' end from iteblog;
mary
hive> Select case 200 when 50 then 'tom' when 100 then 'mary' else 'tim' end from iteblog;
tim
```

## 字符串函数

### 1、字符串长度函数：length
```text
语法: length(string A)
返回值: int
说明：返回字符串A的长度
```
```text
hive> select length('abcedfg') from iteblog;
7
```

### 2、字符串反转函数：reverse
```text
语法: reverse(string A)
返回值: string
说明：返回字符串A的反转结果
```
```text
hive> select reverse(abcedfg’) from iteblog;
gfdecba
```

### 3、字符串连接函数：concat
```text
语法: concat(string A, string B…)
返回值: string
说明：返回输入字符串连接后的结果，支持任意个输入字符串
```
```text
hive> select concat(‘abc’,'def’,'gh’) from iteblog;
abcdefgh
```

### 4、带分隔符字符串连接函数：concat_ws
```text
语法: concat_ws(string SEP, string A, string B…)
返回值: string
说明：返回输入字符串连接后的结果，SEP表示各个字符串间的分隔符
```
```text
hive> select concat_ws(',','abc','def','gh') from iteblog;
abc,def,gh
```

### 5、字符串截取函数：substr,substring
```text
语法: substr(string A, int start),substring(string A, int start)
返回值: string
说明：返回字符串A从start位置到结尾的字符串
```
```text
hive> select substr('abcde',3) from iteblog;
cde
hive> select substring('abcde',3) from iteblog;
cde
hive>  select substr('abcde',-1) from iteblog;  （和ORACLE相同）
e
```

### 6、字符串截取函数：substr,substring
```text
语法: substr(string A, int start, int len),substring(string A, int start, int len)
返回值: string
说明：返回字符串A从start位置开始，长度为len的字符串
```
```text
hive> select substr('abcde',3,2) from iteblog;
cd
hive> select substring('abcde',3,2) from iteblog;
cd
hive>select substring('abcde',-2,2) from iteblog;
de
```

### 7、字符串转大写函数：upper,ucase
```text
语法: upper(string A), ucase(string A)
返回值: string
说明：返回字符串A的大写格式
```
```text
hive> select upper('abSEd') from iteblog;
ABSED
hive> select ucase('abSEd') from iteblog;
ABSED
```

### 8、字符串转小写函数：lower,lcase
```text
语法: lower(string A),lcase(string A)
返回值: string
说明：返回字符串A的小写格式
```
```text
hive> select lower('abSEd') from iteblog;
absed
hive> select lcase('abSEd') from iteblog;
absed
```

### 9、去空格函数：trim
```text
语法: trim(string A)
返回值: string
说明：去除字符串两边的空格
```
```text
hive> select trim(' abc ') from iteblog;
abc
```

### 10、左边去空格函数：ltrim
```text
语法: ltrim(string A)
返回值: string
说明：去除字符串左边的空格
```
```text
hive> select ltrim(' abc ') from iteblog;
abc
```

### 11、右边去空格函数：rtrim
```text
语法: rtrim(string A)
返回值: string
说明：去除字符串右边的空格
```
```text
hive> select rtrim(' abc ') from iteblog;
abc
```

### 12、正则表达式替换函数：regexp_replace
```text
语法: regexp_replace(string A, string B, string C)
返回值: string
说明：将字符串A中的符合java正则表达式B的部分替换为C。
注意，在有些情况下要使用转义字符,类似oracle中的regexp_replace函数。
```
```text
hive> select regexp_replace('foobar', 'oo|ar', '') from iteblog;
fb
```

### 13、正则表达式解析函数：regexp_extract
```text
语法: regexp_extract(string subject, string pattern, int index)
返回值: string
说明：将字符串subject按照pattern正则表达式的规则拆分，返回index指定的字符。
```
```text
hive> select regexp_extract('foothebar', 'foo(.*?)(bar)', 1) from iteblog;
the
hive> select regexp_extract('foothebar', 'foo(.*?)(bar)', 2) from iteblog;
bar
hive> select regexp_extract('foothebar', 'foo(.*?)(bar)', 0) from iteblog;
foothebar
```
注意，在有些情况下要使用转义字符，下面的等号要用双竖线转义，这是java正则表达式的规则。
```text
hive> select data_field,
regexp_extract(data_field,'.*?bgStart\\=([^&]+)',1) as aaa,
regexp_extract(data_field,'.*?contentLoaded_headStart\\=([^&]+)',1) as bbb,
regexp_extract(data_field,'.*?AppLoad2Req\\=([^&]+)',1) as ccc
from pt_nginx_loginlog_st
where pt = '2012-03-26' limit 2;
```

### 14、URL解析函数：parse_url
```text
语法: parse_url(string urlString, string partToExtract [, string keyToExtract])
返回值: string
说明：返回URL中指定的部分。
partToExtract的有效值为：HOST, PATH, QUERY, REF, PROTOCOL, AUTHORITY, FILE, and USERINFO.
```
```text
hive> select parse_url('https://www.iteblog.com/path1/p.php?k1=v1&k2=v2#Ref1', 'HOST') from iteblog;
facebook.com
hive> select parse_url('https://www.iteblog.com/path1/p.php?k1=v1&k2=v2#Ref1', 'QUERY', 'k1') from iteblog;
v1
```

### 15、json解析函数：get_json_object
```text
语法: get_json_object(string json_string, string path)
返回值: string
说明：解析json的字符串json_string,返回path指定的内容。如果输入的json字符串无效，那么返回NULL。
```
```text
hive> select  get_json_object('{"store":
>   {"fruit":\[{"weight":8,"type":"apple"},{"weight":9,"type":"pear"}],
>    "bicycle":{"price":19.95,"color":"red"}
>   },
>  "email":"amy@only_for_json_udf_test.net",
>  "owner":"amy"
> }
> ','$.owner') from iteblog;
amy
```

### 16、空格字符串函数：space
```text
语法: space(int n)
返回值: string
说明：返回长度为n的字符串
```
```text
hive> select space(10) from iteblog;
hive> select length(space(10)) from iteblog;
10
```

### 17、重复字符串函数：repeat
```text
语法: repeat(string str, int n)
返回值: string
说明：返回重复n次后的str字符串
```
```text
hive> select repeat('abc',5) from iteblog;
abcabcabcabcabc
```

### 18、首字符ascii函数：ascii
```text
语法: ascii(string str)
返回值: int
说明：返回字符串str第一个字符的ascii码
```
```text
hive> select ascii('abcde') from iteblog;
97
```

### 19、左补足函数：lpad
```text
语法: lpad(string str, int len, string pad)
返回值: string
说明：将str进行用pad进行左补足到len位
```
```text
hive> select lpad('abc',10,'td') from iteblog;
tdtdtdtabc
```
注意：与GP，ORACLE不同，pad 不能默认

### 20、右补足函数：rpad
```text
语法: rpad(string str, int len, string pad)
返回值: string
说明：将str进行用pad进行右补足到len位
```
```text
hive> select rpad('abc',10,'td') from iteblog;
abctdtdtdt
```

### 21、分割字符串函数: split
```text
语法: split(string str, string pat)
返回值: array
说明: 按照pat字符串分割str，会返回分割后的字符串数组
```
```text
hive> select split('abtcdtef','t') from iteblog;
["ab","cd","ef"]
```

### 22、集合查找函数: find_in_set
```text
语法: find_in_set(string str, string strList)
返回值: int
说明: 返回str在strlist第一次出现的位置，strlist是用逗号分割的字符串。如果没有找该str字符，则返回0
```
```text
hive> select find_in_set('ab','ef,ab,de') from iteblog;
2
hive> select find_in_set('at','ef,ab,de') from iteblog;
0
```

## 集合统计函数

### 1、个数统计函数: count
```text
语法: count(*), count(expr), count(DISTINCT expr[, expr_.])
返回值: int
说明: count(*)统计检索出的行的个数，包括NULL值的行；count(expr)返回指定字段的非空值的个数；
count(DISTINCT expr[, expr_.])返回指定字段的不同的非空值的个数
```
```text
hive> select count(*) from iteblog;
20
hive> select count(distinct t) from iteblog;
10
```

### 2、总和统计函数: sum
```text
语法: sum(col), sum(DISTINCT col)
返回值: double
说明: sum(col)统计结果集中col的相加的结果；sum(DISTINCT col)统计结果中col不同值相加的结果
```
```text
hive> select sum(t) from iteblog;
100
hive> select sum(distinct t) from iteblog;
70
```

### 3、平均值统计函数: avg
```text
语法: avg(col), avg(DISTINCT col)
返回值: double
说明: avg(col)统计结果集中col的平均值；avg(DISTINCT col)统计结果中col不同值相加的平均值
```
```text
hive> select avg(t) from iteblog;
50
hive> select avg (distinct t) from iteblog;
30
```

### 4、最小值统计函数: min
```text
语法: min(col)
返回值: double
说明: 统计结果集中col字段的最小值
```
```text
hive> select min(t) from iteblog;
20
```

### 5、最大值统计函数: max
```text
语法: max(col)
返回值: double
说明: 统计结果集中col字段的最大值
```
```text
hive> select max(t) from iteblog;
120
```

### 6、非空集合总体变量函数: var_pop
```text
语法: var_pop(col)
返回值: double
说明: 统计结果集中col非空集合的总体变量（忽略null）
```

### 7、非空集合样本变量函数: var_samp
```text
语法: var_samp (col)
返回值: double
说明: 统计结果集中col非空集合的样本变量（忽略null）
```

### 8、总体标准偏离函数: stddev_pop
```text
语法: stddev_pop(col)
返回值: double
说明: 该函数计算总体标准偏离，并返回总体变量的平方根，其返回值与VAR_POP函数的平方根相同
```

### 9、样本标准偏离函数: stddev_samp
```text
语法: stddev_samp (col)
返回值: double
说明: 该函数计算样本标准偏离
```

### 10．中位数函数: percentile
```text
语法: percentile(BIGINT col, p)
返回值: double
说明: 求准确的第pth个百分位数，p必须介于0和1之间，但是col字段目前只支持整数，不支持浮点数类型
```

### 11、中位数函数: percentile
```text
语法: percentile(BIGINT col, array(p1 [, p2]…))
返回值: array<double>
说明: 功能和上述类似，之后后面可以输入多个百分位数，返回类型也为array<double>，其中为对应的百分位数。
```
```text
select percentile(score,array(0.2,0.4)) from iteblog；
取0.2，0.4位置的数据
```

### 12、近似中位数函数: percentile_approx
```text
语法: percentile_approx(DOUBLE col, p [, B])
返回值: double
说明: 求近似的第pth个百分位数，p必须介于0和1之间，返回类型为double，但是col字段支持浮点类型。
参数B控制内存消耗的近似精度，B越大，结果的准确度越高。默认为10,000。
当col字段中的distinct值的个数小于B时，结果为准确的百分位数
```

### 13、近似中位数函数: percentile_approx
```text
语法: percentile_approx(DOUBLE col, array(p1 [, p2]…) [, B])
返回值: array<double>
说明: 功能和上述类似，之后后面可以输入多个百分位数，返回类型也为array<double>，其中为对应的百分位数。
```

### 14、直方图: histogram_numeric
```text
语法: histogram_numeric(col, b)
返回值: array<struct {‘x’,‘y’}>
说明: 以b为基准计算col的直方图信息。
```
```text
hive> select histogram_numeric(100,5) from iteblog;
[{"x":100.0,"y":1.0}]
```

## 复合类型构建操作
### 1、Map类型构建: map
```text
语法: map (key1, value1, key2, value2, …)
说明：根据输入的key和value对构建map类型
```
```text
hive> Create table iteblog as select map('100','tom','200','mary') as t from iteblog;
hive> describe iteblog;
t       map<string ,string>
hive> select t from iteblog;
{"100":"tom","200":"mary"}
```

### 2、Struct类型构建: struct
```text
语法: struct(val1, val2, val3, …)
说明：根据输入的参数构建结构体struct类型
```
```text
hive> create table iteblog as select struct('tom','mary','tim') as t from iteblog;
hive> describe iteblog;
t       struct<col1:string ,col2:string,col3:string>
hive> select t from iteblog;
{"col1":"tom","col2":"mary","col3":"tim"}
```

### 3、array类型构建: array
```text
语法: array(val1, val2, …)
说明：根据输入的参数构建数组array类型
```
```text
hive> create table iteblog as select array("tom","mary","tim") as t from iteblog;
hive> describe iteblog;
t       array<string>
hive> select t from iteblog;
["tom","mary","tim"]
```

## 复杂类型访问操作

### 1、array类型访问: A[n]
```text
语法: A[n]
操作类型: A为array类型，n为int类型
说明：返回数组A中的第n个变量值。数组的起始下标为0。
比如，A是个值为[‘foo’, ‘bar’]的数组类型，那么A[0]将返回’foo’,而A[1]将返回’bar’
```
```text
hive> create table iteblog as select array("tom","mary","tim") as t from iteblog;
hive> select t[0],t[1],t[2] from iteblog;
tom     mary    tim
```

### 2、map类型访问: M[key]
```text
语法: M[key]
操作类型: M为map类型，key为map中的key值
说明：返回map类型M中，key值为指定值的value值。
比如，M是值为{‘f’ -> ‘foo’, ‘b’ -> ‘bar’, ‘all’ -> ‘foobar’}的map类型，那么M[‘all’]将会返回’foobar’
```
```text
hive> Create table iteblog as select map('100','tom','200','mary') as t from iteblog;
hive> select t['200'],t['100'] from iteblog;
mary    tom
```

### 3、struct类型访问: S.x
```text
语法: S.x
操作类型: S为struct类型
说明：返回结构体S中的x字段。比如，对于结构体struct foobar {int foo, int bar}，foobar.foo返回结构体中的foo字段
```
```text
hive> create table iteblog as select struct('tom','mary','tim') as t from iteblog;
hive> describe iteblog;
t       struct<col1:string ,col2:string,col3:string>
hive> select t.col1,t.col3 from iteblog;
tom     tim
```

## 复杂类型长度统计函数

### 1、Map类型长度函数: size(Map)
```text
语法: size(Map<k .V>)
返回值: int
说明: 返回map类型的长度
```
```text
hive> select size(map('100','tom','101','mary')) from iteblog;
2
```

### 2、array类型长度函数: size(Array)
```text
语法: size(Array<T>)
返回值: int
说明: 返回array类型的长度
```
```text
hive> select size(array('100','101','102','103')) from iteblog;
4
```

### 3、类型转换函数
```text
类型转换函数: cast
语法: cast(expr as )
返回值: Expected “=” to follow “type”
说明: 返回转换后的数据类型
```
```text
hive> select cast(1 as bigint) from iteblog;
1
```

## 空字段赋值
1）函数说明
```text
NVL：给值为NULL的数据赋值，它的格式是NVL( value，default_value)。
它的功能是如果value为NULL，则NVL函数返回default_value的值，否则返回value的值，如果两个参数都为NULL ，则返回NULL。
```
2）数据准备：采用员工表

3）查询：如果员工的comm为NULL，则用- 1 代替
```text
hive (default)> select comm,nvl(comm, -1) from emp;
OK
comm   _c1
NULL   -1.0
300.0  300.0
500.0  500.0
NULL   -1.0
1400.0  1400.0
NULL   -1.0
NULL   -1.0
NULL   -1.0
NULL   -1.0
0.0   0.0
NULL   -1.0
NULL   -1.0
NULL   -1.0
NULL   -1.0
```


4）查询：如果员工的comm为NULL，则用领导id代替
```text
hive (default)> select comm, nvl(comm,mgr) from emp;
OK
comm   _c1
NULL   7902.0
300.0  300.0
500.0  500.0
NULL   7839.0
1400.0  1400.0
NULL   7839.0
NULL   7839.0
NULL   7566.0
NULL   NULL
0.0   0.0
NULL   7788.0
NULL   7698.0
NULL   7566.0
NULL   7782.0
```

## 行转列
### 1 ）相关函数说明

CONCAT(string A/col, string B/col…)：返回输入字符串连接后的结果，支持任意个输入字符串;

CONCAT_WS(separator, str1, str2,…)：它是一个特殊形式的 CONCAT()。第一个参数剩余参数间的分隔符。
分隔符可以是与剩余参数一样的字符串。如果分隔符是 NULL，返回值也将为 NULL。
这个函数会跳过分隔符参数后的任何 NULL 和空字符串。分隔符将被加到被连接的字符串之间;

COLLECT_SET(col)：函数只接受基本数据类型，它的主要作用是将某字段的值进行去重汇总，产生array类型字段。

### 2 ）数据准备
```text
name	constellation	blood_type
孙悟空	白羊座	A
大海	射手座	A
宋宋	白羊座	B
猪八戒	白羊座	A
凤姐	射手座	A
苍老师	白羊座	B
```

### 3 ）需求
把星座和血型一样的人归类到一起。结果如下：
```text
射手座,A            大海|凤姐
白羊座,A            孙悟空|猪八戒
白羊座,B            宋宋|苍老师
```

### 4 ）创建本地constellation.txt，导入数据
```text
[atguigu@hadoop102 datas]$ vi constellation.txt
孙悟空	白羊座	A
大海	     射手座	A
宋宋	     白羊座	B
猪八戒    白羊座	A
凤姐	     射手座	A
```

### 5 ）创建hive表并导入数据
```text
create table person_info(
name string,
constellation string,
blood_type string)
row format delimited fields terminated by "\t";
load data local inpath "/opt/module/data/constellation.txt" into table person_info;
```

### 6 ）按需求查询数据
```text
select
    t1.base,
    concat_ws('|', collect_set(t1.name)) name
from
    (select
        name,
        concat(constellation, ",", blood_type) base
    from
        person_info) t1
group by
    t1.base;
```

## 窗口函数（开窗函数）

### 1 ）相关函数说明

**OVER()**：指定分析函数工作的数据窗口大小，这个数据窗口大小可能会随着行的变而变化。

**CURRENT ROW**：当前行

**n PRECEDING**：往前n行数据

**n FOLLOWING**：往后n行数据

**UNBOUNDED**：起点，UNBOUNDED PRECEDING 表示从前面的起点， UNBOUNDED FOLLOWING表示到后面的终点

**LAG(col,n,default_val)**：往前第n行数据

**LEAD(col,n, default_val)**：往后第n行数据

**NTILE(n)**：把有序窗口的行分发到指定数据的组中，各个组有编号，编号从1开始，对于每一行，NTILE返回此行所属的组的编号。

注意：n必须为int类型。

### 2 ）数据准备：name,orderDate,cost
jack,2017-01-01,10
tony,2017-01-02,15
jack,2017-02-03,23
tony,2017-01-04,29
jack,2017-01-05,46
jack,2017-04-06,42
tony,2017-01-07,50
jack,2017-01-08,55
mart,2017-04-08,62
mart,2017-04-09,68
neil,2017-05-10,12
mart,2017-04-11,75
neil,2017-06-12,80
mart,2017-04-13,94

### 3 ）需求

（1）查询在2017年4月份购买过的顾客及总人数

（2）查询顾客的购买明细及月购买总额

（3）上述的场景, 将每个顾客的cost按照日期进行累加

（4）查询每个顾客上次的购买时间

（5）查询前20%时间的订单信息

### 4 ）创建本地business.txt，导入数据
```text
[atguigu@hadoop102 datas]$ vi business.txt
```

### 5 ）创建hive表并导入数据
```text
create table business(
name string, 
orderdate string,
cost int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
load data local inpath "/opt/module/data/business.txt" into table business;

```

### 6 ）按需求查询数据

（1）查询在2017年4月份购买过的顾客及总人数
```text
select 
    name,count(*) over () 
from business 
where substring(orderdate,1,7) = '2017-04' 
group by name;
```

（2）查询顾客的购买明细及月购买总额
```text
select name,orderdate,cost,sum(cost) over(partition by month(orderdate)) from business;
```

（3）上述的场景, 将每个顾客的cost按照日期进行累加
```text
select 
    name,orderdate,cost,
    sum(cost) over() as sample1,--所有行相加
    sum(cost) over(partition by name) as sample2,--按name分组，组内数据相加
    sum(cost) over(partition by name order by orderdate) as sample3,--按name分组，组内数据累加
    sum(cost) over(partition by name order by orderdate rows between UNBOUNDED PRECEDING and current row ) as sample4 ,--和sample3一样,由起点到当前行的聚合
    sum(cost) over(partition by name order by orderdate rows between 1 PRECEDING and current row) as sample5, --当前行和前面一行做聚合
    sum(cost) over(partition by name order by orderdate rows between 1 PRECEDING AND 1 FOLLOWING ) as sample6,--当前行和前边一行及后面一行
    sum(cost) over(partition by name order by orderdate rows between current row and UNBOUNDED FOLLOWING ) as sample7 --当前行及后面所有行
from business;
```
rows必须跟在Order by 子句之后，对排序的结果进行限制，使用固定的行数来限制分区中的数据行数量

（4）查看顾客上次的购买时间
```text
select 
    name,orderdate,cost,
    lag(orderdate,1,'1900-01-01') over(partition by name order by orderdate ) as time1, 
    lag(orderdate,2) over (partition by name order by orderdate) as time2
from business;
```

（5）查询前20%时间的订单信息
```text
select * from (
    select 
        name,orderdate,cost, 
        ntile(5) over(order by orderdate) sorted
    from business
) t
where sorted = 1;
```

##  Rank

### 1 ）函数说明

RANK() 排序相同时会重复，总数不会变

DENSE_RANK() 排序相同时会重复，总数会减少

ROW_NUMBER() 会根据顺序计算

### 2 ）数据准备
```text
name	subject	score
孙悟空	语文	87
孙悟空	数学	95
孙悟空	英语	68
大海	语文	94
大海	数学	56
大海	英语	84
宋宋	语文	64
宋宋	数学	86
宋宋	英语	84
婷婷	语文	65
婷婷	数学	85
婷婷	英语	78
```

### 3 ）需求

计算每门学科成绩排名。

### 4 ）创建本地score.txt，导入数据
```text
[atguigu@hadoop102 datas]$ vi score.txt
```


### 5 ）创建hive表并导入数据
```text
create table score(
name string,
subject string,
score int)
row format delimited fields terminated by "\t";
load data local inpath '/opt/module/data/score.txt' into table score;
```

### 6 ）按需求查询数据
```text
select 
    name,
    subject,
    score,
    rank() over(partition by subject order by score desc) rp,
    dense_rank() over(partition by subject order by score desc) drp,
    row_number() over(partition by subject order by score desc) rmp
from score;
```
结果：
```text
name    subject score   rp      drp     rmp
孙悟空  数学    95      1       1       1
宋宋    数学    86      2       2       2
婷婷    数学    85      3       3       3
大海    数学    56      4       4       4
宋宋    英语    84      1       1       1
大海    英语    84      1       1       2
婷婷    英语    78      3       2       3
孙悟空  英语    68      4       3       4
大海    语文    94      1       1       1
孙悟空  语文    87      2       2       2
婷婷    语文    65      3       3       3
宋宋    语文    64      4       4       4
```

## 列转行
行转列：将多个列中的数据在一列中输出
列转行：将某列一行中的数据拆分成多行

### Explode炸裂函数
将hive某列一行中复杂的 array 或 map 结构拆分成多行（只能输入array或map）

语法:
```text
explode(col)
```
例如：
```text
select explode(arraycol) as newcol from tablename;

explode()：函数中的参数传入的是array数据类型的列名。
newcol：是给转换成的列命名一个新的名字，用于代表转换之后的列名。
tablename：原表名
```

explode(array)使得结果中将array列表里的每个元素生成一行
```text
// 输入是array时，array中的每个元素都单独输出为一行
select(array('1','2','3'))
1
2
3
```
explode(map)使得结果中将map里的每一对元素作为一行，key为一列，value为一列，
```text
select explode(map('A','1','B','2','C','3'))
A   1
B   2
C   3
```
通常，explode函数会与lateral view一起结合使用

### posexplode()函数
对一列进行炸裂可以使用 explode()函数，但是如果想实现对两列都进行多行转换，那么用explode()函数就不能实现了，
可以用posexplode()函数，因为该函数可以将index和数据都取出来，使用两次posexplode并令两次取到的index相等就行了。

```text
详见例子4和例子5
```

### Lateral View
Lateral View配合 split, explode 等UDTF函数一起使用。
它能够将一列数据拆成多行数据，并且对拆分后结果进行聚合，即将多行结果组合成一个支持别名的虚拟表

语法：
```text
lateral view udtf(expression) tableAlias as columnAlias (,columnAlias)*
```

lateral view在UDTF前使用，表示【连接】UDTF所分裂的字段。

UDTF(expression)：使用的UDTF函数，例如explode()。

tableAlias：表示UDTF函数转换的虚拟表的名称。

columnAlias：表示虚拟表的虚拟字段名称，如果分裂之后有一个列，则写一个即可；
如果分裂之后有多个列，按照列的顺序在括号中声明所有虚拟列名，以逗号隔开。

Lateral View主要解决在select使用UDTF做查询的过程中，查询只能包含单个UDTF，
不能包含其它字段以及多个UDTF的情况（不能添加额外的select列的问题）。

Lateral View其实就是用来和类似explode这种UDTF函数联用的，
lateral view会将UDTF生成的结果放到一个虚拟表中，
然后这个虚拟表会和输入行进行join来达到连接UDTF外的select字段的目的。
```text
注：
1）lateral view的位置是from后where条件前
2）生成的虚拟表的表名不可省略
3）from后可带多个lateral view
3）如果要拆分的字段有null值，需要使用lateral view outer 替代，避免数据缺失
```

#### 例子1

假设有如下movies表，字段名分别为movie(string)和category(array<string>)
```text
movie           category
《嫌犯追踪》      悬疑,科幻,动作,剧情
《Lie to me》    悬疑,警匪,动作,心理
《战狼2》         战争,动作,灾难
```
转换为
```text
movie           category_name
《嫌犯追踪》      悬疑
《嫌犯追踪》      科幻
《嫌犯追踪》      动作
《嫌犯追踪》      剧情
《Lie to me》     悬疑
《Lie to me》     警匪
《Lie to me》     动作
《Lie to me》     心理
《战狼2》           战争
《战狼2》           动作
《战狼2》           灾难
```
SQL：
```text
select 
    movie,category_name
from  movies
lateral view explode(category) table_tmp as category_name;
```
注：explode函数输入了一个string类型的参数，搭配split()函数

#### 例子2
test表
```text
a       b       1,2,3
c       d       4,5,6
```
转换为
```text
a       b       1
a       b       2
a       b       3
c       d       4
c       d       5
c       d       6
```
SQL：
```text
select 
    col1, col2, col5
from test
lateral view explode(split(col3,','))  b AS col5
// split(col3,",")相对字符串切割，得到数组
```
注：explode函数输入了一个string类型的参数，搭配split()函数

#### 例子3
应用文章的一个思考题(掌握这个SQL技巧超越80%的人——行转列/列转行)
```text
uid         game_list
a,b,c       王者荣耀,刺激战场
e,b,c       极品飞车,实况足球,天天飞车
```
SQL：
```text
select 
    uid_split, game
from (
    select 
        uid,game
    from user_game
    lateral view explode(split(game_list,",")) tmpTable as game
) a
lateral view explode(split(uid, ",")) m as uid_split
```

#### 例子4——多列炸裂 posexplode
前面提到：

对一列进行炸裂可以使用 explode()函数，但是如果想实现对两列都进行多行转换，那么用explode()函数就不能实现了，
可以用posexplode()函数，因为该函数可以将index和数据都取出来，使用两次posexplode并令两次取到的index相等就行了。

引用例子来自：Hive中的explode使用全解
```text
class   student     score
1班      小A,小B,小C    80,92,70
2班      小D,小E        88,62
3班      小F,小G,小H    90,97,85
```

单列posexplode
```text
select
    class,student_index + 1 as student_index,student_name
from
    default.classinfo
    lateral view posexplode(split(student,',')) t as student_index,student_name;
```

双列炸裂，可以进行两次posexplode，姓名和成绩都保留对应的序号，即使变成了9行，也通过where条件只保留序号相同的行即可。
```text
select 
	class,student_name,student_score
from 
	default.classinfo
    lateral view posexplode(split(student,',')) sn as student_index_sn,student_name
    lateral view posexplode(split(score,',')) sc as student_index_sc,student_score
where
    student_index_sn = student_index_sc;
```

例子5
引用例子来自：https://blog.csdn.net/dzysunshine/article/details/101110467
```text
a,b,c,d-2:00,3:00,4:00,5:00
f,b,c,d-1:10,2:20,3:30,4:40
```
转换成如下形式
```text
single_id  single_tim
a			2:00
b			3:00
c			4:00
d			5:00
f			1:10
b			2:20
c			3:30
d			4:40
```

一次posexplode
```text
select 
    id,tim,single_id_index,single_id
from test.a
lateral view posexplode(split(id,',')) t as single_id_index, single_id;
```
结果
```text
single_id_index		single_id
0					a
1					b
2					c
3					d
0					f
1					b
2					c
3					d
```

两次posexplode+where筛选
```text
select
    id,tim,single_id,single_tim
from
    test.a
    lateral view posexplode(split(id,',')) t as single_id_index, single_id
    lateral view posexplode(split(tim,',')) t as single_yim_index, single_tim
where
    single_id_index = single_yim_index;
```

#### 例子6
如下表
```text
column1,column2,column3,X1,X2,X3,X4
A1,     A2,     A3,     5,  6, 1, 4
```
想转换成：
```text
column1,column2,column3,m_key,m_val
A1,     A2,     A3,     X1, 5
A1,     A2,     A3,     X2, 6
A1,     A2,     A3,     X3, 1
A1,     A2,     A3,     X4, 4
```
SQL：
```text
select 
    column1, column2, column3, m_key, m_val 
from
    (select 
        column1, column2, column3, 
        map("X1", X1, "X2", X2, "X3", X3, "X4", X4) as map1
     from table1
     ) as t1
lateral view explode(map1) xyz as m_key, m_val    
```

## 行转列
行转列：将多个列中的数据在一列中输出
列转行：将某列一行中的数据拆分成多行

### concat
语法：
```text
concat(string1/col, string2/col, …)
输入任意个字符串(或字段,可以为int类型等)，返回拼接后的结果
```
如：
```text
select concat(id,'-',name,'-',age) from student;
```

### concat_ws

语法：
```text
concat_ws(separator, str1, str2, …)

特殊形式的 concat()，参数只能为字符串，第一个参数为后面参数的分隔符。
分隔符可以是与后面参数一样的字符串。
如果分隔符是 NULL，返回值也将为 NULL。
这个函数会跳过分隔符参数后的任何 NULL 和空字符串。
分隔符将被加到被连接的字符串之间。
```
如：
```text
select concat_ws('-', name, gender) from student;
```

### collect_set（聚合，返回数组类型）
语法：
```text
collect_set(col)

将某字段进行去重处理，返回array类型；该函数只接受基本数据类型
```
如：
```text
select collect_set(age) from student;
```

collect_set 与 collect_list 的区别就是set去重，list不去重。

### 例子1：
把星座和血型一样的人归类到一起

| name | constellation | blood_type |
|------|---------------|------------|
| 孙悟空  | 白羊座           | A          |
| 大海   | 射手座           | A          |
| 宋宋   | 白羊座           | B          |
| 猪八戒  | 白羊座           | A          |
| 凤姐   | 射手座           | A          |

结果：
```text
射手座,A            大海|凤姐
白羊座,A            孙悟空|猪八戒
白羊座,B            宋宋
```
SQL：
```text
select
    t1.base,
    concat_ws('|', collect_set(t1.name)) as name
from
    (select name,concat(constellation, ",", blood_type) as base
    from person_info) as t1
group by
    t1.base;
```

### 例子2
数据：

| stu_name | course | score |
|----------|--------|-------|
| 张三       | 语文     | 98    |
| 张三       | 数学     | 95    |
| 张三       | 英语     | 89    |
| 李四       | 语文     | 97    |
| 李四       | 数学     | 88    |
| 李四       | 英语     | 90    |

结果：
```text
张三    语文,数学,英语   98,95,89
李四    语文,数学,英语   97,88,90
```
SQL：
```text
select 
	stu_name,
	concat_ws(',',collect_set(course)) as course,
	concat_ws(',',collect_set(score)) as score
from student
group by stu_name
```

另外，行转列常用函数还有
```text
case when <expr> then <result>…else <default> end

if(expr, true_result, false_result)
```
case when 语句是SQL中的一个非常重要的功能，可以完成很多复杂的计算，相当于一个表达式，可以放在任何可放表达式的地方。

语法： 
```text
case when 条件 then 结果 when 条件 then 结果 else end
```
else可不加，是缺省条件下的值，如果不加，有缺省情况则为NULL。
CASE WHEN 还可以和GROUP BY 语句搭配使用，用在sum,count,max等聚合函数内部。

如：
```text
select 
    class_id, 
    max(case when gender = 'm' then 1 else 0 end) as num_boy, 
    max(case when gender = 'f' then 1 else 0 end) as num_girl 
from student 
group by class_id;

select 
    class_id, 
    max(if(gender = 'm', 1 ,0 )) as num_boy, 
    max(if(gender = 'f', 1 ,0 )) as num_girl 
from student 
group by class_id;
```



