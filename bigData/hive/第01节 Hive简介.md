Hive是一个架构在Hadoop之上的【数据仓库】基础工具，它可以处理【结构化】和【半结构化】数据，
它使得查询和分析存储在Hadoop上的数据变得非常方便。

在没有Hive之前，处理数据必须开发复杂的MapReduce作业，但现在有了Hive，
你只要开发简单的SQL查询就可以达到MapReduce作业同样的查询功能。
Hive主要针对的是熟悉SQL的用户。
Hive 使用的查询语言称为 HiveQL（HQL），它跟 SQL 很像。HiveQL 自动把类 SQL 语句转换成 MapReduce 作业。
Hive 对 Hadoop 的复杂性简单化了，而且使用 Hive 并不需要你学习 Java 语言。

Hive一般在终端执行，并且把SQL语句转换成一系列能在Hadoop集群执行作业。
Apache Hive可以让存储在HDFS的数据以表的方式呈现。

## Apache Hive 历史简介
Hive 由 Facebook 的技术团队开发的，Apache Hive 是众多满足 Facebook 业务需求的技术之一。
它非常受 Facebook 内部所有用户的欢迎。它可以开发各种数据相关的应用，并且运行在具有数百个用户的集群之上。
Facebook 的 Apache Hadoop 集群存储超过 2PB 的原始数据，并且每天定期加载 15TB 的数据。
现在 Hive 被很多大公司使用并完善，比如亚马逊，IBM，雅虎，Netflix 等等大公司。

## 为什么使用 Apache Hive
在 Apache Hive 实现之前，Facebook 已经面临很多挑战，比如随着数据的爆炸式增长，要处理这些数据变得非常困难。
而传统的关系型数据库面对这样海量的数据可以说无能为力。
Facebook 为了克服这个难题，开始尝试使用 MapReduce。
但使用它需要具备 java 编程能力以及必须掌握 SQL，这使得该方案变得有些不切实际。
而 Apache Hive 可以很好的解决 Facebook 当前面临的问题。

Apache Hive 避免开发人员给临时需求开发复杂的 Hadoop MapReduce 作业。
因为 hive 提供了数据的摘要、分析和查询。
Hive 具有比较好的扩展性和稳定性。
由于 Hive 跟 SQL 语法上比较类似， 这对于 SQL 开发人员在学习和开发 Hive 时成本非常低，比较容易上手。
Apache Hive 最重要的特性就是不会 Java，依然可以用好 Hive。

## Hive 架构

![hiveArchitecture01.png](img/01/hiveArchitecture01.png)

由上图可知，Hadoop 和 MapReduce 是 Hive 架构的根基。

Hive 架构包括如下组件：
* CLI（command line interface）
* JDBC/ODBC
* Thrift Server
* WEB GUI
* Metastore
* Driver（Complier、Optimizer 和 Executor）

这些组件可以分为两大类：

### 服务端组件
* **Driver组件**：该组件包括Compiler、Optimizer和Executor，它的作用是将HiveQL语句进行解析、编译优化，生成执行计划，
  然后调用底层的 MapReduce 计算框架。

* **Metastore组件**：元数据服务组件，这个组件存储Hive的元数据，hive的元数据存储在关系数据库里，
  hive支持的关系数据库有derby、mysql。元数据对于hive十分重要，因此hive支持把metastore服务独立出来，
  安装到远程的服务器集群里，从而解耦hive服务和metastore服务，保证hive运行的健壮性。

* **Thrift服务**：thrift是facebook开发的一个软件框架，它用来进行可扩展且跨语言的服务的开发，hive集成了该服务，
  能让不同的编程语言调用hive的接口。

### 客户端组件

* **CLI**：command line interface，命令行接口。

* **Thrift客户端**：上面的架构图里没有写上Thrift客户端，但是hive架构的许多客户端接口是建立在 thrift客户端之上，
  包括JDBC和ODBC接口。

* **WEB GUI**：hive客户端提供了一种通过网页的方式访问hive所提供的服务。
  这个接口对应hive的hwi组件（hive web interface），使用前要启动hwi服务。


## Hive Shell
shell 是我们和 Hive 交互的主要方式，我们可以在 Hive shell 执行 HiveQL 语句以及其他命令。
Hive Shell 非常像 MySQL shell，它是 Hive 的命令行接口。HiveQL 跟 SQL 一样也是不区分大小写的。

我们可以以两种方式执行 Hive Shell：非交互式模式和交互式模式：

**非交互式模式 Hive**：Hive Shell 可以以非交互式模式执行。只要使用 -f 选项并指定包含 HQL 语句的文件的路径。
如：hive -f my-script.sql

**交互式模式 Hive**：所谓交互式模式，就是在 Hive Shell 直接执行命令，Hive 会在 shell 返回执行结果。
只要在 Linux Shell 输入 hive 即可进去交互式模式 hive shell。

![hiveShell01.png](img/01/hiveShell01.png)

## Apache Hive 的特性
Hive 有很多特性，然我们来一个个看一下：
* Hive以简单的方式提供数据摘要，数据查询和数据分析能力。
* Hive支持外部表，这使得它可以处理未存储在HDFS的数据。
* Apache Hive非常适合Hadoop的底层接口需求。
* 支持数据分区。
* Hive有一个基于规则的优化器，负责优化逻辑计划。
* 可扩展性以及可伸缩性。
* 使用HiveQL不需要深入掌握编程语言，只有掌握基本的SQL知识就行，使用门槛较低。
* 处理结构化数据。
* 使用Hive执行即时查询做数据分析。

## Apache Hive 局限性

Hive 具有以下局限性：
* Hive不支持实时查询和行级更新。
* 高延迟。
* 不适用于在线事务处理。