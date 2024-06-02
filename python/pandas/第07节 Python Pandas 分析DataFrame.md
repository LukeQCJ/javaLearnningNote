# Python Pandas 分析DataFrame

Pandas是基于NumPy 的一种工具，该工具是为了解决数据分析任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作大型数据集所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。

你很快就会发现，它是使Python成为强大而高效的数据分析环境的重要因素之一，本文主要介绍Python Pandas 分析DataFrame。

## 1、查看数据
head()方法是获取DataFrame概述的最常用方法之一。

head()方法从顶部开始返回标题和指定的行数。

例如：

通过打印DataFrame的前10行获得快速查看：
```text
import pandas as pd

df = pd.read_csv('data/data.csv')

print(df.head(10))
```

在我们的示例中，我们将使用一个名为“data.csv”的CSV文件。

data.csv文件：https://www.cjavapy.com/download/5fe1f74edc72d93b4993067c/

注意：如果未指定行数，则head()方法将返回前5行。

例如：

打印DataFrame的前5行：
```text
import pandas as pd

df = pd.read_csv('data/data.csv')

print(df.head())
```

还有一个tail()方法，用于查看DataFrame的last行。

tail()方法从底部开始返回标题和指定的行数。

例如：

打印DataFrame的最后5行：
```text
print(df.tail())
```

## 2、DataFrame数据信息
DataFrame对象具有称为info()的方法，该方法为您提供有关数据集的更多信息。

例如：

打印有关数据的信息：
```text
print(df.info())
```
output:
```text
<class 'pandas.core.frame.DataFrame'>
RangeIndex: 169 entries, 0 to 168
Data columns (total 4 columns):
 #   Column    Non-Null Count  Dtype  
---  ------    --------------  -----  
 0   Duration  169 non-null    int64  
 1   Pulse     169 non-null    int64  
 2   Maxpulse  169 non-null    int64  
 3   Calories  164 non-null    float64
dtypes: float64(1), int64(3)
memory usage: 5.4 KB
None
```

## 3、结果解释
结果告诉我们，共有169行和4列：
```text
RangeIndex: 169 entries, 0 to 168
Data columns (total 4 columns):
```

以及每列的名称，其数据类型为：
```text
#   Column    Non-Null Count  Dtype
  ---  ------    --------------  -----  
0   Duration  169 non-null    int64  
1   Pulse     169 non-null    int64  
2   Maxpulse  169 non-null    int64  
3   Calories  164 non-null    float64
```

## 4、Null 值
info()方法还告诉我们每个列中有多少非空值。
在分析数据时，空值或Null值可能很麻烦，应该考虑删除具有空值的行。
这是所谓的清理数据的操作，将在下一个文档中了解更多。

## 相关文档：

Python pandas DataFrame 行列使用常用操作

Python pandas DataFrame 行列的常用操作及运算