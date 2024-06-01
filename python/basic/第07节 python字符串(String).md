# Python 字符串(String)的使用

本文主要介绍 Python中字符串(String)的基础使用操作和相关方法使用（为变量分配字符串、多行(Multiline)字符串、
字符串(Strings)是数组、字符串切片、负索引、字符串长度、字符串方法、判断字符串(in和not in)、字符串连接、
字符串格式化、转义字符），以及相关示例代码。

## 1、字符串声明及输出
python中的字符串文字被单引号(')或双引号(")引起来。

'hello'与"hello"相同。

可以使用print（）函数显示字符串：

例如：
```text
print("Hello")
print('Hello')
```

## 2、 Python中为变量分配字符串
使用变量名，后面用等号和字符串的方式为变量分配字符串：

例如：
```text
a = "Hello"
print(a)
```

## 3、多行(Multiline)字符串
多行字符串允许一个字符串跨多行，字符串中可以包含换行符、制表符以及其他特殊字符。
可以使用三个引号将多行字符串分配给变量：

例如：

您可以使用三个双引号：
```text
a = """Lorem ipsum dolor sit amet,
consectetur adipiscing elit,
sed do eiusmod tempor incididunt
ut labore et dolore magna aliqua."""

print(a)
```

或三个单引号：

例如：
```text
a = '''Lorem ipsum dolor sit amet,
consectetur adipiscing elit,
sed do eiusmod tempor incididunt
ut labore et dolore magna aliqua.'''

print(a)
```

注意：结果中， 换行符被插入到与代码相同的位置。

## 4、字符串(Strings)是数组
像许多其他流行的编程语言一样， Python中的字符串是表示unicode字符的字节数组。

但是， Python没有字符数据类型，单个字符就是长度为1的字符串。

方括号可用于访问字符串的元素。

例如：

获取索引1处的字符（请记住第一个字符的位置为0）：
```text
a = "Hello, World!"
print(a[1])
```

## 5、字符串切片(Slicing)
可以使用slice语法返回一定范围的字符。

指定开始索引和结束索引，以冒号分隔，以返回字符串的一部分。

例如：

获取从索引2到索引5（不包括）的字符：
```text
b = "Hello, World!"
print(b[2:5])
```
结果：
```text
llo
```

Python 不支持单字符类型，单字符在 Python 中也是作为一个字符串使用。

Python 访问子字符串，可以使用方括号来截取字符串，如下实例：
```shell
#!/usr/bin/python

var1 = 'Hello World!'
var2 = "Python cjavapy"

print "var1[0]: ", var1[0]
print "var2[1:5]: ", var2[1:5]
```

## 6、负索引(Negative Indexing)
例如：

从字符串的末尾开始计数，将截取倒数第5个字符至倒数第2个（不包括在内）(倒数第1个索引是-1)：
```text
b = "Hello, World!"
print(b[-5:-2])
```
结果：
```text
orl
```

## 7、字符串长度(String Length)
要获取字符串的长度，请使用len()函数。

例如：

len（）函数返回字符串的长度：
```text
a = "Hello, World!"
print(len(a))
```

## 8、字符串方法
Python有一组可用于字符串的内置方法。

例如：

**strip()方法**从开头或结尾删除所有空格：
```text
a = " Hello, World! "
print(a.strip()) # returns "Hello, World!"
```

例如：

**lower()方法**以小写形式返回字符串：
```text
a = "Hello, World!"
print(a.lower())
```

例如：

**upper()方法**以大写形式返回字符串：
```text
a = "Hello, World!"
print(a.upper())
```

例如：

replace()方法将一个字符串替换为另一个字符串：
```text
a = "Hello, World!"
print(a.replace("H", "J"))
```

例如：

如果找到分隔符的实例，split()方法会将字符串拆分为子字符串：
```text
a = "Hello, World!"
print(a.split(",")) # returns ['Hello', ' World!']
```

通过我们的字符串方法参考了解有关字符串方法的更多信息。

## 9、判断字符串(in和not in)
要检查字符串中是否存在某些短语或字符，我们可以使用关键字in或not in。

例如：

检查以下文本中是否存在短语“ain”：
```text
txt = "The rain in Spain stays mainly in the plain"
x = "ain" in txt
print(x)
```
结果：
```text
True
```

例如：

检查以下文本中是否没有短语“ain”：
```text
txt = "The rain in Spain stays mainly in the plain"
x = "ain" not in txt
print(x)
```
结果：
```text
False
```

## 10、字符串连接
要连接或组合两个字符串，可以使用+运算符。

例如：

将变量a与变量b合并到变量c中：
```text
a = "Hello"
b = "World"
c = a + b
print(c)
```

例如：

要在它们之间添加空格，请添加“”：
```text
a = "Hello"
b = "World"
c = a + " " + b
print(c)
```

## 11、字符串格式化(format)
正如我们在“Python变量”一文中了解到的那样，我们无法像这样将字符串和数字组合在一起：

例如：
```text
age = 36
txt = "My name is cjavapy, I am " + age
print(txt)
```
报错：
```text
TypeError: can only concatenate str (not "int") to str
```

但是我们可以使用format（）方法将字符串和数字组合起来！

format（）方法采用传递的参数，对其进行格式化，然后将其放置在占位符{}所在的字符串中：

例如：

使用format（）方法将数字插入字符串：
```text
k = 13
txt = "c java python is {}"
print(txt.format(age))
```

format()方法接受无限数量的参数，并放置在各自的占位符中：

例如：
```text
a = 3
b = 567
c = 49.95
myorder = "I want {} + {} + {} = ?"
print(myorder.format(a, b, c))
```

您可以使用索引号{0}来确保将参数放置在正确的占位符中：

例如：
```text
a = 3
b = 567
c = 49.95
myorder = " {2} + {0} + {1} = ?"
print(myorder.format(a, b, c))
```
结果：
```text
 49.95 + 3 + 567 = ?
```

## 12、转义字符(Escape Character)
要在字符串中插入非法字符，请使用转义字符。

转义字符是反斜杠\，后跟要插入的字符。

错误情况示例是在字符串内用双引号引起来的双引号：

例如：

如果在双引号包围的字符串中使用双引号，则会出现错误：
```text
txt = "We are the so-called "cjavapy" from the website."
```
报错：
```text
SyntaxError: invalid syntax
```

要解决此问题，请使用转义符\“：

例如：

使用转义符可以在通常不允许的情况下使用双引号：
```text
txt = "We are the so-called \"cjavapy\" from the website."
```

Python中使用的其他转义字符：

| 转义字符   | 说明            |
|--------|---------------|
| \'     | 单引号           |
| \\|反斜杠 |
| \n     | 换行            |
| \r     | 回车            |
| \t     | Tab           |
| \b     | 退格(Backspace) |
| \f     | 换页            |
| \ooo   | 八进制值          |
| \xhh   | 十六进制值         |

## 13、字符串内建函数方法
Python有一组可用于字符串的内置方法。

注意：所有字符串方法都返回新值。 它们不会更改原始字符串。

| 方法             | 描述                        |
|----------------|---------------------------|
| capitalize()   | 将第一个字符转换为大写               |
| casefold()     | 将字符串转换为小写                 |
| center()       | 返回居中的字符串                  |
| count()        | 返回指定值在字符串中出现的次数           |
| encode()       | 返回字符串的编码版本                |
| endswith()     | 如果字符串以指定值结尾，则返回true       |
| expandtabs()   | 设置字符串的制表符大小               |
| find()         | 在字符串中搜索指定的值，并返回找到该字符串的位置  |
| format()       | 格式化字符串中的指定值               |
| format_map()   | 格式化字符串中的指定值               |
| index()        | 在字符串中搜索指定的值，并返回找到该字符串的位置  |
| isalnum()      | 如果字符串中的所有字符都是字母数字，则返回True |
| isalpha()      | 如果字符串中的所有字符都在字母中，则返回True  |
| isdecimal()    | 如果字符串中的所有字符均为小数，则返回True   |
| isdigit()      | 如果字符串中的所有字符都是数字，则返回True   |
| isidentifier() | 如果字符串是标识符，则返回True         |
| islower()      | 如果字符串中的所有字符均为小写，则返回True   |
| isnumeric()    | 如果字符串中的所有字符均为数字，则返回True   |
| isprintable()  | 如果字符串中的所有字符都是可打印的，则返回True |
| isspace()      | 如果字符串中的所有字符都是空格，则返回True   |
| istitle()      | 如果字符串遵循标题规则，则返回True       |
| isupper()      | 如果字符串中的所有字符均为大写，则返回True   |
| join()         | 将iterable的元素连接到字符串的末尾     |
| ljust()        | 返回字符串的左对齐版本               |
| lower()        | 将字符串转换为小写                 |
| lstrip()       | 返回字符串的左修剪版本               |
| maketrans()    | 返回要在翻译中使用的翻译表             |
| partition()    | 返回一个将字符串分为三部分的元组          |
| replace()      | 返回将指定值替换为指定值的字符串          |
| rfind()        | 在字符串中搜索指定的值，并返回找到它的最后位置   |
| rindex()       | 在字符串中搜索指定的值，并返回找到它的最后位置   |
| rjust()        | 返回字符串的右对齐版本               |
| rpartition()   | 返回一个将字符串分为三部分的元组          |
| rsplit()       | 在指定的分隔符处分割字符串，并返回一个列表     |
| rstrip()       | 返回字符串的右修剪版本               |
| split()        | 在指定的分隔符处分割字符串，并返回一个列表     |
| splitlines()   | 在换行符处分割字符串并返回一个列表         |
| startswith()   | 如果字符串以指定值开头，则返回true       |
| strip()        | 返回字符串的修剪版本                |
| swapcase()     | 交换大小写，小写变成大写，反之亦然         |
| title()        | 将每个单词的第一个字符转换为大写          |
| translate()    | 返回翻译后的字符串                 |
| upper()        | 将字符串转换为大写                 |
| zfill()        | 以指定的0值开头填充字符串             |

## 14、Unicode 字符串
在 Python2中，普通字符串是以8位ASCII码进行存储的，
而Unicode字符串则存储为16位unicode字符串，这样能够表示更多的字符集。
使用的语法是在字符串前面加上前缀u。

在 Python3中，所有的字符串都是Unicode字符串。

---

# Python 字符串格式化(format)

Python中，为了确保字符串将按预期显示，我们可以使用format()方法设置结果的格式。对字符串进行格式化输出。

## 1、字符串格式化(format)
format()方法允许格式化字符串的选定部分。

有时文本的某些部分是无法控制的，也许它们来自数据库或用户输入。

要控制这些值，请在文本中添加占位符（花括号{}），然后通过format()方法运行这些值：

例如：

添加一个占位符，输出字符串:
```text
y = 49
txt = "My age is {} years old"
print(txt.format(y))
```
output:
```text
My age is 49 years old
```

可以在大括号内添加参数以指定如何转换值：

例如：

显示为一个数字与两个小数:
```text
txt = "我的体重是{:.2f} KG"
```

在我们的String format()参考中查看所有格式类型。

## 2、格式化多个值
如果要使用更多值，只需将更多值添加到format()方法：
```text
print(txt.format(txt, age, count))
```

并添加更多占位符：

例如：
```text
txt = 3
age = 13
count = 41
my_order = "txt = {} age = {} count = {:.2f} "
print(my_order.format(txt, age, count))
```
output:
```text
txt = 3 age = 13 count = 41.00 
```

## 3、索引号
可以使用索引号(一个在花括号{0}中的数字)来确保这些值被放在正确的占位符中:

例如：
```text
txt = 3
age = 13
count = 41
my_order = "txt = {0} age = {1} count = {2:.2f} "
print(my_order.format(txt, age, count))
```
output:
```text
txt = 3 age = 13 count = 41.00 
```

另外，如果要多次引用相同的值，请使用索引号：

例如：
```text
name = "cjavapy"
url = "https://www.cjavapy.com"
txt = "name1 = {1} name2 = {1} url = {0}"
print(txt.format(url, name))
```
output:
```text
name1 = cjavapy name2 = cjavapy url = https://www.cjavapy.com
```

## 4、命名索引
还可以通过在大括号{name}中输入名称来使用命名索引，但是在传递参数值txt.format(name = "python")时必须使用名称:

例如：
```text
my_order = "this is {name}, it is a {type}."
print(my_order.format(name = "cjavapy", type = "website"))

name = "luke"
print(f"this is my name {name}")  # 格式化字符串常量
txt = f"this is my name {name}"  # 格式化字符串常量
print(txt)
```
output:
```text
this is cjavapy, it is a website.
this is my name luke
this is my name luke
```

f-string: formatted string literals, 格式化字符串常量。

功能同str.format() %-formatting, 较两者更简洁易用，推荐使用。

需要注意的是，Python3.6及以后的版本可用。

---

在Python中，字符串前面加上特定的前缀有不同的含义：

1）前缀f表示格式化字符串（Formatted String）：使用f-string可以在字符串中嵌入表达式，并将其值格式化为字符串。
在f-string中，用花括号{}括起的表达式会被替换为相应的值。

例如：
```text
name = "Alice"
age = 25
message = f"My name is {name} and I'm {age} years old."
print(message)
```

输出：
```text
My name is Alice and I'm 25 years old.
```

2）前缀r表示原始字符串（Raw String）：原始字符串中的转义字符不会被转义，而是按照字面意义来解释。常见用途是在正则表达式中使用，以避免双重转义。

例如：
```text
path = r"C:\Program Files\Python"
print(path)
```

输出：
```text
C:\Program Files\Python
```

3）前缀b表示字节字符串（Bytes String）：字节字符串是以字节（二进制）形式表示的字符串。
在字节字符串中，每个字符都用一个字节来表示。字节字符串通常用于处理二进制数据，例如读取图像或文件。

例如：
```text
data = b'\x48\x65\x6c\x6c\x6f'  # 表示字符串 "Hello" 的字节表示
print(data.decode())
```

输出：
```text
Hello
```

4）前缀u表示Unicode字符串：
在Python 2中，它表示Unicode字符串，
而在Python 3中，所有的字符串都是Unicode字符串，所以u前缀在Python 3中没有特殊含义，仍然可以使用。

例如：
```text
unicode_string = u"Hello, 世界"
print(unicode_string)
```

输出：
```text
Hello, 世界
```

需要注意的是，f、r、b、u前缀可以组合使用，例如fr"..."表示既是格式化字符串又是原始字符串。
