# Python 函数

函数是仅在调用时运行的代码块。可以将数据（称为参数）传递给函数。
函数可以返回数据。函数是组织好的，可重复使用的，用来实现单一，或相关联功能的代码段。
函数能提高应用的模块性，和代码的重复利用率。

已经知道Python提供了许多内建函数，比如print()。但你也可以自己创建函数，这被叫做用户自定义函数。

## 1、定义函数
在 Python中，函数是使用def关键字定义的：

例如：
```text
def my_function():
    print("cjavapy from a function")
```

## 2、调用函数
要调用函数，请在函数名称后加上括号：

例如：
```text
def my_function():
    print("cjavapy from a function")

my_function()
```
output:
```text
cjavapy from a function
```

## 3、函数的参数
数据可以作为参数传递给函数。

在函数名称后的括号内指定参数。 可以根据需要添加任意数量的参数，只需用逗号分隔即可。

以下示例具有一个带有一个参数（fname）的函数。调用该函数时，我们传递一个名字，该名字在函数内部用于打印全名：

例如：
```text
def my_function(lname):
    print(lname + " is useful")

my_function("c")
my_function("java")
my_function("python")
```
output:
```text
c is useful
java is useful
python is useful
```

在Python文档中，参数通常缩短为args。

## 4、函数形参或函数实参
函数形参和函数实参可用于同一事物：传递到函数中的数据。

从函数的角度来看：**函数形参**是在函数定义的括号内列出的变量，**函数实参**是在调用时发送给函数的值。

## 5、实参的数量
默认情况下，必须使用正确数量的参数调用函数。这意味着，如果您的函数需要2个参数，则必须使用2个参数来调用函数。

例如：

该函数需要2个参数，调用传递2个参数：
```text
def my_function(fname, lname):
    print(fname + " " + lname)

my_function("cjavapy", "python")
```
output:
```text
cjavapy python
```

例如：

该函数需要2个参数，但调用只传递一个参数：
```text
def my_function(fname, lname):
    print(fname + " " + lname)

my_function("cjavapy")
```
报错：
```text
TypeError: my_function() missing 1 required positional argument: 'lname'
```

## 6、任意个数的参数 *args
如果不知道将传递给函数多少个参数，请在函数定义的参数名称之前添加*。

函数将接收一个元组参数，并可以访问元组中参数：

例如：

如果参数个数未知，请在参数名称之前添加*：
```text
def my_function(*langs):
    print("最受欢迎的编程语言是 " + langs[2])

my_function("c", "java", "python")
```
output:
```text
最受欢迎的编程语言是 python
```

在Python文档中，任意个数参数通常缩写为*args。

## 7、关键字参数
可以使用key = value语法传递参数。这样传递参数就可以不按顺。

例如：
```text
def my_function(l3, l2, l1):
    print("最受欢迎的编程语言是 " + l3)
    

my_function(l1 = "c", l2 = "java", l3 = "python")
```
output:
```text
最受欢迎的编程语言是 python
```

在Python文档中，关键字自变量一词通常简称为kwargs。

## 8、命名关键字参数
关键字参数，对于传入的参数名无法限制。如需对参数名有限制，就要用命名关键字参数。
命名关键字参数需要使用*分隔参数，*后面的参数被视为命名关键字参数，
命名关键字参数必须使用key=value形式传入，key是定义时确定的不可以改变。
如下，
```text
# 命名关键字参数
def args4(a1, a2, *, a3):
    print("args4:")
    print(a1)
    print(a2)
    print(a3)


kag = {"a1": "C", "a2": "Java", "a3": "Python"}
args4(1, 2, a3=3)
args4(**kag)
```
output:
```text
args4:
1
2
3
args4:
C
Java
Python
```

## 9、任意个数关键字参数 **kwargs
如果不知道将传递给的函数多少个关键字参数，请在函数定义的参数名称前添加两个星号：**。

这样函数将接收参数字典，并可以访问相应的参数：

例如：

如果关键字参数的数量未知，请在参数名称之前添加双**：
```text
def my_function(**langs):
    print("最受欢迎的编程语言是 " + langs["lname"])


my_function(lage="20", lname="cjavapy")
```
output:
```text
最受欢迎的编程语言是 cjavapy
```

Python文档中，任意Kword参数通常缩短为**kwargs。

## 10、参数默认值
下面的示例显示如何使用默认参数值。

如果我们调用不带参数的函数，它将使用默认值：

例如：
```text
def my_function(lang="python"):
    print("I like " + lang)


my_function("c")
my_function("c#")
my_function()
my_function("cjavapy")
```
output:
```text
I like c
I like c#
I like python
I like cjavapy
```

## 11、将列表作为参数传递
可以将参数的任何数据类型发送到函数（字符串，数字，列表，字典等），它将在函数内部被视为相同的数据类型。

例如。 如果将列表作为参数传递，则在函数内部时仍是列表：

例如：
```text
def my_function(langs):
    for x in langs:
        print(x)


l = ["c", "java", "python"]

my_function(l)
```
output:
```text
c
java
python
```

## 12、函数返回值
要让函数返回值，请使用return语句：

例如：
```text
def my_function(x):
    return 5 * x
    

print(my_function(3))
print(my_function(5))
print(my_function(9))
```
output:
```text
15
25
45
```

## 13、pass语句
function定义不能为空，但是如果出于某种原因function函数没有内容，需要使用pass语句，来避免报错。

例如：
```text
def my_function():
    pass
```

## 14、函数递归
Python还接受函数递归，就是定义的函数可以调用自身。

递归是常见的数学和编程概念。 就是一个函数调用自己。 这样做的好处是，可以遍历数据以获得结果。

开发人员在进行递归时应该非常小心，因为它很容易进入编写永远不会终止的函数，或者使用过多内存或处理器资源的函数。 
但是，如果编写正确，则递归可以是一种非常有效且数学上精巧的编程方法。

在此示例中，tri_recursion()是我们定义为调用自身的函数（“递归”）。 
我们将k变量用作数据，每次递归时该变量减（-1）。当条件不大于0（即为0）时，递归结束。

对于初学者来说，可能需要花费一些时间来确定其工作原理，最好的测试方法是测试和修改它。

例如：

递归的例子
```text
def tri_recursion(k):
    if k > 0:
        result = k + tri_recursion(k - 1)
        print(result)
    else:
        result = 0
    return result


print("递归的示例")
tri_recursion(5)
```
output:
```text
递归的示例
1
3
6
10
15
```
