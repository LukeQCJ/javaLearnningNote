# Python 正则表达式(RegEx)

RegEx或正则表达式是形成搜索模式的一系列字符。
正则表达式可用于检查字符串是否包含指定的搜索模式。也可以进行字符串的替换和提取。
本文主要介绍Python正则表达式(RegEx)。

## 1、re模块(Module)
Python有一个名为re的内置包，它可用于处理正则表达式。

导入re模块：
```text
import re
```

## 2、Python中正则表达式(RegEx)
导入re模块后，可以开始使用正则表达式：

例如：

搜索字符串以查看它是否以"The"开头并以"cjavapy"结尾：
```text
import re

txt = "The website is cjavapy"
x = re.search("^The.*cjavapy$", txt)
```

## 3、re模块函数方法
re模块提供了一组函数，使我们可以在字符串中搜索匹配项：

| 函数      | 描述                          |
|---------|-----------------------------|
| findall | 返回包含所有匹配项的列表                |
| search  | 如果字符串中任何地方都存在匹配项，则返回Match对象 |
| split   | 返回一个列表，该字符串在每次匹配时均已拆分       |
| sub     | 用字符串替换一个或多个匹配项              |

## 4、元字符(Metacharacters)
元字符是具有特殊含义的字符：

| 元字符 | 描述                     | 示例              |
|-----|------------------------|-----------------|
| []  | 一组字符                   | "[a-m]"         |
| \   | 发出特殊序列的信号（也可以用于转义特殊字符） | "\d"            |
| .   | 任何字符（换行符除外）            | "he..o"         |
| ^   | 匹配一行的开头位置              | "^hello"        |
| $   | 匹配一行的结束位置              | "world$"        |
| *   | 零次或更多次                 | "aix*"          |
| +   | 1次或更多次                 | "aix+"          |
| {n} | n次出现                   | "al{2}"         |
| \|  | 两者任一                   | "falls \|stays" |
| ()  | 捕获和组                   |                 |

## 5、特殊字符
特殊字符是\，后跟下面列表中的字符之一，并且具有特殊含义：

| 字符 | 描述                                                           | 示例               |
|----|--------------------------------------------------------------|------------------|
| \A | 如果【指定的字符在字符串的开头】，则返回匹配项                                      | "\AThe"          |
| \b | 返回一个匹配项，其中【指定的字符位于单词的开头或结尾（开头的“r”确保该字符串被视为“原始字符串”） 】         | r"\bain"r"ain\b" |
| \B | 返回包含指定字符的匹配，但【不包含在单词的开头(或结尾)(开头的“r”确保将该字符串作为"raw string"处理)】 | r"\Bain"r"ain\B" |
| \d | 返回字符串【包含数字（0到9之间的数字）】的匹配项                                    | "\d"             |
| \D | 返回字符串【不包含数字】的匹配项                                             | "\D"             |
| \s | 返回字符串【包含空格字符】的匹配项                                            | "\s"             |
| \S | 返回字符串【不包含空格字符】的匹配项                                           | "\S"             |
| \w | 返回一个匹配项，该字符串【包含任何单词字符（从a到Z的字符，0-9的数字和下划线_字符）】                | "\w"             |
| \W | 返回一个匹配项，其中字符串【不包含任何单词字符】                                     | "\W"             |
| \Z | 如果指定的字符位于字符串的末尾，则返回匹配项                                       | "Spain\Z"        |

## 6、集合
集合是在方括号[]中的一组字符，它们具有特殊含义：

| 集合         | 描述                                                   |
|------------|------------------------------------------------------|
| [arn]      | 返回存在指定字符（a，r或n）之一的匹配项                                |
| [a-n]      | 返回任何小写字符的匹配项，按字母顺序在a和n之间                             |
| [^arn]     | 返回除a，r和n以外的任何字符的匹配项                                  |
| [0123]     | 返回存在任何指定数字（0，1，2或3）的匹配项                              |
| [0-9]      | 返回0和9之间的任何数字的匹配项                                     |
| [0-5][0-9] | 返回00和59中任何两位数字的匹配项                                   |
| [a-zA-Z]   | 返回a和z之间的任何字母的匹配项，小写或大写                               |
| [+]        | 设置为+，*，。，\|，（），$，{}没有特殊含义，因此[+]的意思是：返回以下任何匹配的+字符 字符串 |

## 7、findall()函数
findall()函数返回包含所有匹配项的列表。

例如：

打印所有匹配项的列表：
```text
import re

txt = "my name is cjavapy"
x = re.findall("am", txt)
print(x)
```
output:
```text
['am']
```

该列表按找到匹配项的顺序包含匹配项。

如果找不到匹配项，则返回一个空列表：

例如：

如果找不到匹配项，则返回一个空列表：
```text
import re

txt = "my name is cjavapy"
x = re.findall("python", txt)
print(x)
```
output:
```text
[]
```

## 8、search()函数
search()函数搜索匹配的字符串，如果有匹配，则返回一个匹配对象。

如果有多个匹配项，则仅返回匹配项的第一个匹配项：

例如：

搜索字符串中的第一个空格字符：
```text
import re

txt = "my name is cjavapy"
x = re.search("\\s", txt)

print("第一个空白字符位置:", x.start())
```
output:
```text
第一个空白字符位置: 2
```
如果找不到匹配项，则返回值None：

例如：

做一个搜索，返回不匹配:
```text
import re

txt = "my name is cjavapy"
x = re.search("python", txt)
print(x)
```
output:
```text
None
```

## 9、split()函数
split()函数返回一个列表，其中字符串已分割在每个匹配:

例如：

在每个空格字符处分割：
```text
import re

txt = "my name is cjavapy"
x = re.split("\\s", txt)
print(x)
```
output:
```text
['my', 'name', 'is', 'cjavapy']
```

可以通过指定maxsplit参数来控制出现次数：

例如：

仅在第一次出现时才拆分字符串：
```text
import re

txt = "my name is cjavapy"
x = re.split("\s", txt, 1)
print(x)
```
output:
```text
['my', 'name is cjavapy']
```

## 10、sub()函数
sub()函数用选择的文本替换匹配:

例如：

将每个空白字符替换为数字7：
```text
import re

txt = "my name is cjavapy"
x = re.sub("\\s", "7", txt)
print(x)
```
output:
```text
my7name7is7cjavapy
```

可以通过指定count参数来控制替换次数：

例如：

替换前两个事件：
```text
import re

txt = "my name is cjavapy"
x = re.sub("\\s", "7", txt, 2)
print(x)
```
output:
```text
my7name7is cjavapy
```

## 11、Match 对象(Object)
匹配对象是包含有关搜索和结果信息的对象。

注意：如果没有匹配项，则将返回值None，而不是Match Object。

例如：

进行搜索以返回匹配对象：
```text
import re

txt = "my name is cjavapy"
x = re.search("am", txt)
print(x)  # 输出的是一个对象
```
output:
```text
<re.Match object; span=(4, 6), match='am'>
```

匹配对象具有用于检索有关搜索信息的属性和方法，以及结果:
- .span()返回一个包含匹配的开始和结束位置的元组。
- .string返回传递给该函数的字符串
- .group()返回字符串中存在匹配项的部分

例如：

打印第一个匹配项的位置（开始和结束位置）。 正则表达式查找以大写字母“C”开头的所有单词：
```text
import re

txt = "my name is Cjavapy"
x = re.search(r"\bC\w+", txt)
print(x.span())
```
output:
```text
(11, 18)
```
这里面的(11, 18)包括11不包括18。

例如：

打印传递给函数的字符串：
```text
import re

txt = "my name is Cjavapy"
x = re.search(r"\bC\w+", txt)
print(x.string)
```
output:
```text
my name is Cjavapy
```

例如：

打印匹配的字符串部分。正则表达式查找以大写字母“C”开头的所有单词：
```text
import re

txt = "my name is Cjavapy"
x = re.search(r"\bC\w+", txt)
print(x.group())
```
output:
```text
Cjavapy
```
注意：如果没有匹配项，则将返回值None，而不是Match Object。