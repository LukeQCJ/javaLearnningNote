# Python 编写代码语法

本文主要介绍Python代码编写相关的语法规则，包括执行代码的方法，Python代码的缩进，和Python中声明变量，以及相关示例。

## 1､执行Python代码

正如我们在上一页中学到的，可以通过直接在命令行中编写代码来执行Python语法：
```text
>>> print("Hello, World!")
Hello, World!
```

或通过使用.py文件扩展名在服务器上创建python文件，然后在命令行中运行它：
```text
C:\Users\py> python myfile.py
```

## 2､Python代码缩进

缩进是指代码行开头的空格。

在其他编程语言中，代码中的缩进仅出于可读性考虑，而Python中的缩进非常重要。

Python使用缩进来指示代码块。

例如，
```text
if 5 > 3:
    print("cjavapy")
```

如果您跳过缩进，Python会给您一个错误：

例如，
```text
if 5 > 3:
print("cjavapy")
```
报错：
```text
IndentationError: expected an indented block after 'if' statement on line 1
```

空格数量由程序员决定，但必须至少一个。

例如，
```text
if 5 > 4:
  print("cjavapy")
if 5 > 3:
    print("cjavapy")
```

必须在同一代码块中使用相同数量的空格，否则Python将给您一个错误：

例如，
```text
if 5 > 2:
  print("cjavapy")
   print("cjavapy")
```
报错：
```text
IndentationError: unexpected indent
```

## 3､Python变量声明和赋值

在Python中，在为变量赋值时会创建变量：

例如，

Python中的变量：
```text
x = 5
y = "Hello, World!"
```

Python没有用于声明变量的命令。

将在“Python变量”一章中了解有关变量的更多信息。

## 4､Python代码注释

Python具有注释功能，可用于代码内文档。

注释以＃开头，Python将把其余的行作为注释呈现：

例如，

Python中的注释：
```text
# This is a comment.
print("Hello, World!")
```
