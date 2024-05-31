#Python while循环语句

Python 编程中 while 语句用于循环执行程序，
即在某条件下，循环执行某段程序，以处理需要重复处理的相同任务,执行语句可以是单个语句或语句块。
判断条件可以是任何表达式，任何非零、或非空（null）的值均为true。
当判断条件假 false 时，循环结束。

本文主要介绍一下 Python中while循环的使用。

## 1、 Python 循环语句
Python有两个原始循环命令：
- while循环
- for循环

## 2、while 循环
使用while循环，只要条件为真，我们就可以执行语句。执行语句可以是单个语句或语句块。
判断条件可以是任何表达式，任何非零、或非空（null）的值均为true。当判断条件假 false 时，循环结束。

例如：

只要小于6，就执行print打印输出：
```text
i = 1
while i < 6:
    print(i)
    i += 1
```
output:
```text
1
2
3
4
5
```

注意：一定要增加i，否则循环将永远继续。

while循环要求相关变量已准备就绪，在此示例中，我们需要定义一个索引变量i，并将其设置为1。

## 3、break语句
使用break语句，即使while条件为true，我们也可以停止循环：

例如：

i等于3退出循环：
```text
i = 1
while i < 6:
    print(i)
    if i == 3:
        break
    i += 1
```
output:
```text
1
2
3
```

## 4、continue语句
使用continue语句，我们可以停止当前迭代，然后继续下一个迭代：

例如：

如果i等于3，则停止当前迭代，继续进行下一个迭代：
```text
i = 0
while i < 5:
    i += 1
    if i == 3:
        continue
    print(i)
```
output:
```text
1
2
4
5
```

## 5、else语句
使用else语句，当条件不再成立时，我们可以运行一次代码块：

例如：

条件为False时，打印输出一条消息：
```text
i = 1
while i < 6:
    print(i)
    i += 1
else:
    print("i 不小于 6")
```
output:
```text
1
2
3
4
5
i 不小于 6
```