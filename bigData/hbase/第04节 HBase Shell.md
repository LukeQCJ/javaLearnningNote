# HBase Shell
本章介绍如何启动HBase随附的HBase交互式Shell。

## HBase外壳
HBase包含一个shell，您可以使用shell与HBase通信。
HBase使用Hadoop文件系统存储其数据。 
它有一个主服务器和区域服务器。数据存储将采用区域（表）的形式。这些区域将被拆分并存储在区域服务器中。

主服务器**管理**这些区域服务器，所有这些任务都在HDFS上进行。下面给出的是HBase Shell支持的一些命令。

## 通用命令
* status：提供的HBase的状态，例如，服务器的数量。
* version：提供正在使用的HBase的版本。
* table_help：提供有关表引用命令的帮助。
* whoami：提供有关用户的信息。

## 数据定义语言
这些是在HBase中的表上运行的命令。

* create：创建表。
* list：列出HBase中所有的表。
* disable：禁用表。
* is_disabled：验证表是否被禁用。
* enable：启用表。
* is_enabled：验证表是否被启用。
* describe：表的描述信息。
* alter：修改一张桌子。
* exists：验证表是否存在。
* drop-从HBase删除表。
* drop_all：删除命令中匹配’regex’的表。
* Java 管理 API：在上述所有命令之前，Java提供了一个管理API，通过编程来实现DDL功能。 
  org.apache.hadoop.hbase.client包执行HBaseAdmin和HTableDescriptor是这个包中提供DDL功能的两个重要类。

## 数据处理语言
* put：将单元格值放在特定表中指定行中的指定列。
* get：获取行或单元格的内容。
* delete：删除表中的单元格值。
* deleteall：删除给定行中的所有单元格。
* scan：扫描并返回表数据。
* count：计算并返回表中的行数。
* truncate：禁用，删除并重新创建指定的表。
* Java 客户端 API：在上述所有命令之前，Java在org.apache.hadoop.hbase.client软件包下提供了客户端API，
  以实现DML功能，CRUD（创建检索更新删除）操作以及更多其他功能。 HTable的Put和Get是此程序包中的重要类。

## 启动HBase Shell
要访问HBase Shell，必须导航到HBase主文件夹。
```text
cd /usr/localhost/
cd hbase
```

您可以使用hbase shell命令来启动HBase交互式Shell ，如下所示。
```text
./bin/hbase shell
```

如果您已经在系统中成功安装了HBase，则它会为您提供HBase shell提示，如下所示。
```text
HBase Shell
Use "help" to get list of supported commands.
Use "exit" to quit this interactive shell.
For Reference, please visit: http://hbase.apache.org/2.0/book.html#shell
Version 2.4.0, r282ab70012ae843af54a6779543ff20acbcbb629, Thu Dec  3 09:58:52 PST 2020
Took 0.0020 seconds
hbase:001:0>
```

要随时退出交互式Shell命令，请键入exit或使用ctrl + c。在继续进行之前，请检查shell的功能。
为此，请使用list命令。List是用于获取HBase中所有表的列表的命令。首先，使用以下命令验证系统中HBase的安装和配置。
```text
hbase(main):001:0> list
```

键入此命令时，它将提供以下输出。
```text
hbase(main):001:0> list
TABLE
```

# HBase 通用命令
HBase中的常规命令是status，version，table_help和whoami。本章介绍这些命令。

## status
此命令返回系统状态，包括系统上运行的服务器的详细信息。其语法如下：
```text
hbase(main):009:0> status
```

如果执行此命令，它将返回以下输出。
```text
hbase(main):009:0> status
3 servers, 0 dead, 1.3333 average load
```

## version
该命令返回系统中使用的HBase版本。其语法如下：
```text
hbase(main):010:0> version
```

如果执行此命令，它将返回以下输出。
```text
hbase(main):009:0> version
2.4.0, r282ab70012ae843af54a6779543ff20acbcbb629, Thu Dec  3 09:58:52 PST 2020
```

## table_help
该命令指导您如何以及如何使用表引用的命令。下面给出的是使用此命令的语法。
```text
hbase(main):02:0> table_help
```

使用此命令时，它将显示与表相关的命令的帮助主题。下面给出的是此命令的部分输出。
```text
Help for table-reference commands.
You can either create a table via 'create' and then manipulate the table via commands like 'put', 'get', etc.
See the standard help information for how to use each of these commands.
However, as of 0.96, you can also get a reference to a table, on which you can invoke commands.
For instance, you can get create a table and keep around a reference to it via:
hbase> t = create 't', 'cf'…...
```

## whoami
该命令返回HBase的用户详细信息。如果执行此命令，则返回当前的HBase用户，如下所示。
```text
hbase(main):008:0> whoami
hadoop (auth:SIMPLE)
groups: hadoop
```

---

# HBase的基本shell操作

1- 如何进入HBase的操作命令的控制台
```text
> hbase shell
```

2- 如何查看HBase的命令帮助文档
```text
> help
```

![help命令](img/04/hbaseHelpCommand01.png)

```text
查看某一个命令如何使用:

格式: help '命令'

例如: hbase(main):002:0> help 'create'
```

3 - 查看集群状态: status

4- 查看HBase有那些表: list

5-如何创建一张表：
```text
格式:
create '表名', '列族1','列族2'......
```

6- 如何向表中添加数据: put
```text
格式:
put '表名', 'rowkey值','列族:列名','列值'
```

7- 如何读取某一个rowkey的数据
```text
格式:
get '表名','rowkey值'[,'列族1','列族2'... || '列族1:列名1','列族2:列名2'... || '列族1','列族1:列名1'... ]
```

8- 如何修改表中数据
```text
修改与添加数据的操作,是一致的, 只需要保证rowkey相同, 就是修改操作
```

9- 删除数据: delete 和 deleteAll
```text
格式:
delete '表名','rowkey','列族:列名'

    deleteall '表名','rowkey' [,'列族:列名']

delete 和 deleteall 区别:

共同点: 都是用于执行删除数据的操作

区别点:
1) delete操作, 只能删除某个列的数据, deleteall 支持删除整行数据
2) 通过delete删除某个列的数据时候, 默认只删除最新的版本, 而deleteall直接将所有的版本全部都删除
```

10- 如何查看表结构
```text
格式:
describe '表名'
```

11- 如何清空表
```text
格式:
truncate '表名'

底层:  先将表禁用 --> 删除表 --> 创建表
```

12- 如何删除表
```text
格式:
drop '表名'

如何禁用表:
disable '表名'

如何启用表:
enable '表名'

如何判断表是否是禁用/启用:
is_disabled '表名'
is_enabled '表名'
```

13- 如何查询多个数据
```text
格式:  
scan '表名'[,{COLUMNS=>['列族1','列族2'] || COLUMNS=>['列族1','列族2:列名'] || COLUMNS=>['列族1:列名','列族2:列名'], FORMATTER=>'toString', LIMIT=>N,STARTROW=>'起始rowkey', ENDROW=>'结束rowkey']

范围查询:
STARTROW=>'起始rowkey', ENDROW=>'结束rowkey'
包头不包尾

注意: 当只写STARTROW 不写 ENDROW, 表示 从指定的rowkey开始 直到结束

说明:
FORMATTER=>'toString' 用于显示中文
LIMIT=>N :  显示前N行数据

注意:
1- 每一个属性 都可以随意使用, 并不是必须组合在一起
2- 也不存在先后的顺序
3- 大小写是区分, 不要写错
```

14- 查看表共计有多少条数据
```text
格式:
count '表名'
```

---

# HBase高级shell操作

1- HBase的过滤器查询操作
```text
格式:
scan '表名',{FILTER=>"过滤器的名字(比较运算符,比较器表达式)"}

常见的过滤器:
    rowkey相关的过滤器:
        RowFilter:   实现行键字符串的比较和过滤操作
        PrefixFilter: rowkey的前缀过滤器

    列族过滤器:
        FamilyFilter: 列族过滤器
    列名过滤器:
        QualifierFilter: 列名过滤器
    
    列值过滤器: 
        ValueFilter: 列值过滤器, 找到符合对应列的数据值
        SingleColumnValueFilter: 在指定的列族和列名中进行比较具体的值, 将符合的数据全部都返回(包含条件的内容字段)
        SingleColumnValueExcludeFilter: 在指定的列族和列名中进行比较具体的值, 将符合的数据全部都返回(不包含条件的内容字段)


比较运算符: >  < >= <= !=

比较器:
    比较器                       比较器表达式
    BinaryComparator            binary:值              完整匹配字节数据
    BinaryPrefixComparator      binaryprefix: 值       匹配字节数据的前缀
    NullComparator              null                   匹配null值
    SubstringComparator         substring:值           模糊匹配操作

HBase的 API 文档: https://hbase.apache.org/2.1/apidocs/index.html
```

2 - 查询rowkey中以 rk 开头的数据
```text
scan 'test01',{FILTER=>"PrefixFilter('rk')"}
```

3 - 查询在列名中包含 a字段的列有哪些?
```text
scan 'test01',{FILTER=>"QualifierFilter(=,'substring:a')"}
```

4- 查询在f1列族下 name列中 包含 z 展示出来
```text
scan 'test01',{FILTER=>"ValueFilter(=,'substring:z')"}  -- 不满足要求

-- 找到后, 将整个数据全部都返回了
scan 'test01',{FILTER=>"SingleColumnValueFilter('f1','name',=,'substring:z')"}  

scan 'test01',{FILTER=>"SingleColumnValueExcludeFilter('f1','name',=,'substring:z')"}
```

5- whoami: 显示HBase当前登录使用用户

6- exists: 判断表是否存在

7- alter 修改表结构信息
```text
添加列族:
alter  '表名' , NAME =>'新列族'[,VERSION=>N]

删除列族:
alter '表名','delete' =>'旧列族'
```


