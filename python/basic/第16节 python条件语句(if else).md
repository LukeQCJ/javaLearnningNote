# Python 条件语句(If else)

Python if条件语句是通过一条或多条语句的执行结果（True 或者 False）来决定执行的代码块。
本文主要介绍一下Python中if条件语句使用。

## 1、Python If条件语句

Python支持数学中常见的逻辑条件:
- 等于: a == b
- 不等于: a != b
- 小于: a < b
- 小于等于: a <= b
- 大于: a > b
- 大于等于: a >= b

这些条件可以几种方式使用，最常见的是在 “if语句” 和循环中使用。

使用if关键字编写“if语句”。

例如：

If语句:
```text
a = 33
b = 200
if b > a:
    print("b 大于 a")
```

在此示例中，我们使用两个变量a和b作为if语句的一部分，以测试b是否大于a。 
当a为33，b为200时，我们知道200大于33，因此我们在屏幕上打印 “b大于a”。

## 2、缩进
Python依靠缩进（在行首的空白）在代码中定义范围。为此，其他编程语言通常使用花括号。

例如：

注意if代码块中的缩进:：
```text
a = 33
b = 200
if b > a:
    print("b 大于 a")
```

如果语句不带缩进（将产生错误）：
```text
a = 33
b = 200
if b > a:
print("b 大于 a") # 将得到一个错误
```

## 3、if条件语句中elif
elif关键字是在if条件不满足的情况，则执行elif条件。

例如：
```text
a = 33
b = 33
if b > a:
    print("b 大于 a")
elif a == b:
    print("a 和 b 相等")
```

在此示例中，a等于b，因此第一个条件不成立，但elif条件成立，因此我们在屏幕上打印"a 和 b 相等"。

## 4、if条件语句中else
else关键字可捕获上述条件未捕获的任何内容。

例如：
```text
a = 200
b = 33
if b > a:
    print("b 大于 a")
elif a == b:
    print("a 和 b 相等")
else:
    print("a 大于 b")
```

在此示例中，a大于b，因此第一个条件不成立，elif条件也不成立，所以执行else条件并打印输出 “a大于b”。

也可以使用else而不使用elif：

例如：
```text
a = 200
b = 33
if b > a:
    print("b 大于 a")
else:
    print("b 不大于 a")
```

## 5、简写 If 语句
如果只有一条语句要执行，则可以将其与if语句放在同一行。

例如：

一行if语句:
```text
if a > b: print("a 大于 b")
```

## 6、简写 If ... Else 语句
如果只有一条语句要执行，一条语句要执行，另一条语句要执行，则可以将所有语句放在同一行上：

例如：

一行if else语句：
```text
a = 2
b = 330
print("A") if a > b else print("B")
```
output:
```text
B
```

```text
a = 2300
b = 330
print("A") if a > b else print("B")
```
output:
```text
A
```

该技术称为**三元运算符**或**条件表达式**。

在同一行上还可以有多个else语句:

例如：

一行if else语句，3个条件:
```text
a = 330
b = 330
print("A") if a > b else print("=") if a == b else print("B")
```
output:
```text
=
```

## 7、if条件中的and
and关键字是一个逻辑运算符，用于组合条件语句:

例如：

判断a是否大于b, c是否大于a:
```text
a = 200
b = 33
c = 500
if a > b and c > a:
    print("两个条件都是True")
```
output:
```text
两个条件都是True
```

## 8、if条件中的or
or关键字是一个逻辑运算符，用于组合条件语句:

例如：

判断a是否大于b，或a是否大于c:
```text
a = 220
b = 33
c = 510
if a > b or a > c:
    print("至少有一个条件是True")
```
output:
```text
至少有一个条件是True
```

## 9、 If条件嵌套
如果语句内部可以有if语句，这被称为嵌套if语句：

例如：
```text
x = 41
if x > 10:
    print("大于10")
    if x > 20:
        print("并且也大于20")
    else:
        print("不大于20")
```
output:
```text
大于10
并且也大于20
```

## 10、if条件语句中使用pass
if语句不能为空，但是如果出于某种原因，有一个没有内容的if语句，使用pass语句以避免出现错误：

例如：
```text
a = 33
b = 200
if b > a:
    pass
```
