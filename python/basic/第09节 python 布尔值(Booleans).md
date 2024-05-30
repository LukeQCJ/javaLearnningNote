# Python 布尔值(Booleans)

Python中布尔值(Booleans)表示以下两个值之一：True或False。
本文主要介绍布尔值(Booleans)的使用，和使用时需要注意的地方，以及相关的示例代码。

## 1、布尔值(Booleans)

在编程中，通常需要知道表达式是True还是False。

可以执行 Python中的任何表达式，并获得两个值之一，True或False。

比较两个值时，将对表达式求值， Python返回布尔值：

例如：
```text
print(11 > 9)
print(12 == 9)
print(13 < 9)
```

在if语句中运行条件时，Python返回True或False：

例如：

根据条件是True还是False打印一条消息：
```text
a = 200
b = 33

if b > a:
    print("b 大于 a")
else:
    print("b 不大于 a")
```

## 2、布尔值类型的转换和变量（bool()）
bool()函数可以将其它类型转换为布尔值类型True或False，

例如：

使用bool()对字符串和数字转换布尔值：
```text
print(bool("Hello"))
print(bool(15))
```

例如：

使用bool()对两个变量转换布尔值：
```text
x = "Hello"
y = 15

print(bool(x))
print(bool(y))
```

## 3、大多数的值都是True
如果它具有某种内容，则几乎所有值都将转换为True。

除空字符串(''或“”)外，任何字符串均为True。

除0外，任何数字均为True。

任何非空的列表，元组，集合和字典都是True。

例如：

以下内容将返回True：
```text
bool("abc")
bool(123)
bool(["apple", "cherry", "banana"])
```

## 4、某些值是False
实际上，除了空值（例如（），[]，{}，“”，数字0和值None。 当然，值False也可转换为False。

例如：

以下将返回False：
```text
bool(False)
bool(None)
bool(0)
bool("")
bool(())
bool([])
bool({})
```

另外一个值或对象（在这种情况下）的计算结果为False，
也就是说，如果您有一个对象，该对象是由具有__len__函数的类制成的，该函数返回0或False：

例如：
```text
class myclass():
    def __len__(self):
        return 0

myobj = myclass()
print(bool(myobj))
```

## 5、函数方法可以返回一个布尔值
可以创建返回布尔值的函数：

例如：

打印输出函数结果：
```text
def myFunction() :
    return True

print(myFunction())
```

可以根据函数返回值布尔值判断执行代码：

例如：

打印输出“YES!” 如果函数返回True，否则打印“NO!”：
```text
def myFunction() :
    return True

if myFunction():
    print("YES!")
else:
    print("NO!")
```

Python还具有许多内置的返回布尔值的函数，例如，isinstance()函数，可用于确定对象是否属于某种数据类型：

例如：

判断对象是否为int：
```text
x = 200
print(isinstance(x, int))
```

## 6、使用示例代码
```text
print(bool())
print(bool(False))
print(bool(0), bool(0.0), bool(0j))
print(bool(""), bool(()), bool([]), bool({}))

class alfalse():
    def __bool__(self):           # 定义了 __bool__() 方法，始终返回False
        return False

f = alfalse()
print(bool(f))

class alzero():
    def __len__(self):            # 定义了 __len__() 方法，始终返回0
        return 0

zero = alzero()
print(bool(zero))

class justaclass():
    pass

c = justaclass()
print(bool(c))                    # 一般class instance都返回为True
```
结果：
```text
False
False
False False False
False False False False
False
False
True
```