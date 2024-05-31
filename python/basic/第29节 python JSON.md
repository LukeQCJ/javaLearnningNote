# Python JSON

JSON是用于存储和交换数据的语法。
JSON是使用JavaScript对象表示法编写的文本。
本文主要介绍 Python中JSON使用，及使用示例代码。

## 1、 Python中JSON

Python有一个json的内置程序包，可用于处理JSON数据。

例如：

导入json模块：
```text
import json
```

## 2、解析JSON—从JSON转换为Python对象
如果有JSON字符串，则可以使用json.loads()方法进行解析。

结果将是一个 Python字典。

例如：

从JSON转换为 Python：
```text
import json

x = '{ "name":"python", "age":3, "city":"china"}'

# 解析 x:
y = json.loads(x)

print(y["name"])
```
output:
```text
python
```

## 3、从Python对象转换成JSON
如果有Python对象，则可以使用json.dumps()方法将其转换为JSON字符串。

例如：

从Python对象转换为JSON：
```text
import json

# a Python object (dict):
x = {"name": "cjavapy", "age": 3, "city": "china"}

# convert into JSON:
print(type(x))
y = json.dumps(x)

# 结果是 JSON string:
print(y)
print(type(y))
```
output:
```text
<class 'dict'>
{"name": "cjavapy", "age": 3, "city": "china"}
<class 'str'>
```

下列类型可以转换Python对象成JSON字符串:
- dict
- list
- tuple
- string
- int
- float
- True
- False
- None

例如：

将Python对象转换为JSON字符串，并输出值：
```text
import json

print(json.dumps({"name": "cjavapy", "age": 3}))
print(json.dumps(["python", "cjavapy"]))
print(json.dumps(("python", "cjavapy")))
print(json.dumps("hello"))
print(json.dumps(42))
print(json.dumps(31.76))
print(json.dumps(True))
print(json.dumps(False))
print(json.dumps(None))
```
output:
```text
{"name": "cjavapy", "age": 3}
["python", "cjavapy"]
["python", "cjavapy"]
"hello"
42
31.76
true
false
null
```

从Python对象转换为JSON时，Python对象将转换为等效的JSON(JavaScript)：

| Python | JSON   |
|--------|--------|
| dict   | Object |
| list   | Array  |
| tuple  | Array  |
| str    | String |
| int    | Number |
| float  | Number |
| True   | true   |
| False  | false  |
| None   | null   |

例如：

转换包含所有合法数据类型的Python对象：
```text
import json

x = {
    "name": "python",
    "age": 20,
    "married": True,
    "divorced": False,
    "children": ("c", "python"),
    "pets": None,
    "langs": [
        {"model": "java", "year": 5},
        {"model": "python", "year": 6}
    ]
}

print(json.dumps(x))
```
output:
```text
{"name": "python", "age": 20, "married": true, "divorced": false, "children": ["c", "python"], "pets": null, "langs": [{"model": "java", "year": 5}, {"model": "python", "year": 6}]}
```

## 4、格式化json.dumps()生成的JSON字符串
上面的示例显示了一个JSON字符串，但是它不是很容易阅读，没有缩进和换行符。

json.dumps()方法有一些参数，以便更容易地读取结果:

例如：

使用indent参数定义缩进数量：
```text
json.dumps(x, indent=4)
```
output:
```text
{
    "name": "python",
    "age": 20,
    "married": true,
    "divorced": false,
    "children": [
        "c",
        "python"
    ],
    "pets": null,
    "langs": [
        {
            "model": "java",
            "year": 5
        },
        {
            "model": "python",
            "year": 6
        }
    ]
}
```

还可以定义分隔符，默认值为(", ", ": ")，这意味着使用逗号和空格分隔每个对象，并使用冒号和空格分隔键和值：

例如：

使用separators参数更改默认的分隔符：
```text
json.dumps(x, indent=4, separators=(". ", " = "))
```
output:
```text
{
    "name" = "python". 
    "age" = 20. 
    "married" = true. 
    "divorced" = false. 
    "children" = [
        "c". 
        "python"
    ]. 
    "pets" = null. 
    "langs" = [
        {
            "model" = "java". 
            "year" = 5
        }. 
        {
            "model" = "python". 
            "year" = 6
        }
    ]
}
```

## 5、对json.dumps()结果排序
json.dumps()方法有对结果中的键进行排序的参数：

例如：

使用sort_keys参数指定是否对结果进行排序：
```text
json.dumps(x, indent=4, sort_keys=True)
```
output:
```text
{
    "age": 20,
    "children": [
        "c",
        "python"
    ],
    "divorced": false,
    "langs": [
        {
            "model": "java",
            "year": 5
        },
        {
            "model": "python",
            "year": 6
        }
    ],
    "married": true,
    "name": "python",
    "pets": null
}
```

## 6、使用第三方库：Demjson
Demjson 是 python 的第三方模块库，可用于编码和解码 JSON 数据，包含了 JSONLint 的格式化及校验功能。

Github 地址：https://github.com/dmeranda/demjson

官方地址：http://deron.meranda.us/python/demjson/

### 1）环境配置
在使用 Demjson 编码或解码 JSON 数据前，我们需要先安装 Demjson 模块。本教程我们会下载 Demjson 并安装：
```text
tar -xvzf demjson-2.2.3.tar.gz
cd demjson-2.2.3
python setup.py install
```

更多安装介绍文档：http://deron.meranda.us/python/demjson/install

### 2）JSON 函数
| 函数	     | 描述                          |
|---------|-----------------------------|
| encode	 | 将 Python 对象编码成 JSON 字符串     |
| decode	 | 将已编码的 JSON 字符串解码为 Python 对象 |

### 3）encode使用
Python encode() 函数用于将 Python 对象编码成 JSON 字符串。

语法
```text
demjson.encode(self, obj, nest_level=0)
```
以下实例将数组编码为 JSON 格式数据：
```text
#!/usr/bin/python
import demjson
data = [ { 'a' : 1, 'b' : 2, 'c' : 3, 'd' : 4, 'e' : 5 } ]
json = demjson.encode(data)
print(json)
```
以上代码执行结果为：
```text
[{"a":1,"b":2,"c":3,"d":4,"e":5}]
```

### 4）decode使用
Python 可以使用 demjson.decode()函数解码 JSON 数据。该函数返回  Python 字段的数据类型。

语法
```text
demjson.decode(self, txt)
```
以下实例展示了Python 如何解码 JSON 对象：
```text
#!/usr/bin/python

import demjson
json = '{"a":1,"b":2,"c":3,"d":4,"e":5}';
text = demjson.decode(json)

print(text)
```

以上代码执行结果为：
```text
{u'a':1, u'c':3, u'b':2, u'e':5, u'd':4}
```
