# Python Lambda表达式

Lambda函数是一个小的匿名函数。Lambda函数可以接受任意数量的参数，但只能具有一个表达式。

## 1、语法
```text
lambda arguments : expression
```
执行该表达式并返回结果：

例如：

在参数a上添加10，然后返回结果：
```text
x = lambda a : a + 10
print(x(5))
```
output:
```text
15
```

Lambda函数可以接受任意数量的参数：

例如：

将参数a与参数b相乘，并返回结果：
```text
x = lambda a, b: a * b
print(x(5, 6))
```
output:
```text
30
```

例如：

汇总参数a，b和c并返回结果：
```text
x = lambda a, b, c: a + b + c
print(x(5, 6, 2))
```
output:
```text
13
```

## 2、使用Lambda函数的好处
将lambda用作另一个函数中的匿名函数时，可以更好地使用lambda函数。

假设有一个接受一个参数的函数定义，该参数将与一个未知数相乘：
```text
def my_func(n):
    return lambda a: a * n
```

使用该函数定义可创建一个始终使您发送的数字翻倍的函数：

例如：
```text
def my_func(n):
    return lambda a: a * n


my_doubler = my_func(2)

print(my_doubler(11))
```
output:
```text
22
```

或者，使用相同的函数定义，使一个函数总是三倍的数字你发送:

例如：
```text
def my_func(n):
    return lambda a: a * n


my_triple = my_func(3)

print(my_triple(11))
```
output:
```text
33
```

或者，在同一程序中使用相同的函数定义来创建两个函数：

例如：
```text
def my_func(n):
    return lambda a: a * n


my_double = my_func(2)
my_triple = my_func(3)

print(my_double(11))
print(my_triple(11))
```
output:
```text
22
33
```

在短时间内需要使用匿名函数时，请使用lambda函数。