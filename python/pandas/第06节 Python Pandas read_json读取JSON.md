# Python Pandas read_json读取JSON

Pandas是基于NumPy 的一种工具，该工具是为了解决数据分析任务而创建的。
Pandas 纳入了大量库和一些标准的数据模型，提供了高效地操作大型数据集所需的工具。
Pandas提供了大量能使我们快速便捷地处理数据的函数和方法。

你很快就会发现，它是使Python成为强大而高效的数据分析环境的重要因素之一，本文主要介绍Python Pandas read_json读取JSON。

## 1、读取JSON
大数据集通常被存储或提取为JSON。

JSON是纯文本，但是具有对象的格式，并且在包括Pandas在内的编程行业中众所周知。

在我们的示例中，我们将使用一个名为“data.json”的JSON文件。

data.json文件：https://www.cjavapy.com/download/5fe1f8c9dc72d93b4993067d/

例如：

将JSON文件加载到DataFrame中：
```text
import pandas as pd

df = pd.read_json('data/data.json')

print(df.to_string())
```

提示：使用to_string（）打印整个DataFrame。

## 2、 JSON格式的Dictionary
JSON对象与Python字典具有相同的格式。

如果JSON代码不在文件中，而是在Python字典中，则可以将其直接加载到DataFrame中：

例如：

将Python字典加载到DataFrame中：
```text
import pandas as pd

data = {
    "Duration": {
        "0": 60,
        "1": 60,
        "2": 60,
        "3": 45,
        "4": 45,
        "5": 60
    },
    "Pulse": {
        "0": 110,
        "1": 117,
        "2": 103,
        "3": 109,
        "4": 117,
        "5": 102
    },
    "Maxpulse": {
        "0": 130,
        "1": 145,
        "2": 135,
        "3": 175,
        "4": 148,
        "5": 127
    },
    "Calories": {
        "0": 409,
        "1": 479,
        "2": 340,
        "3": 282,
        "4": 406,
        "5": 300
    }
}

df = pd.DataFrame(data)

print(df)
```
output:
```text
   Duration  Pulse  Maxpulse  Calories
0        60    110       130       409
1        60    117       145       479
2        60    103       135       340
3        45    109       175       282
4        45    117       148       406
5        60    102       127       300
```
