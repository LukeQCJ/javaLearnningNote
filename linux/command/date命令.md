## Linux date命令介绍
date命令在Linux中用来显示和设置系统日期和时间。这个命令允许用户以不同的格式打印时间，也可以计算未来和过去的日期。

## Linux date命令适用的Linux版本
date命令在所有主流的Linux发行版中都可以使用，
包括但不限于Debian、Ubuntu、Alpine、Arch Linux、Kali Linux、RedHat/CentOS、Fedora和Raspbian。
无需特别的安装过程，因为date是Linux内置的程序。

## Linux date命令的基本语法
date命令的语法格式如下：
```text
date [options]... [+format]
```

## Linux date命令的常用选项或参数说明
```text
选项	说明
-d	允许用户操作具体的日期
–date	显示给定的日期字符串格式
–set	更改系统时钟
–file	印出文件里的每一行的日期字符串
-r	打印文件最后修改时间
+%s	显示从1970/01/01 00:00:00 UTC到现在为止的秒数
```

## Linux date命令实例详解
实例1：显示当前的系统时间和日期
```text
[root@master ~]# date
2024年 05月 05日 星期日 15:34:00 CST
```

实例2：操作具体日期
```text
[root@master ~]# date -d "2000-11-22 09:10:15"
2000年 11月 22日 星期三 09:10:15 CST
```

实例3：显示给定的日期字符串作为日期格式
```text
[root@master ~]# date --date="09/10/1960"
1960年 09月 10日 星期六 00:00:00 CST
```

实例4：设置或更改Linux中的时间
```text
[root@master ~]# date --set="20100513 05:30"
2010年 05月 13日 星期四 05:30:00 CST
[root@master ~]# date
2010年 05月 13日 星期四 05:30:24 CST

# 又把时间改回来
[root@master ~]# date --set="20240505 15:36"
2024年 05月 05日 星期日 15:36:00 CST
[root@master ~]# date
2024年 05月 05日 星期日 15:36:01 CST
```

实例5：显示过去的日期
```text
[root@master ~]# date --date="2 year ago"
2022年 05月 05日 星期四 15:37:11 CST
[root@master ~]# date --date="yesterday"
2024年 05月 04日 星期六 15:37:33 CST
[root@master ~]# date --date="10 sec ago"
2024年 05月 05日 星期日 15:37:43 CST
```

实例6：显示未来的日期
```text
[root@master ~]# date --date="next monday"
2024年 05月 06日 星期一 00:00:00 CST
[root@master ~]# date --date="4 day"
2024年 05月 09日 星期四 15:38:40 CST
[root@master ~]# date --date="tomorrow"
2024年 05月 06日 星期一 15:38:49 CST
```

实例7：自定义格式显示日期
```text
[root@master ~]# date +"Year: %Y, Month: %m, Day: %d"
Year: 2024, Month: 05, Day: 05
[root@master ~]# date "+DATE: %D%nTIME: %T"
DATE: 05/05/24
TIME: 15:40:02
[root@master ~]# date +"DATE: %D%nTIME: %T"
DATE: 05/05/24
TIME: 15:40:31
[root@master ~]# date +"Week number: %V Year: %y"
Week number: 18 Year: 24
```

实例8：显示文件最后修改时间
```text
[root@master ~]# date -r /etc/hosts
2023年 09月 02日 星期六 23:10:23 CST
```
实例9：更改时间区域为New York时间
```text
[root@master ~]# TZ='America/New_York' date
2024年 05月 05日 星期日 03:42:13 EDT
```

实例10：使用date命令创建包含当前时间和日期的文件名
```text
[root@master ~]# echo "hello" >> test-$(date +%Y%m%d).log
[root@master ~]# ll test*
-rw-r--r-- 1 root root 6 5月   5 15:44 test-20240505.log
```

实例11：在shell脚本中使用date命令

下面我们将date命令的输出分配给date_now变量：
```text
[root@master ~]# date_now=$(date "+%F-%H-%M-%S")
```

实例12：使用date命令作为Epoch转换器

Epoch，或Unix时间戳，是从1970年1月1日00:00:00 UTC到目前为止的秒数。

```text
[root@master ~]# date +%s
1714895156
[root@master ~]# date -d "1984-04-08" +"%s"
450201600
```

## Linux date命令的注意事项
使用date命令需要注意：
- date命令默认使用操作系统的时区，除非另有指定。
- 使用–date选项不会影响系统的实际日期和时间值，它只是打印请求的日期。
- 在设置系统时钟时要谨慎，因为多数Linux发行版已经使用NTP或systemd-timesyncd服务同步系统时钟了。

如果遇到“bash: date: command not found”的错误提示，那就按照上面的步骤安装相关程序即可。

## Linux date相关命令
- cal命令：用于显示日历
- tzselect命令：用于查看或更改系统时区
- timedatectl命令：用于管理和配置系统时间和日期
- printf命令：用于格式化并打印数据
