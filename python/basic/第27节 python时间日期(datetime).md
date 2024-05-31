# Python 时间日期(datetime)

datetime 模块提供用于处理日期和时间的类。
在支持日期时间数学运算的同时，实现的关注点更着重于如何能够更有效地解析其属性用于格式化输出和数据操作。
本文主要介绍Python中的时间日期(datetime)。

## 1、Python 时间日期(datetime)
Python中的时间日期不是其本身的数据类型，但是我们可以导入名为datetime的模块以将时间日期用作时间日期对象。

例如：

导入datetime模块并显示当前时间日期：
```text
import datetime

x = datetime.datetime.now()
print(x)
```
output:
```text
2024-05-31 16:15:39.574222
```

## 2、输出时间日期
当我们从上面的示例执行代码时，结果将是：
```text
2024-05-31 16:15:39.574222
```

该日期包含年，月，日，小时，分钟，秒和微秒。

datetime模块具有许多方法来返回有关日期对象的信息。

这里有一些示例，将在本章稍后了解更多有关它们的信息：

例如：

返回年份和工作日名称：
```text
import datetime

x = datetime.datetime.now()

print(x.year)
print(x.strftime("%A"))
```
output:
```text
2024
Friday
```

## 3、创建时间日期datetime对象
要创建日期，我们可以使用datetime模块的datetime()类（构造函数）。

datetime()类需要三个参数来创建日期：年，月，日。

例如：

创建一个日期对象：
```text
import datetime

x = datetime.datetime(2020, 5, 17)

print(x)
```
output:
```text
2020-05-17 00:00:00
```

datetime()类还接受时间和时区的参数(hour, minute, second, microsecond, tzone)，
但它们是可选的，其默认值为0，（tzone时区为None）。

## 4、strftime()方法
datetime对象具有一种将日期对象格式化为可读字符串的方法。

该方法称为strftime()，它采用一个参数format来指定返回字符串的格式：

例如：

显示月份名称：
```text
import datetime

x = datetime.datetime(2018, 6, 1)

print(x.strftime("%B"))
```
output:
```text
June
```

一个参考的所有合法的格式代码:

| 标识	 | 含义	                                                     | 举例                           |
|-----|---------------------------------------------------------|------------------------------|
| %a	 | 星期简写	                                                   | Mon                          |
| %A	 | 星期全称	                                                   | Monday                       |
| %b	 | 月份简写	                                                   | Mar                          |
| %B	 | 月份全称	                                                   | March                        |
| %c	 | 适合语言下的时间表示	                                             | May Mon May 20 16:00:02 2013 |
| %d	 | 一个月的第一天，取值范围： [01,31].	                                 | 20                           |
| %H	 | 24小时制的小时，取值范围[00,23].	                                  | 17                           |
| %I	 | 12小时制的小时，取值范围 [01,12].	                                 | 10                           |
| %j	 | 一年中的第几天，取值范围 [001,366].	                                | 120                          |
| %m	 | 十进制月份，取值范围[01,12].	                                     | 50                           |
| %M	 | 分钟，取值范围 [00,59].	                                       | 50                           |
| %p	 | 上、下午，AM 或 PM.	                                          | PM                           |
| %S	 | 秒，取值范围 [00,61].	                                        | 30                           |
| %U	 | 这一年的星期数（星期天为一个星期的第一天，开年的第一个星期天之前的天记到第0个星期）趋势范围[00,53]。	 | 20                           |
| %w	 | 星期的十进制表示，取值范围 [0(星期天),6].	1                             |
| %W	 | 这一年的星期数（星一为一个星期的第一天，开年的第一个星期一之前的天记到第0个星期）趋势范围[00,53]。	  | 20                           |
| %x	 | 特定自然语言下的日期表示	                                           | 05/20/13                     |
| %X	 | 特定自然语言下的时间表示	                                           | 16:00:02                     |
| %y	 | 年的后两位数，取值范围[00,99].	                                    | 13                           |
| %Y	 | 完整的年	                                                   | 2013                         |
| %Z	 | 时区名	                                                    | CST（China Standard Time）     |
| %%	 | %字符	                                                    | %                            |

## 5、datetime模块介绍
1）datetime模块中包含如下**类**：

| 类名	            | 功能说明                                           |
|----------------|------------------------------------------------|
| date	          | 日期对象,常用的属性有year, month, day                    |
| time	          | 时间对象                                           |
| datetime	      | 日期时间对象,常用的属性有hour, minute, second, microsecond |
| datetime_CAPI	 | 日期时间对象C语言接口                                    |
| timedelta	     | 时间间隔，即两个时间点之间的长度                               |
| tzinfo	        | 时区信息对象                                         |

2）**静态方法和字段**
```text
datetime.today()：返回一个表示当前本地时间的datetime对象；
datetime.now([tz])：返回一个表示当前本地时间的datetime对象，如果提供了参数tz，则获取tz参数所指时区的本地时间；
datetime.utcnow()：返回一个当前utc时间的datetime对象；#格林威治时间
datetime.fromtimestamp(timestamp[, tz])：根据时间戮创建一个datetime对象，参数tz指定时区信息；
datetime.utcfromtimestamp(timestamp)：根据时间戮创建一个datetime对象；
datetime.combine(date, time)：根据date和time，创建一个datetime对象；
datetime.strptime(date_string, format)：将格式字符串转换为datetime对象；
```

3）**方法和属性**
```text
dt = datetime.now()#datetime对象
# dt.year、month、day、hour、minute、second、microsecond、tzinfo：
dt.date()：获取date对象；
dt.time()：获取time对象；
dt.replace([year[,month[,day[,hour[,minute[,second[,microsecond[,tzinfo]]]]]]]])：
dt.timetuple()
dt.utctimetuple()
dt.toordinal()
dt.weekday()
dt.isocalendar()
dt.isoformat([ sep] )
dt.ctime()：返回一个日期时间的C格式字符串，等效于time.ctime(time.mktime(dt.timetuple()))；
dt.strftime(format)
```

## 6、使用timedelta类实现时间加减
```text
# coding:utf-8
from datetime import *

dt = datetime.now()
# 日期减一天
dt1 = dt + timedelta(days=-1)  # 昨天
dt2 = dt - timedelta(days=1)  # 昨天
dt3 = dt + timedelta(days=1)  # 明天
delta_obj = dt3 - dt
print(type(delta_obj), delta_obj)  # <type 'datetime.timedelta'> 1 day, 0:00:00
print(delta_obj.days, delta_obj.total_seconds())  # 1 86400.0
```

## 7、tzinfo时区类
```text
#! /usr/bin/python
# coding=utf-8

from datetime import datetime, tzinfo, timedelta

"""
tzinfo是关于时区信息的类
tzinfo是一个抽象类，所以不能直接被实例化
"""


class UTC(tzinfo):
    """UTC"""

    def __init__(self, offset=0):
        self._offset = offset

    def utcoffset(self, dt):
        return timedelta(hours=self._offset)

    def tzname(self, dt):
        return "UTC +%s" % self._offset

    def dst(self, dt):
        return timedelta(hours=self._offset)


# 北京时间
beijing = datetime(2011, 11, 11, 0, 0, 0, tzinfo=UTC(8))
print("beijing time:", beijing)
# 曼谷时间
bangkok = datetime(2011, 11, 11, 0, 0, 0, tzinfo=UTC(7))
print("bangkok time", bangkok)
# 北京时间转成曼谷时间
print("beijing-time to bangkok-time:", beijing.astimezone(UTC(7)))

# 计算时间差时也会考虑时区的问题
timespan = beijing - bangkok
print("时差:", timespan)
```
output:
```text
beijing time: 2011-11-11 00:00:00+08:00
bangkok time 2011-11-11 00:00:00+07:00
beijing-time to bangkok-time: 2011-11-10 23:00:00+07:00
时差: -1 day, 23:00:00
```