# Python Pandas DataFrame

Pandas是基于NumPy 的一种工具，该工具是为了解决数据分析任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作大型数据集所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。

你很快就会发现，它是使Python成为强大而高效的数据分析环境的重要因素之一，本文主要介绍Python Pandas DataFrame。

## 1、DataFrame 简介
Pandas DataFrame是2维数据结构，例如，2维数组或具有行和列的表。

例如：

创建一个简单的Pandas DataFrame：
```text
import pandas as pd

data = [['张三', 21, '男'], ['李四', 26, '女'], ['王五', 33, '男']]
df = pd.DataFrame(data)
print(df)
```
输出：
```text
    0   1  2
0  张三  21  男
1  李四  26  女
2  王五  33  男
```

## 2、loc定位行
从上面的结果可以看出，DataFrame就像是一个具有行和列的表。

Pandas使用loc属性返回一个或多个指定行

例如：

返回第0行：
```text
print(df.loc[0])
```
输出：
```text
0    张三
1    21
2     男
Name: 0, dtype: object
```
注意：此示例返回Pandas Series。

例如：

返回第0行和第1行：
```text
print(df.loc[[0, 1]])
```
输出：
```text
    0   1  2
0  张三  21  男
1  李四  26  女
```
注意：使用[]时，结果是熊猫DataFrame。

## 3、命名索引和列名
使用columns和index参数，可以命名自己的列名和索引。

例如：

添加名称列表，为每一行命名：
```text
import pandas as pd

data = [['张三', 21, '男'], ['李四', 26, '女'], ['王五', 33, '男']]

df = pd.DataFrame(data, columns=['姓名', '年龄', '性别'], index=['a', 'b', 'c'])

print(df)
```
输出：
```text
   姓名  年龄 性别
a  张三  21  男
b  李四  26  女
c  王五  33  男
```

## 4、定位命名索引
在loc属性中使用命名索引返回指定的行。

例如：

输出 "a":
```text
print(df.loc["a"])
```
输出：
```text
姓名    张三
年龄    21
性别     男
Name: a, dtype: object
```

## 5、将文件加载到DataFrame
如果数据集存储在文件中，Pandas可以将它们加载到DataFrame中。

例如：

将逗号分隔的文件（CSV文件）加载到DataFrame中：
```text
import pandas as pd

df = pd.read_csv('data.csv')

print(df)
```

## 相关文档：

Python pandas DataFrame 行列使用常用操作

Python pandas DataFrame 行列的常用操作及运算