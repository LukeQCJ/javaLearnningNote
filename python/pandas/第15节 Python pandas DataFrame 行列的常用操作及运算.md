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

---

## 补充DataFrame运算

### 1. DataFrame之间的运算

- 在运算中自动对齐不同索引的数据
- 如果索引不对应，则补NaN
- DataFrame没有广播机制

创建 DataFrame df1 不同人员的各科目成绩，月考一。
```text
import numpy as np
import pandas as pd

# 创建DataFrame二维数组
df1 = pd.DataFrame(
    data=np.random.randint(10, 100, size=(3, 3)),
    index=["小明", "小红", "小黄"],
    columns=["语文", "数学", "英语"]
)
print("月考一：")
print(df1)
```

#### DataFrame和【标量】之间的运算：

```text
import numpy as np
import pandas as pd

# 创建DataFrame二维数组
df1 = pd.DataFrame(
    data=np.random.randint(10, 100, size=(3, 3)),
    index=["小明", "小红", "小黄"],
    columns=["语文", "数学", "英语"]
)
print("月考一：")
print(df1)

print("====dataFrame与标量运算：====")
print("加法：+ 100")
print(df1 + 100)

print("减法：- 100")
print(df1 - 100)

print("乘法：* 100")
print(df1 * 100)

print("除法：/ 100")
print(df1 / 100)

print("取余：% 10")
print(df1 + 10)

print("幂运算(平方)：** 2")
t_df = df1 ** 2
print(t_df)

print("开方运算(平方根)：sqrt")
print(np.sqrt(t_df))
```
output:
```text
月考一：
    语文  数学  英语
小明  26  96  50
小红  36  44  30
小黄  49  39  50
====dataFrame与标量运算：====
加法：+ 100
     语文   数学   英语
小明  126  196  150
小红  136  144  130
小黄  149  139  150
减法：- 100
    语文  数学  英语
小明 -74  -4 -50
小红 -64 -56 -70
小黄 -51 -61 -50
乘法：* 100
      语文    数学    英语
小明  2600  9600  5000
小红  3600  4400  3000
小黄  4900  3900  5000
除法：/ 100
      语文    数学   英语
小明  0.26  0.96  0.5
小红  0.36  0.44  0.3
小黄  0.49  0.39  0.5
取余：% 10
    语文   数学  英语
小明  36  106  60
小红  46   54  40
小黄  59   49  60
幂运算(平方)：** 2
      语文    数学    英语
小明   676  9216  2500
小红  1296  1936   900
小黄  2401  1521  2500
开方运算(平方根)：sqrt
      语文    数学    英语
小明  26.0  96.0  50.0
小红  36.0  44.0  30.0
小黄  49.0  39.0  50.0
```
#### DataFrame之间的运算

再创建 DataFrame df2 不同人员的各科目成绩，月考二。
```text
import numpy as np
import pandas as pd

# 创建DataFrame二维数组
df1 = pd.DataFrame(
    data=np.random.randint(10, 100, size=(3, 3)),
    index=["小明", "小红", "小黄"],
    columns=["语文", "数学", "英语"]
)
print("月考一：")
print(df1)

df2 = pd.DataFrame(
    data=np.random.randint(10, 100, size=(3, 3)),
    index=["小明", "小红", "小黄"],
    columns=["语文", "数学", "英语"]
)
print("月考二：")
print(df2)
```
DataFrame提供的算数运算方法：
```text
import numpy as np
import pandas as pd

# 创建DataFrame二维数组
df1 = pd.DataFrame(
    data=np.random.randint(10, 100, size=(3, 3)),
    index=["小明", "小红", "小黄"],
    columns=["语文", "数学", "英语"]
)
print("月考一：")
print(df1)

df2 = pd.DataFrame(
    data=np.random.randint(10, 100, size=(3, 3)),
    index=["小明", "小红", "小黄"],
    columns=["语文", "数学", "英语"]
)
print("月考二：")
print(df2)

df3 = pd.DataFrame(
    data=np.random.randint(10, 100, size=(4, 4)),
    index=["小明", "小红", "小黄", "小绿"],
    columns=["语文", "数学", "英语", "物理"]
)
print("月考三：")
print(df3)
print("====dataFrame之间的运算：====")
print("====加法：+ ")
print("行列相同：直接相加")
print(df1 + df2)
print("行列不同：不同的行列上的值为NAN")
print(df1 + df3)
print("====加法：add方法，不设置fill_value属性同 + 符号")
print(df1.add(df3))
print("====加法：add方法，设置fill_value=0")
print(df1.add(df3, fill_value=0))
print("还有其他算数运算的方法，sub、divide等，具体用到的时候查资料")
```
output:
```text
月考一：
    语文  数学  英语
小明  71  82  19
小红  35  47  62
小黄  52  91  80
月考二：
    语文  数学  英语
小明  21  94  31
小红  76  77  45
小黄  30  76  22
月考三：
    语文  数学  英语  物理
小明  46  36  47  79
小红  68  55  38  75
小黄  24  72  38  28
小绿  15  71  79  34
====dataFrame之间的运算：====
====加法：+ 
行列相同：直接相加
     语文   数学   英语
小明   92  176   50
小红  111  124  107
小黄   82  167  102
行列不同：不同的行列上的值为NAN
       数学  物理     英语     语文
小明  118.0 NaN   66.0  117.0
小红  102.0 NaN  100.0  103.0
小绿    NaN NaN    NaN    NaN
小黄  163.0 NaN  118.0   76.0
====加法：add方法，不设置fill_value属性同 + 符号
       数学  物理     英语     语文
小明  118.0 NaN   66.0  117.0
小红  102.0 NaN  100.0  103.0
小绿    NaN NaN    NaN    NaN
小黄  163.0 NaN  118.0   76.0
====加法：add方法，设置fill_value=0
       数学    物理     英语     语文
小明  118.0  79.0   66.0  117.0
小红  102.0  75.0  100.0  103.0
小绿   71.0  34.0   79.0   15.0
小黄  163.0  28.0  118.0   76.0
还有其他算数运算的方法，sub、divide等，具体用到的时候查资料
```

### 2. Series与DataFrame之间的运算

- 使用Python操作符：以行为单位操作（参数必须是行），对所有行都有效
- 类似于NumPy中二维数组与一维数组的运算，但可能出现NaN
- 使用Pandas操作函数：
  - axis=0：以列为单位操作（参数必须是列），对所有列都有效；
  - axis=1：以行为单位操作（参数必须是行），对所有行都有效

```text
import numpy as np
import pandas as pd

# 创建DataFrame二维数组
df1 = pd.DataFrame(
    data=np.random.randint(10, 100, size=(3, 3)),
    index=["小明", "小红", "小黄"],
    columns=["语文", "数学", "英语"]
)
print("月考一：")
print(df1)

s = pd.Series([100, 10, 1], index=df1.columns)
print("====DataFrame与Series运算====")
print("s：")
print(s)
print("df1 + s：")
print(df1 + s)
print("df1.add(s)：")
print(df1.add(s))
print("axis:(0 or index,1 or columns)，默认为列")
df1.add(s, axis="columns")
df1.add(s, axis=1)

s = pd.Series([100, 10, 1], index=df1.index)
print("s：")
print(s)
# 行
print(df1.add(s, axis=0))
print(df1.add(s, axis="index"))
```
output:
```text
月考一：
    语文  数学  英语
小明  55  20  51
小红  58  62  67
小黄  37  54  95
====DataFrame与Series运算====
s：
语文    100
数学     10
英语      1
dtype: int64
df1 + s：
     语文  数学  英语
小明  155  30  52
小红  158  72  68
小黄  137  64  96
df1.add(s)：
     语文  数学  英语
小明  155  30  52
小红  158  72  68
小黄  137  64  96
axis:(0 or index,1 or columns)，默认为列
s：
小明    100
小红     10
小黄      1
dtype: int64
     语文   数学   英语
小明  155  120  151
小红   68   72   77
小黄   38   55   96
     语文   数学   英语
小明  155  120  151
小红   68   72   77
小黄   38   55   96
```

--- 
## DataFrame的逻辑运算
### 1、逻辑运算符号<、>、|、&

### 2、逻辑运算函数
1）query(expr)
```text
说明：
expr：查询字符串
```

2）isin(values)
```text
说明：
指定值进行判断
```

例子：
```text
import numpy as np
import pandas as pd

# 创建DataFrame二维数组
df1 = pd.DataFrame(
    data=np.random.randint(40, 100, size=(3, 3)),
    index=["小明", "小红", "小黄"],
    columns=["语文", "数学", "英语"]
)
print("月考一：")
print(df1)

print("====DataFrame逻辑符运算====")
print("df1['语文'] > 60：")
print(df1["语文"] > 60)
print("df1[df1['语文'] > 60]：")
print(df1[df1["语文"] > 60])
print("====完成多个逻辑判断：")
print(df1[(df1["语文"] > 60) & (df1["数学"] > 60) & (df1["英语"] > 60)])
print("====DataFrame逻辑运算函数====")
print('df1.query("语文 > 60")：')
print(df1.query("语文 > 60"))
lst = range(90, 100)
print('筛选出“语文”成绩在90~100范围之内的数据：')
print(df1[df1["语文"].isin(lst)])
```
output:
```text
月考一：
    语文  数学  英语
小明  97  96  50
小红  71  93  55
小黄  87  43  48
====DataFrame逻辑符运算====
df1['语文'] > 60：
小明    True
小红    True
小黄    True
Name: 语文, dtype: bool
df1[df1['语文'] > 60]：
    语文  数学  英语
小明  97  96  50
小红  71  93  55
小黄  87  43  48
====完成多个逻辑判断：
Empty DataFrame
Columns: [语文, 数学, 英语]
Index: []
====DataFrame逻辑运算函数====
df1.query("语文 > 60")：
    语文  数学  英语
小明  97  96  50
小红  71  93  55
小黄  87  43  48
筛选出“语文”成绩在90~100范围之内的数据：
    语文  数学  英语
小明  97  96  50
```

---
## DataFrame统计运算

### 1、describe()
```text
计算平均值、标准差、最大值、最小值。。。
count：有多少个数量
mean：平均值
std：标准差
min：最小值
max：最大值
25%、50%、75%：分位数
```

### 2、统计函数
```text
和numpy中的统计函数类似
sum()：求和
mean()：求平均值
median()：中位数
min()：最小值
max()：最大值
mode()：求众数，就是数据集中出现次数最多的数值
abs()：求绝对值
prod()：计算数据集中所有元素的积
std()：求标准差
var()：求方差
idxmax()：沿列轴查找最大值的索引
idxmin()：沿列轴查找最小值的索引
```

### 3、累计统计函数
```text
cumsum：计算前1/2/3/.../n个数的和
cummax：计算前1/2/3/.../n个数的最大值
cummin：计算前1/2/3/.../n个数的最小值
cumprod：计算前1/2/3/.../n个数的积
```
