# Python 变量

本文主要介绍Python中，创建变量、变量名、将值分配给多个变量、输出变量、全局变量和全局关键字方法，以及相关的示例代码。

## 1､定义变量

变量其实通过一个标记调用内存中的值，而变量名就是这个标记的名称，
但是万一这个标记已经被提前占用或者解释器认为这个标记是不合法的，那么就会报错。
与其他编程语言不同，Python没有用于声明变量的命令。

变量是在首次为其分配值时创建的。

例如，
```text
x = 5
y = "cjavapy"
print(x)
print(y)
```

Python中变量不需要用任何特定类型声明，甚至可以为变量赋值后更改变更类型。

例如，
```text
x = 4 # x 是int类型
x = "cjavapy is website" # 现在x是str字符串类型
print(x)
```

可以使用单引号或双引号声明字符串变量：

例如，
```text
x = "Python"
# 两种都声明了字符串类型的变量
x = 'cjavapy'
```

## 2､变量名

Python变量命名规则：
- 变量名只能包含字母、数字和下划线。
- 变量名可以字母或下划线打头，但不能以数字开头
- 变量名不能包含空格，但可使用下划线来分隔其中的单词。
- 不要将Python关键字和函数名用作变量名，即不要使用Python保留用于特殊用途的单词，如print 。
- 变量名应既简短又具有描述性。
- 慎用小写字母l和大写字母O，因为它们可能被人错看成数字1和0。

例如，
```text
# 符合命名规范的变量名:
myvar = "marry"
my_var = "cjavapy"
_my_var = "cjavapy"
myVar = "cjavapy"
MYVAR = "cjavapy"
myvar2 = "cjavapy"

# 非法的变量名:
2myvar = "cjavapy"
my-var = "cjavapy"
my var = "cjavapy"
```

注意：变量名称区分大小写

## 3､单行多个变量赋值

Python允许在一行中将值分配给多个变量：

例如，
```text
x, y, z = "c", "python", "java"
print(x)
print(y)
print(z)
```

可以在一行中将相同的值分配给多个变量：

例如，
```text
x = y = z = "Python"
print(x)
print(y)
print(z)
```

## 4、输出变量

Python print语句通常用于输出变量。

为了结合文本和变量，Python使用以下 + 字符：

例如，
```text
x = "cjavapy"
print("Python " + x)
```

还可以使用+字符将变量添加到另一个变量：

例如，
```text
x = "Python "
y = "cjavapy"
z =  x + y
print(z)
```

对于数字，+字符用作数学运算符：

例如，
```text
x = 5
y = 10
print(x + y)
```

如果您尝试将字符串和数字组合在一起，Python会给您一个错误：

例如，
```text
x = 5
y = "cjavapy"
print(x + y)
```
报错：
```text
TypeError: unsupported operand type(s) for +: 'int' and 'str'
```

## 5、局部变量

局部变量 是在 函数内部 定义的变量，只能在函数内部使用函数执行结束后，函数内部的局部变量，会被系统回收。
不同的函数内，可以定义相同的名字的局部变量，但是 彼此之间 不会产生影响。

例如，
```text
def demo1():
    num = 10
    print(num)
    num = 20
    print("修改后 %d" % num)

def demo2():
    num = 100
    print(num)

demo1()
demo2()
```

## 6、全局变量

在函数外部创建的变量（如上述所有示例所示）称为全局变量。

全局变量可以被函数内部和外部的每个人使用。

例如，

在函数外部创建变量，并在函数内部使用它
```text
x = "cjavapy"

def myfunc():
    print("Python " + x)
    
myfunc()
```

如果在函数内部创建具有相同名称的变量，则此变量将是局部变量，并且只能在函数内部使用。
具有相同名称的全局变量将保留原样，并具有原始值。

例如，

在函数内部创建一个与全局变量同名的变量
```text
x = "awesome"
def myfunc():
    x = "cjavapy"
    print("Python is " + x)
    
myfunc()
print("Python " + x)
```

## 7、global关键字

通常，在函数内部创建变量时，该变量是局部变量，只能在该函数内部使用。
要在函数内部创建全局变量，可以使用 global关键字。

例如，

如果使用global关键字，则该变量属于全局范围：
```text
def myfunc():
    global x
    x = "cjavapy"

myfunc()

print("c java python is " + x)
```

例如，

要在函数内部更改全局变量的值，必须使用global关键字的变量，否则是重新声明一个变量。
```text
x = "js"
def myfunc():
    global x
    x = "cjavapy"

myfunc()
print("c java python is " + x)
```
