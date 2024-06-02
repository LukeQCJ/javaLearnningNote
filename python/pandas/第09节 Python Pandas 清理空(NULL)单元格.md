# Python Pandas 清理空(NULL)单元格

Pandas是基于NumPy 的一种工具，该工具是为了解决数据分析任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作大型数据集所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。
你很快就会发现，它是使Python成为强大而高效的数据分析环境的重要因素之一，本文主要介绍Python Pandas 清理空(NULL)单元格。

## 1、空单元格
分析数据时，空单元格可能会给带来错误的结果。

## 2、删除行
处理空单元格的一种方法是删除包含空单元格的行。

这通常是可以的，因为数据集可能非常大，并且删除几行不会对结果产生很大的影响。

例如：

返回没有空单元格的新数据框：
```text
import pandas as pd

df = pd.read_csv('data/data.csv')

new_df = df.dropna()

print(new_df.to_string())
```

在我们的清理示例中，我们将使用一个名为“dirtydata.csv”的CSV文件。

dirtydata.csv文件：https://www.cjavapy.com/download/5fe1f9d0dc72d93b4993067e/

注意：默认情况下，**dropna()方法**返回一个new数据框，并且不会更改原始DataFrame。

**如果你想改变原始的DataFrame，使用inplace = True参数**:

例如：

删除所有带有NULL值的行：
```text
import pandas as pd

df = pd.read_csv('data.csv')

df.dropna(inplace = True)

print(df.to_string())
```

Note:

dropna(inplace = True)不会返回一个新的DataFrame，但是它会从原始DataFramee中删除包含NULL值的所有行。

## 3、替换空值
处理空单元格的另一种方法是改为插入新值。

这样，不必仅由于某些空单元格而删除整个行。

**fillna()方法**允许我们用一个值替换空单元格：

例如：

将NULL值替换为数字130：
```text
import pandas as pd

df = pd.read_csv('data.csv')

df.fillna(130, inplace = True)
```

仅替换指定的列

上面的示例替换了整个数据框中的所有空白单元格。

要仅替换一列的空值，请为DataFrame指定列名：

例如：

将"Calories"列中的NULL值替换为数字130:
```text
import pandas as pd

df = pd.read_csv('data.csv')

df["Calories"].fillna(130, inplace = True)
```

## 4、Replace 使用Mean, Median, 或 Mode
替换空单元格的一种常用方法是计算列的平均值，中位数或众数。

Pandas使用mean()、median（）和mode（）方法来计算指定列的相应值：

例如：

计算平均值，并用其替换任何空值：
```text
import pandas as pd

df = pd.read_csv('data.csv')

x = df["Calories"].mean()

df["Calories"].fillna(x, inplace = True)
```

例如：

计算MEDIAN，并用它替换任何空值：
```text
import pandas as pd

df = pd.read_csv('data.csv')

x = df["Calories"].median()

df["Calories"].fillna(x, inplace = True)
```

例如：

计算MODE，并用它替换任何空值：
```text
import pandas as pd

df = pd.read_csv('data.csv')

x = df["Calories"].mode()[0]

df["Calories"].fillna(x, inplace = True)
```
