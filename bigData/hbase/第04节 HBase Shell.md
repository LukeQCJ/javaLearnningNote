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
