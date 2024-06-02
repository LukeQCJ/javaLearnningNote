# Python Pandas read_csv读取csv文件

Pandas是基于NumPy 的一种工具，该工具是为了解决数据分析任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作大型数据集所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。
你很快就会发现，它是使Python成为强大而高效的数据分析环境的重要因素之一，本文主要介绍Python Pandas read_csv读取csv文件。

## 1、读取CSV文件
存储大数据集的一种简单方法是使用CSV文件（以逗号分隔的文件）。

CSV文件包含纯文本，并且是一种众所周知的格式，所有人（包括Pandas）都可以读取。

在我们的示例中，我们将使用一个名为“data.csv”的CSV文件。

data.csv文件：https://www.cjavapy.com/download/5fe1f74edc72d93b4993067c/

例如：

将CSV加载到DataFrame中：
```text
import pandas as pd

df = pd.read_csv('data/data.csv')

print(df.to_string())
```

提示：使用to_string()打印整个DataFrame。

默认情况下，当打印DataFrame时，只会得到前5行和后5行：

例如：

输出读取的df：
```text
import pandas as pd

df = pd.read_csv('data/data.csv')

print(df)
```
output:
```text
     Duration  Pulse  Maxpulse  Calories
0          60    110       130     409.1
1          60    117       145     479.0
2          60    103       135     340.0
3          45    109       175     282.4
4          45    117       148     406.0
..        ...    ...       ...       ...
164        60    105       140     290.8
165        60    110       145     300.0
166        60    115       145     310.2
167        75    120       150     320.4
168        75    125       150     330.4

[169 rows x 4 columns]
```
