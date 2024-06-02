# Python pandas DataFrame 行列的常用操作及运算

Pandas是基于NumPy 的一种工具，该工具是为了解决数据分析任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作大型数据集所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。
本文主要介绍Python pandas 中DataFrame 行列的常用操作和运算，以及相关的示例代码。

## 1、DataFrame的常用操作
1）查看DataFrame的大小
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])
print(df.shape)
```
output:
```text
(3, 4)
```

2）查看DataFrame的列名(columns)
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])
print(df.columns)
```
output:
```text
Index(['a', 'b', 'c', 'd'], dtype='object')
```

3）查看DataFrame的行名(index)
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])
print(df.index)
```
output:
```text
RangeIndex(start=0, stop=3, step=1)
```

4）查看每行的数据类型
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])
print(df.dtypes)
```
output:
```text
a    int64
b    int64
c    int64
d    int64
dtype: object
```

5）查看行列的内容
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])
print(df)
print(df.iloc[-1])  # 选取DataFrame最后一行，返回的是Series
print(df.iloc[-1:])  # 选取DataFrame最后一行，返回的是DataFrame
print(df.loc[0:1, 'b':'c'])  # 这种用于选取行索引列索引已知
print(df.iat[1, 1])  # 选取第二行第二列，用于已知行、列位置的选取。
print(df.head(2))  # 查看前两行的内容
print(df.tail(2))  # 查看后两行的内容
print(df.loc[[df.index[0], df.index[1]]])  # 通过label查看
print(df.loc[[df.index[-2], df.index[-1]]])
```

6）查看每一列数据的整体特征
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])
print(df.describe())
```
output:
```text
               a         b          c          d
count   3.000000  3.000000   3.000000   3.000000
mean    5.333333  7.666667   9.666667   9.333333
std     4.509250  1.527525   2.516611   4.163332
min     1.000000  6.000000   7.000000   6.000000
25%     3.000000  7.000000   8.500000   7.000000
50%     5.000000  8.000000  10.000000   8.000000
75%     7.500000  8.500000  11.000000  11.000000
max    10.000000  9.000000  12.000000  14.000000
```

7）对DataFrame进行倒置
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])
print(df.T)
```
output:
```text
    0   1   2
a  10   1   5
b   6   9   8
c   7  12  10
d   8  14   6
```

## 2、DataFrame的行列运算
1）DataFrame中两列数据相加减
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])

df['d - a'] = df['d'] - df['a']
df['d + a'] = df['d'] + df['a']

print(df)
```
output:
```text
    a  b   c   d  d - a  d + a
0  10  6   7   8     -2     18
1   1  9  12  14     13     15
2   5  8  10   6      1     11
```

2）DataFrame中两行数据相加减
```text
import pandas as pd

df = pd.DataFrame([[10, 6, 7, 8],
                   [1, 9, 12, 14],
                   [5, 8, 10, 6]],
                  columns=['a', 'b', 'c', 'd'])

print(df[df.a == 10].values - df[df.a == 1].values)
print(df[df.a == 10].values + df[df.a == 1].values)
```
output:
```text
[[ 9 -3 -5 -6]]
[[11 15 19 22]]
```

## 相关文档：

Python pandas.DataFrame.iloc函数方法的使用

Python pandas.DataFrame.loc函数方法的使用

Python pandas dataframe iloc 和 loc 的用法及区别

Python pandas.DataFrame.at函数方法的使用

Python pandas.DataFrame.iat函数方法的使用

Python pandas.DataFrame.drop函数方法的使用

Python pandas.DataFrame.where函数方法的使用