# Python Pandas 入门教程

Pandas是基于NumPy 的一种工具，该工具是为了解决数据分析任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作大型数据集所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。

你很快就会发现，它是使Python成为强大而高效的数据分析环境的重要因素之一，本文主要介绍Python Pandas 入门教程。

## 1、安装Pandas
如果您已经在系统上安装了Python和PIP，则Pandas的安装非常简单。

使用以下命令安装它：
```text
pip install pandas
```

如果此命令失败，则使用已经安装了Pandas的python发行版，例如Anaconda，Spyder等。

## 2、Import Pandas
安装Pandas后，通过添加import关键字将其导入您的应用程序：
```text
import pandas
```

Pandas导入后，可以使用了。

例如：
```text
import pandas

my_dataset = {
    'langs': ["C", "Python", "Java"],
    'count': [3, 7, 2]
}

my_var = pandas.DataFrame(my_dataset)

print(my_var)
```
output:
```text
    langs  count
0       C      3
1  Python      7
2    Java      2
```

## 3、Pandas as pd
Pandas通常以pd别名导入。

alias：在Python中，别名是用于指代同一事物的替代名称。

导入时，使用as关键字创建别名：
```text
import pandas as pd
```

执行后，Pandas软件包可以称为pd而不是pandas。

例如：
```text
import pandas as pd

my_dataset = {
    'langs': ["C", "Python", "Java"],
    'count': [3, 7, 2]
}

my_var = pd.DataFrame(my_dataset)

print(my_var)
```
output:
```text
    langs  count
0       C      3
1  Python      7
2    Java      2
```

## 4、判断Pandas版本
Pandas版本字符串存储在__version__属性下。

例如：
```text
import pandas as pd

print(pd.__version__)
```

## 相关文档：

Python pandas DataFrame 行列使用常用操作

Python pandas DataFrame 行列的常用操作及运算