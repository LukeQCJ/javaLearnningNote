# Python for循环语句

Python for循环可以遍历任何序列的项目，如一个列表或者一个字符串。
本文主要介绍一下Python中，for循环语句的使用。

## 1、Python for循环

for循环用于遍历一个序列（列表，元组，字典，集合或字符串）。

这不太像其他编程语言中的for关键字，而更像其他面向对象编程语言中的迭代器方法那样工作。

使用for循环，我们可以执行语句，对列表，元组，集合等中的每个项目执行一次。

例如：

打印输出列表：
```text
langs = ["c", "java", "python","cjavapy"]
for x in langs:
    print(x)
```
output:
```text
c
java
python
cjavapy
```

for循环不需要预先声明索引变量。

## 2、for循环遍历字符串
即使字符串是可迭代的对象，它们也单个字符组成：

例如：

遍历 "cjavapy” 字符串中的字母：
```text
for x in "cjavapy":
    print(x)
```

## 3、break语句
使用break语句，我们可以在循环遍历所有项目之前停止循环：

例如：

当x为 "java" 时退出循环：
```text
langs = ["c", "java", "python"]
for x in langs:
    print(x)
    if x == "java":
        break
```
output:
```text
c
java
```

例如：

当x是 “java” 时退出循环，但是在print(x)之前出现break：
```text
langs = ["c", "java", "python"]
for x in langs:
    if x == "java":
        break
    print(x)
```
output:
```text
c
```

## 4、continue语句
使用continue语句，我们可以停止循环的当前迭代，然后继续下一个：

例如：

不要打印python：
```text
langs = ["c", "java", "python","cjavapy"]
for x in langs:
    if x == "python":
        continue
    print(x)
```
output:
```text
c
java
cjavapy
```

## 5、range()函数
要对一组代码进行指定次数的循环，可以使用range()函数，

range()函数返回一个数字序列，默认情况下从0开始，递增1(默认情况下)，并以指定的数字结束。

例如：

使用range()函数：
```text
for x in range(6):
    print(x)
```
output:
```text
0
1
2
3
4
5
```

注意：range(6)不是0到6的值，而是0到5的值。

range()函数默认将初始值设置为0，但是可以通过添加参数range(2,6)来指定初始值：
range(2,6)中表示2到6（但不包括6）之间的值：

例如：

使用开始参数：
```text
for x in range(2, 6):
    print(x)
```
output:
```text
2
3
4
5
```

range()函数默认将序列递增1，但是可以通过添加第三个参数range(2,10,3)来指定递增值：

例如：

用3递增序列（默认为1）：
```text
for x in range(2, 10, 3):
    print(x)
```
output:
```text
2
5
8
```

## 6、for循环语句中的else
for循环中的else关键字指定了循环结束时要执行的代码块:

例如：

打印输出从0到5的所有数字，并在循环结束时打印输出提示信息：
```text
for x in range(6):
    print(x)
else:
    print("Finally finished!")
```
output:
```text
0
1
2
3
4
5
Finally finished!
```

## 7、for循环嵌套
嵌套循环是循环中的循环。

“内层循环” 将针对 “外层循环” 的每次迭代执行一次：

例如：

打印输出列表：
```text
adj = ["red", "big", "tasty"]
langs = ["c", "java", "python"]

for x in adj:
    for y in langs:
        print(x, y)
```
output:
```text
red c
red java
red python
big c
big java
big python
tasty c
tasty java
tasty python
```

## 8、for循环中pass
for循环不能为空，但是如果出于某种原因，for循环不包含任何内容，需要使用pass语句，可以避免执行报错。

例如：
```text
for x in [0, 1, 2]:
    pass
```
