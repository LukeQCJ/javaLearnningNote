# Python Pandas 简介

Pandas是**基于NumPy**的一种工具，该工具是为了解决**数据分析**任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作**大型数据集**所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。
你很快就会发现，它是使Python成为强大而高效的数据分析环境的重要因素之一，本文主要介绍Python Pandas 简介。

## 1、Pandas简介
Pandas是用于**处理数据集**的Python库。

它具有**分析，清理，浏览和处理**数据的功能。

“Pandas”这个名称同时引用了"Panel Data"和“Python数据分析”，由Wes McKinney在2008年创建。

## 2、Pandas优点
Pandas使我们**能够分析大数据**并**根据统计理论做出结论**。

Pandas可以**清理混乱的数据集**，并使它们可读并具有相关性。

相关数据在数据科学中非常重要。

数据科学：是计算机科学的一个分支，我们研究如何存储，使用和分析数据以从中获取信息。

## 3、Pandas的作用
1）汇总和计算描述统计，处理缺失数据 ，层次化索引

2）数据清理、转换、合并、重塑、groupby

3）日期和时间数据类型及工具

Pandas还能够删除不相关的行或包含错误值（例如，空或NULL值）的行，这称为**清除数据**。

## 4、数据结构
**1）Series**

一维数组，与Numpy中的一维array类似。二者与Python基本的数据结构List也很相近。
Series如今能保存不同种数据类型，字符串、boolean值、数字等都能保存在Series中。

**2）Time-Series**

以时间为索引的Series。

**3）DataFrame**

二维的表格型数据结构。很多功能与R中的data.frame类似。可以将DataFrame理解为Series的容器。

**4）Panel**

三维的数组，可以理解为DataFrame的容器。

**5）Panel4D**

它是像Panel一样的4维数据容器。

**6）PanelND**

拥有factory集合，可以创建像Panel4D一样N维命名容器的模块。

## 5、Pandas的代码库
Pandas的源代码位于github repository：https://github.com/pandas-dev/pandas

github：使许多人可以在同一代码库上工作。