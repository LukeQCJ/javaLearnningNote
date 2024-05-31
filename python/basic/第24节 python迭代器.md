# Python 迭代器(Iterator)

什么是迭代器？
它是一个带状态的对象，在你调用next()方法的时候返回容器中的下一个值，
任何实现了__iter__和__next__()（python2中实现next()）方法的对象都是迭代器，
__iter__返回迭代器自身，__next__返回容器中的下一个值，
如果容器中没有更多元素了，则抛出StopIteration异常。可迭代对象实现了__iter__方法，该方法返回一个迭代器对象，
本文主要介绍一下Python中迭代器(Iterator)。

## 1、Python 迭代器(Iterator)
迭代器是一个包含数个值的对象。

迭代器是可以迭代的对象，这意味着您可以遍历所有值。

从技术上讲，在Python中，迭代器是实现迭代器协议的对象，该协议由方法__iter__()和__next__()组成。

例如：

迭代一个元组中元素：
```text
my_tuple = ("c", "python", "java")

for x in my_tuple:
    print(x)
```

## 2、迭代器(Iterator)和可迭代(Iterable)
列表，元组，字典和集合都是可迭代的对象。它们是可迭代的容器，可以从中获得迭代器。

所有这些对象都有一个iter()方法，该方法用于获取迭代器：

例如：

从元组返回一个迭代器，并输出每个值:
```text
my_tuple = ("c", "java", "python")
my_it = iter(my_tuple)

print(next(my_it))
print(next(my_it))
print(next(my_it))
```
output:
```text
c
java
python
```

字符串也是可迭代对象，可以返回迭代器:

例如：

字符串可以当作字符列表：
```text
my_str = "python"
my_it = iter(my_str)

print(next(my_it))
print(next(my_it))
print(next(my_it))
print(next(my_it))
print(next(my_it))
print(next(my_it))
```
output:
```text
p
y
t
h
o
n
```

## 3、遍历迭代器
我们还可以使用for循环来迭代可迭代对象：

例如：

迭代一个元组的值：
```text
my_tuple = ("c", "java", "python")

for x in my_tuple:
    print(x)
```

例如：

迭代字符串的字符：
```text
my_str = "banana"

for x in my_str:
    print(x)
```

for循环实际上创建了一个迭代器对象，并为每个循环执行next()方法。

## 4、定义一个迭代器
要创建一个对象/类作为迭代器，必须实现__iter__()和__next__()方法。

正如在Python 类和对象一章中所了解的那样，所有类都有一个名为__init__()的函数，该函数可以在创建对象时进行一些初始化。

__iter__()方法的行为类似，可以执行操作（初始化等），但必须始终返回迭代器对象本身。

__next__()方法还允许你进行操作，并且必须返回序列中的下一项。

例如：

创建一个返回数字（从1开始）的迭代器，每个序列将增加一个（返回1,2,3,4,5等）：
```text
class MyNumbers:
    def __iter__(self):
        self.a = 1
        return self

    def __next__(self):
        x = self.a
        self.a += 1
        return x


my_class = MyNumbers()
my_iter = iter(my_class)

print(next(my_iter))
print(next(my_iter))
print(next(my_iter))
print(next(my_iter))
print(next(my_iter))
```
output:
```text
1
2
3
4
5
```

## 5、StopIteration
如果有足够的next()语句，或者如果在for循环中使用了该语句，则以上示例将永远继续下去。

为了防止迭代永远进行，我们可以使用StopIteration语句。

在__next__()方法中，如果迭代执行了指定的次数，我们可以添加终止条件以引发错误：

例如：

在10次迭代后停止：
```text
class MyNumbers:
    def __iter__(self):
        self.a = 1
        return self

    def __next__(self):
        if self.a <= 10:
            x = self.a
            self.a += 1
            return x
        else:
            raise StopIteration


my_class = MyNumbers()
my_iter = iter(my_class)

for x in my_iter:
    print(x)
```
output:
```text
1
2
3
4
5
6
7
8
9
10
```

迭代列表：
```text
# 首先获得Iterator对象:
it = iter([1, 2, 3, 4, 5])
# 循环:
while True:
    try:
        # 获得下一个值:
        x = next(it)
        print(x)
    except StopIteration:
        # 遇到StopIteration就退出循环
        break
```
output:
```text
1
2
3
4
5
```
