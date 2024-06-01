# Python try except finally异常处理

Python中try块可以**捕获**测试代码块中的错误。except块可以**处理**错误。
finally块是不管try和except块是否出现异常都可以执行代码。

## 1、异常处理
当发生错误或异常时，Python通常会停止并生成错误消息。

这些异常可以使用try语句处理：

例如：

try块将生成一个异常，因为未定义x：
```text
try:
    print(x)
except:
    print("An exception occurred")
```
output:
```text
An exception occurred
```

由于try块引发错误，因此将执行except块。

没有try块，程序将崩溃并引发错误：

例如：

该语句将引发错误，因为未定义x：
```text
print(x)
```
报错：
```text
NameError: name 'x' is not defined
```

## 2、多个异常处理
可以定义任意数量的异常块，例如 如果要为特殊类型的错误执行特殊代码块：

例如：

如果try块出现NameError，则打印输出一条提示信息，如果出现其他错误，则打印输出另一条提示信息:
```text
try:
    print(x)
except NameError:
    print("Variable x is not defined")
except:
    print("其它类型异常")
```
output:
```text
Variable x is not defined
```

## 3、else
可以使用else关键字定义在未引发错误的情况下要执行的代码块：

例如：

在此示例中，try块不会产生任何错误：
```text
try:
    print("Hello")
except:
    print("出现异常错误")
else:
    print("正常执行没出错")
```
output:
```text
Hello
正常执行没出错
```

## 4、finally
finally块（如果指定）将被执行，而不管try块是否引发错误。

例如：
```text
try:
    print(x)
except:
    print("出现异常错误")
finally:
    print("无论是否发生异常都会执行")
```
output:
```text
出现异常错误
无论是否发生异常都会执行
```

可以用来关闭对象和清理资源:

例如：

尝试打开并写入不可写的文件：
```text
try:
    f = open("demofile.txt")
    f.write("cjavapy")
except:
    print("写入文件时发生异常")
finally:
    f.close()
```

程序可以继续运行，并且关闭了打开文件对象。

## 5、抛出引发异常(raise)
作为Python开发人员，可以在某些情况下引发异常。

要抛出（或引发）异常，请使用raise关键字。

例如：

当x小于0时，抛出错误并停止程序:
```text
x = -1

if x < 0:
    raise Exception("x需要是大于等于0的数字")
```

raise关键字用于引发异常。

可以定义引发哪种错误，以及向用户显示文本。

例如：

如果x不是整数，则引发TypeError：
```text
x = "hello"

if not type(x) is int:
    raise TypeError("只允许是整数")
```

## 6、Python3与Python2的区别(try except)
Python2和Python3中try except主要区别是一个用的是,，另一个用的是as，具体如下：

1）Python2
```text
try:
    raise
except Exception, e: # 区别主要是这里是,
    print (e)
    return false
```

2）Python3
```text
try:
    raise
except Exception as e: # 区别主要这里是as
    print (e)
    return false
```

## 7、输出异常信息(行号)
输出带有错误行号的详细异常信息，需要先使用import traceback导入traceback，然后就可以输出带有行号的详细异常信息，如下，
```text
import traceback

try:
    raise
except Exception as e:# Python2中语法：except Exception, e:
    print sys._getframe().f_lineno, 'str(e):\t\t', str(e)
    print sys._getframe().f_lineno, 'repr(e):\t', repr(e)
    print sys._getframe().f_lineno, 'e.message:\t', e.message
    print sys._getframe().f_lineno, 'traceback.print_exc():'; traceback.print_exc()
    print sys._getframe().f_lineno, 'traceback.format_exc():\n%s' % traceback.format_exc()
```
