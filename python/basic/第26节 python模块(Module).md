# Python 模块(Module)

模块(module)其实就是py文件，里面定义了一些函数、类、变量等，包(package)是多个模块的聚合体形成的文件夹，
里面可以是多个py文件，也可以嵌套文件夹。
库是参考其他编程语言的说法，是指完成一定功能的代码集合，在python中的形式就是模块和包。

本文主要介绍 Python中模块(Module)。

## 1、什么是模块(Module)?
假设模块与代码库相同。

包含要包含在应用程序中的一组功能的文件。

## 2、定义模块(Module)
要创建模块，只需将所需的代码保存在文件扩展名为.py的文件中：

例如：

将此代码保存在名为my_module.py的文件中
```text
def say(name):
    print("hi cjavapy , " + name)
```

## 3、使用Module模块
现在，我们可以使用import语句来使用刚刚创建的模块：

例如：

导入名为my_module的模块，并调用greeting函数：
```text
import my_module

my_module.say("python")
```

注意：使用模块中的函数时，请使用语法：module_name.function_name。

## 4、模块中的变量
如前所述，模块可以包含函数，也可以包含所有类型的变量（数组，字典，对象等）：

例如：

将此代码保存在文件my_module.py中
```text
person1 = {
    "name": "python",
    "age": 3,
    "country": "china"
}
```

例如：

导入名为my_module的模块，并访问person1字典:
```text
import my_module

a = my_module.person1["age"]
print(a)
```
output:
```text
3
```

## 5、模块的命名
可以随意命名模块文件，但文件扩展名必须为.py

## 6、模块的别名
可以在导入模块时使用as关键字创建别名：

例如：

为my_module创建一个名为mx的别名：
```text
import my_module as mx

a = mx.person1["age"]
print(a)
```

## 7、内置模块
Python中有几个内置模块，可以直接导入使用。

例如：

导入并使用platform模块：
```text
import platform

x = platform.system()
print(x)
```

## 8、使用dir()函数查看模块中函数
内置函数可以列出模块中的所有函数名称（或变量名称）,dir()函数：

例如：

列出属于platform模块的所有已定义名称：
```text
import platform

x = dir(platform)
print(x)
```

注意：dir()函数可以在所有模块上使用，也可以在自己创建的模块上使用。

## 9、使用from和import导入指定的功能
可以选择使用from关键字从模块中仅导入部分的功能。

例如：

名为my_module的模块具有一个函数和一个字典：
```text
person1 = {
    "name": "python",
    "age": 3,
    "country": "china"
}


def say(name):
    print("hi python , " + name)
```

例如：

从模块仅导入person1字典：
```text
from my_module import person1

print(person1["age"])
```

注意：使用from关键字导入时，在引用模块中的元素时不要使用模块名称。 
```text
例如，person1["age"]，不使用my_module.person1 [“age”]
```

## 10、标准库
Python拥有丰富的标准库，提供了许多有用的功能，如文件操作、网络编程、时间日期处理等。

| 模块名称	                  | 功能描述                                    |
|------------------------|-----------------------------------------|
| math	                  | 提供数学相关函数，如三角函数、对数、平方根等。                 |
| datetime	              | 用于处理日期和时间。                              |
| os	                    | 提供了访问操作系统服务的功能，如文件、进程管理等。               |
| sys	                   | 用于访问与 Python解释器密切相关的变量和函数。              |
| re	                    | 提供正则表达式功能，用于复杂字符串处理。                    |
| json	                  | 用于读取和写入JSON数据。                          |
| http	                  | 包含创建HTTP服务的工具，如http.client和http.server。 |
| urllib	                | 用于读取来自URL的数据。                           |
| sqlite3	               | 提供了一个轻量级的数据库，可以使用SQL语法进行访问。             |
| random	                | 生成伪随机数，用于各种随机化操作。                       |
| unittest	              | 用于编写和运行测试。                              |
| logging	               | 提供了灵活的记录事件、错误、警告和调试信息等功能。               |
| argparse	              | 解析命令行参数和选项。                             |
| pathlib	               | 提供面向对象的文件系统路径操作。                        |
| collections	           | 提供额外的数据类型，如Counter、deque、OrderedDict等。  |
| subprocess	            | 用于生成新进程，连接到它们的输入/输出/错误管道，获取结果。          |
| threading	             | 用于提供线程支持。                               |
| multiprocessing	       | 支持进程间通信和并发。                             |
| xml.etree.ElementTree	 | 用于XML数据处理。                              |
| email	                 | 用于管理电子邮件消息，包括MIME和其他基于RFC 2822的消息文档。    |
