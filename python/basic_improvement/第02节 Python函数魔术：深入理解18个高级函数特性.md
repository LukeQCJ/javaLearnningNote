探索那些让代码瞬间变得优雅而强大的高级函数。准备好，让我们一起揭开它们的神秘面纱吧！

## 1. map()：一招制胜，批量操作
想象一下，你需要给一个数字列表的每个元素加上5。普通的循环是不是让你觉得有点乏味？map()函数来救援！
```text
def add_five(x):
    return x + 5


numbers = [1, 2, 3]
# 使用map函数
result = map(add_five, numbers)
# result = map(lambda n: n + 5, numbers)
print(list(result))
```
output:
```text
[6, 7, 8]
```
魔法揭秘：map()接受一个函数和一个（或多个） iterable，对iterable中的每个元素应用该函数，返回一个迭代器。

## 2. filter()：慧眼识珠，筛选高手
有了filter()，你可以轻松地从一堆数据中挑出符合条件的宝藏。
```text
def is_even(n):
    return n % 2 == 0


# 筛选出偶数
numbers = [1, 2, 3, 4, 5]
filtered_numbers = filter(is_even, numbers)
# filtered_numbers = filter(lambda n: n % 2 == 0, numbers)
print(list(filtered_numbers))
```
output:
```text
[2, 4]
```
魔法揭秘：它接受一个函数和一个 iterable，仅保留使函数返回True的元素。

## 3. reduce()：聚沙成塔，累积计算
这个函数在Python标准库的functools里，它能将一个列表的所有元素通过一个函数累积起来，比如求和。
```text
from functools import reduce


def add_int(x, y):
    return x + y


numbers = [1, 2, 3, 4]
# 求和
sum_it = reduce(add_int, numbers)
# sum_it = reduce(lambda x, y: x + y, numbers)
print(sum_it)
```
output:
```text
10
```
魔法揭秘：reduce()使用一个二元操作符（这里是一个匿名函数lambda），连续作用于序列的元素，最终得到一个结果。

## 4. 列表推导式：一行代码，千行功效
列表推导式是Python的效率神器，简洁到让人惊叹！
```text
squares = [x**2 for x in range(1, 6)]  # 生成1到5的平方数
print(squares)  
```
output:
```text
[1, 4, 9, 16, 25]
```
魔法揭秘：它将循环和条件判断压缩成一行，快速创建新列表。

## 5. 字典推导式：字典速造，清晰无比
字典推导式同样强大，用于快速构建字典。
```text
words = ['apple', 'banana', 'cherry']
word_lengths = {word: len(word) for word in words}
print(word_lengths)
```
output:
```text
{'apple': 5, 'banana': 6, 'cherry': 6}
```
魔法揭秘：基于旧的iterable，快速创建新的字典键值对。

## 6. 高阶函数：函数也可以当参数
Python允许函数作为参数传递给另一个函数，这开启了无限可能。
```text
def apply(func, x):
    return func(x)


print(apply(lambda x: x * 2, 5))
```
output:
```text
10
```
魔法揭秘：高阶函数提高了代码的灵活性，让抽象层次更上一层楼。

## 7. zip()：并驾齐驱，打包专家
当你想同时遍历两个（或多个）列表时，zip()就是你的最佳拍档。
```text
names = ['Alice', 'Bob', 'Charlie']
ages = [24, 30, 18]

pairs = zip(names, ages)

for name, age in pairs:
    print(f"{name} is {age} years old.")
```
output:
```text
Alice is 24 years old.
Bob is 30 years old.
Charlie is 18 years old.
```
魔法揭秘：它接收多个 iterable，并将对应位置的元素组合成一个元组，返回一个迭代器。

## 8. enumerate()：索引与值，一网打尽
遍历的同时获取元素的索引？非enumerate莫属。
```text
fruits = ['apple', 'banana', 'mango']
for index, fruit in enumerate(fruits):
    print(f"#{index}: {fruit}")
```
output:
```text
#0: apple
#1: banana
#2: mango
```
魔法揭秘：它将可迭代对象转换为枚举对象，每次迭代返回当前的索引和值。

## 9. set()与集合操作：去重高手，交并差集简便🛠
快速去除重复元素，或者进行集合运算，set是不二之选。
```text
a = [1, 2, 3, 4]
b = [3, 4, 5, 6]
unique_a = set(a)
union_set = set(a).union(set(b))
print(unique_a, union_set)
```
output:
```text
{1, 2, 3, 4} {1, 2, 3, 4, 5, 6}
```
魔法揭秘：集合支持并集(union)、交集(intersection)、差集等操作，适用于去重和集合逻辑处理。

## 10. any()与all()：逻辑判断，一目了然
检查列表中是否存在至少一个True值？或者所有都是True？它们俩是你的得力助手。
```text
numbers = [0, 1, 2]
print(any(numbers > 0))  # 输出：True
print(all(numbers > 0))  # 输出：False
```
魔法揭秘：any()只要有一个元素满足条件就返回True，all()需要所有元素都满足条件才返回True。

## 11. 装饰器：不动声色，功能增强
装饰器让你可以在不修改原函数代码的情况下，给函数添加新功能。
```text
def my_decorator(func):
    def wrapper():
        print("Something is happening before the function is called.")
        func()
        print("Something is happening after the function is called.")

    return wrapper


@my_decorator
def say_hello():
    print("Hello!")


say_hello()
```
output:
```text
Something is happening before the function is called.
Hello!
Something is happening after the function is called.
```
魔法揭秘：装饰器本质上是一个函数，它接收一个函数作为参数，并返回一个新的函数。

## 12. 生成器：按需生产，内存友好
生成器是一种特殊的迭代器，使用yield关键字，懒加载数据，超级节省内存。
```text
def count_up_to(n):
    count = 1
    while count <= n:
        yield count
        count += 1


def count_up_to2(n):
    res = []
    count = 1
    while count <= n:
        res.append(count)
        count += 1
    return res


# 生成器
lst = count_up_to(5)
print(lst)
for num in count_up_to(5):
    print(num)
# 集合
lst = count_up_to2(5)
print(lst)
for num in count_up_to2(5):
    print(num)
```
output:
```text
<generator object count_up_to at 0x000001F3D5EB5E50>
1
2
3
4
5
[1, 2, 3, 4, 5]
1
2
3
4
5
```
魔法揭秘：每当迭代时，生成器的代码只执行到下一个yield语句，暂停并返回值，下次迭代时继续执行。

## 13. 上下文管理器：资源管理，自动善后
用with语句管理资源，如文件打开关闭，自动化的异常处理，干净又安全。
```text
with open('example.txt', 'r') as file:
    content = file.read()
    print(content)
```
魔法揭秘：上下文管理器定义了__enter__和__exit__方法，自动处理进入和退出代码块时的操作。

## 14. 断言：代码自检，错误早发现
在代码中放置断言，帮助你在开发阶段发现逻辑错误。
```text
def divide(a, b):
    assert b != 0, "除数不能为0"
    return a / b


print(divide(10, 2))
# 尝试除以0会抛出异常
print(divide(10, 0))
```
output:
```text
5.0
Traceback (most recent call last):
  File "D:\apps\PyCharm\installation\PyCharm Community Edition 2024.1.1\plugins\python-ce\helpers\pydev\pydevd.py", line 1535, in _exec
    pydev_imports.execfile(file, globals, locals)  # execute the script
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "D:\apps\PyCharm\installation\PyCharm Community Edition 2024.1.1\plugins\python-ce\helpers\pydev\_pydev_imps\_pydev_execfile.py", line 18, in execfile
    exec(compile(contents+"\n", file, 'exec'), glob, loc)
  File "D:\pythonProject\ANN_base\basic_improvement.py", line 8, in <module>
    print(divide(10, 0))
          ^^^^^^^^^^^^^
  File "D:\pythonProject\ANN_base\basic_improvement.py", line 2, in divide
    assert b != 0, "除数不能为0"
           ^^^^^^
AssertionError: 除数不能为0
python-BaseException
```
魔法揭秘：assert用于测试某个条件是否为真，如果条件为假，则引发AssertionError异常。

## 15. 解包操作：一键分配，简单高效
解包操作能将序列或集合的元素分配给对应的变量，反之亦然。
```text
a, b, c = (1, 2, 3)  # 序列解包
print(a, b, c)

nums = [4, 5, 6]
*x, = nums  # 星号解包，收集剩余元素
print(x)
```
output:
```text
1 2 3
[4, 5, 6]
```
魔法揭秘：解包操作简化了变量赋值和函数参数传递，使代码更加直观。

# 进阶与高级
## 16. itertools模块：迭代器的乐园
itertools是Python的标准库之一，提供了很多高效处理迭代器的工具。

### 组合生成：product与combinations
**product**：生成笛卡尔积。
```text
from itertools import product

letters = ['a', 'b']
numbers = [1, 2]
print(list(product(letters, numbers)))
```
output:
```text
[('a', 1), ('a', 2), ('b', 1), ('b', 2)]
```

**combinations**：生成不重复的组合。
```text
print(list(combinations(letters, 2)))  
```
output:
```text
[('a', 'b')]
```

### 无限迭代：count, cycle
**count**：从指定起始值开始无限递增。
```text
import itertools

for i in itertools.count(10):
    print(i) # 打印从10开始的无限序列，实际使用时应有限制条件
```

**cycle**：无限重复序列。
```text
import itertools

for i in itertools.cycle('AB'):
    print(i)  # 无限循环打印'A', 'B'
```

## 17. contextlib：上下文管理的扩展
contextlib提供了更灵活的方式来创建和使用上下文管理器。

### 自定义资源管理
```text
from contextlib import contextmanager


@contextmanager
def managed_file(name):
    file = None
    try:
        file = open(name, 'r')
        yield file
    finally:
        print("执行finally")
        if file is not None:
            file.close()


with managed_file('example.txt') as f:
    for line in f:
        print(line)
```
### 闭包上下文：contextmanager装饰器
上面的例子展示了如何使用装饰器来简化上下文管理器的编写，这对于临时性管理资源非常有用。

## 18. 软件设计模式：工厂与策略模式
Python的高级函数特性非常适合实现设计模式，例如：

工厂模式的简单实现
```text
def factory(shape='circle'):
    shape_dict = {
        'circle': lambda: "I am a circle",
        'square': lambda: "I am a square"
    }
    return shape_dict.get(shape, lambda: "Shape not found")()


print(factory('circle'))  # 输出：I am a circle
```
策略模式：动态选择算法
```text
def sort_strategy(sort_type, data):
    strategies = {
        'bubble': lambda i: sorted(data),
        'quick': lambda i: sorted(data, key=lambda x: x)  # 简化示例，实际应实现快速排序
    }
    return strategies.get(sort_type, lambda m: "Invalid sort type")(data)


lst = [3, 1, 4, 1, 5]
print(sort_strategy('bubble', lst))  # 输出：[1, 1, 3, 4, 5]
```
结语
Python的魔法远远不止于此，每一次深入探索都能发现新的惊喜。
通过这些高级特性和设计模式的应用，你的代码将变得更加优雅、高效。