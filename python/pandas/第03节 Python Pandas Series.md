# Python Pandas Series

Pandas是基于NumPy 的一种工具，该工具是为了解决数据分析任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作大型数据集所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。
你很快就会发现，它是使Python成为强大而高效的数据分析环境的重要因素之一，本文主要介绍Python Pandas Series。

## 1、Series简介
Pandas Series就像表格中的一列。它是一维数组，可保存任何类型的数据。
Series对象本质上由两个数组构成，一个数组构成对象的健(index,索引)，一个数组构成对象的值(values)。

Series是带标签的一维数组，可存储整数、浮点数、字符串、Python对象等类型的数据。
**轴标签**统称为**索引**，它由两部分组成。

- index：数据索引标签
- values：一组数据(ndarray类型)

例如：

从列表中创建一个简单的Pandas Series：
```text
import pandas as pd

a = [1, 7, 2]

myvar = pd.Series(a)

print(myvar)
```
output:
```text
0    1
1    7
2    2
dtype: int64
```

## 2、标签(Labels)
如果未指定其他任何内容，则这些值将用其索引号标记。第一个值的索引为0，第二个值的索引为1，依此类推。

该标签可用于访问指定值。

例如：

返回Series的第一个值：
```text
print(myvar[0])  # 1
```

## 3、创建标签(Labels)
使用index参数，可以命名自己的标签。

例如：

创建自己的标签：
```text
import pandas as pd

a = [1, 7, 2]

myvar = pd.Series(a, index = ["x", "y", "z"])

print(myvar)
```
output:
```text
x    1
y    7
z    2
dtype: int64
```

创建标签后，可以通过参考标签来访问项目。

例如：

返回值“y”：
```text
print(myvar["y"])  # 7
```

## 4、Key和Value对象作为Series
创建Series时，还可以使用键/值对象，例如，dict。

例如：

从字典创建一个简单的Pandas Series：
```text
import pandas as pd

calories = {"day1": 420, "day2": 380, "day3": 390}

myvar = pd.Series(calories)

print(myvar)
```
output:
```text
day1    420
day2    380
day3    390
dtype: int64
```

注意：字典的键将成为标签。

要仅选择词典中的某些项目，请使用index参数，并仅指定要包括在Series中的项目。

例如：

仅使用“day1”和“day2”中的数据创建Series：
```text
import pandas as pd

calories = {"day1": 420, "day2": 380, "day3": 390}

myvar = pd.Series(calories, index=["day1", "day2"])

print(myvar)
```
output:
```text
day1    420
day2    380
dtype: int64
```

## 5、Series的索引和切片
**1）位置下标**

当使用默认值索引时，通常使用位置下标。类似列表的索引使用方式。
```text
import pandas as pd
import numpy as np

data = np.random.randint(0,100,size=(6,))
series = pd.Series(data=data)
print(series)
print("_________")
print(series[0])
print(series[3])
print(series[4])
```
output:
```text
0     5
1    75
2    33
3    95
4    46
5    89
dtype: int32
_________
5
95
46
```

**2）标签索引**

用在显示索引时，通过key获取value的方式获取。
```text
import pandas as pd
import numpy as np

data = np.random.randint(0, 100, size=(6,))
series = pd.Series(data=data, index=list('abcdef'))
print(series)
print("___________")
print(series['a'])
print(series['f'])
print(series['c'])
```
output:
```text
a    19
b    58
c    65
d    37
e    36
f    48
dtype: int32
___________
19
48
65
```

**3）布尔型索引**

通过布尔型的数组获取Series对象中的值。
```text
import pandas as pd
import numpy as np

data = np.random.randint(0, 100, size=(6,))
series = pd.Series(data=data, index=list('abcdef'))
print(series)
print("________")
series2 = series > 50  # 获取series中大于50的元素，series2是一个bool类型的数组
print(series2)
print("________")
print(series[series2])  # 通过series2这个bool数组获取series中的元素
```
output:
```text
a    40
b    98
c    58
d    20
e    56
f    59
dtype: int32
________
a    False
b     True
c     True
d    False
e     True
f     True
dtype: bool
________
b    98
c    58
e    56
f    59
dtype: int32
```

**4）切片索引**
```text
import pandas as pd
import numpy as np

data = np.random.randint(0, 100, size=(6,))
series = pd.Series(data=data)

print(series)
print("___________")
# 隐式索引：通过整数
print(series[1:5])
print(series[:4])
print(series[2:])
print(series.iloc[2])  # 指定下标
print(series.iloc[1:3])  # 指定切片范围

data = np.random.randint(0, 100, size=(6,))
series = pd.Series(data=data, index=list('abcdef'))
print(series)
print("___________")
# 显示索引：通过字符标签
print(series['a':'d'])
print(series['a':])
print(series[:'f'])
print(series[::2])
print(series.loc['a':'e'])
print(series.loc[['a', 'c', 'f']])
```

## 6、Series的常用函数
**1）series.head(n)**

显示从前边计算的数据行，可以指定显示的行数，不写n默认是前5行。

**2）series.tail(n)**

显示从后边计算的数据行，可以指定显示的行数，不写n默认是后5行 。

**3）series.unique()**

去除重复的值

**4）series.notnull()**

判断是否有空值，不为空返回True，为空返回False。

**5）series.isnull()**

判断是否有空值，不为空返回False，为空返回True。

```text
import pandas as pd
import numpy as np

lst = [11, 13, 20, 16, 12, 8]
series = pd.Series(lst, index=["A", "B", "C", "D", "E", "F"])
print(series.head())  # 获取前5行
print(series.tail())  # 获取后5行
print(series.unique())  # 去除重复的值
series['B'] = None  # 修改D索引对应的值为None
print(series.isnull())  # 判断是否有空值
print(series.notnull())  # 判断是否有非空值
```

## 7、DataFrames
Pandas中的数据集通常是多维表，称为DataFrames。

Series就像**一列**，DataFrame是**整个表**。

例如：

从两个Series创建一个DataFrame：
```text
import pandas as pd

data = {
    "calories": [420, 380, 390],
    "duration": [50, 40, 45]
}

myvar = pd.DataFrame(data)

print(myvar)
```
output:
```text
   calories  duration
0       420        50
1       380        40
2       390        45
```

## 相关文档：

Python pandas DataFrame 行列使用常用操作

Python pandas DataFrame 行列的常用操作及运算