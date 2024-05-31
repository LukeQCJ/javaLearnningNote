# Python 作用域(scope)

变量仅在创建区域内可用。 这称为作用域(scope)。本主主要介绍一下 Python中的作用域(scope)。

## 1、局部作用域(Local Scope)
在函数内部创建的变量属于该函数的本地范围作用域(scope)，并且只能在该函数内部使用。

例如：

在函数内部创建的变量在函数内部可用:
```text
def myfunc():
    x = 300
    print(x)

myfunc()
```

**函数内部函数**

如以上示例中所述，变量x在函数外部不可用，但对于函数内部的任何函数均可用：

例如：

可以从函数内的一个函数访问局部变量：
```text
def my_func():
    x = 300

    def my_inner_func():
        print(x)

    my_inner_func()


my_func()
```
output:
```text
300
```

## 2、全局作用域(Global Scope)
在 Python代码主体中创建的变量是全局变量，属于全局范围。

全局变量可以在任何作用域中使用，包括全局和局部作用域。

例如：

在函数外部创建的变量是全局变量，全局都可以使用：
```text
x = 300


def my_func():
    print(x)


my_func()

print(x)
```
output:
```text
300
300
```

**命名变量**

如果在函数内部和外部使用相同的变量名进行操作，
Python会将它们视为两个单独的变量，一个在全局范围内可用（在函数外部），而一个在局部范围内可用（在函数内部）：

例如：

该函数将打印本地x，然后该代码将打印全局x：
```text
x = 300


def my_func():
    x = 200
    print(x)


my_func()

print(x)
```
output:
```text
200
300
```

## 3、global全局关键字
如果需要在方法或函数内创建一个全局变量，则可以使用global关键字。

global关键字使变量成为全局变量。

例如：

如果使用global关键字，变量属于全局作用域:
```text
def my_func():
    global x
    x = 300


my_func()

print(x)
```
output:
```text
300
```

另外，如果想要对函数内部的全局变量进行更改，也可以使用global关键字。

例如：

要改变函数内部全局变量的值，可以使用global关键字引用该变量:
```text
x = 300


def my_func():
    global x
    x = 200


my_func()

print(x)
```
output:
```text
200
```
