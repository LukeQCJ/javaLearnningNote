# Python 列表、字典和集合推导式及生成器表达式

Python 推导式 comprehensions（又称解析式），**推导式**是 Python 内置的非常简单却强大的可以用来创建列表、字典和集合的语法。

生成器表达式并不真正的创建，而是返回一个生成器对象，此对象在每次计算出一个条目后，把这个条目“产生”(yield)出来。
**生成器表达式使用了“惰性计算”或称作“延时求值”的机制**。
**生成器表达式可以用来处理大数据文件**。
生成器表达式产生的是一个生成器对象，写法简单而优雅，实质就是迭代器。

本文主要介绍 Python中列表、字典和集合推导式及生成器表达式的使用。

## 1、列表推导式
列表推导式写法简单而优雅，可以将多行代码融合成一行。主要是将其他对象转换成列表或对原来的列表进行过滤。

语法
```text
[expression for iter_val in iterable]
```
或
```text
[expression for iter_val in iterable if cond_expr]
```

例如，
```text
list_comp1 = [x ** 2 for x in range(10)]
print(list_comp1)

list_comp2 = [x ** 2 for x in range(10) if x % 2 == 0]
print(list_comp2)
```
output:
```text
[0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
[0, 4, 16, 36, 64]
```

## 2、字典推导式
字典推导式写法简单而优雅，可以将多行代码融合成一行。主要是将其他对象转换成字典或对原来的字典进行过滤。

语法
```text
{expression for iter_key,iter_val in iterable}
```
或
```text
{expression for iter_key,iter_val in iterable if cond_expr}
```

例如，
```text
my_dict = {'C': 22, 'Java': 24, 'Python': 28, 'Linux': 20}

dict_comp1 = {key: key + "_" + str(value) for key, value in my_dict.items()}
print(dict_comp1)

dict_comp2 = {key: key + "_" + str(value) for key, value in my_dict.items() if value > 20}
print(dict_comp2)
```
output:
```text
{'C': 'C_22', 'Java': 'Java_24', 'Python': 'Python_28', 'Linux': 'Linux_20'}
{'C': 'C_22', 'Java': 'Java_24', 'Python': 'Python_28'}
```

## 3、集合推导式
集合推导式写法简单而优雅，可以将多行代码融合成一行。主要是将其他对象转换成集合或对原来的集合进行过滤。

语法
```text
{expression for iter_val in iterable}
```
或
```text
{expression for iter_val in iterable if cond_expr}
```

例如，
```text
squared1 = {x ** 2 for x in [1, 1, 2, 3, 4, 5, 5]}
print(squared1)
squared2 = {x ** 2 for x in [1, 1, 2, 3, 4, 5, 5] if x < 5}
print(squared2)
```
output:
```text
{1, 4, 9, 16, 25}
{16, 1, 4, 9}
```

## 4、迭代器
【可迭代对象】：存储了元素的一个容器对象，实现了__iter__魔术方法。

【迭代器对象】：实现了__iter__和__next__魔术方法的对象称为迭代器对象。

其中，__iter__方法返回自身（迭代器对象）；__next__方法返回下一个值。

如：
```text
str1 = "abc"
iterator_str1 = str1.__iter__()
print(iterator_str1.__next__())
print(iterator_str1.__next__())
print(iterator_str1.__next__())
```
output:
```text
a
b
c
```

**for语法糖**
```text
1）for先调用可迭代对象的__iter__方法，该方法返回一个迭代器

2）再对迭代器对象调用__next__方法，不断获取下一个值

3）直到获取完成抛出StopIteration异常退出
```
迭代器的优点：懒加载，惰性求值，需要的时候生成，可以节省空间。

例：使用迭代器实现阶乘
```text
class factorial:
    def __init__(self, stop, num=1, count=1):
        self.num = num
        self.stop = stop
        self.count = count

    def __iter__(self):
        return self

    def __next__(self):
        if self.count > self.stop:
            raise StopIteration
        self.num = self.num * self.count
        self.count += 1
        return self.num


f = factorial(5)
for i in f:
    print(i)
```
运行结果：输出1-5的阶乘
```text
1
2
6
24
120
```

## 5、生成器
生成器是迭代器更加优雅的写法，不需要手动实现__iter__和__next__方法。
生成器可以用更少的代码来实现迭代器的效果，相比于容器类型更加节省内存。

生成器有两种写法：一种叫【生成器表达式】，另一种叫【生成器函数】。

【生成器表达式】与列表推导式使用相同，区别在于生成器表达式用小括号()。生成器表达式返回的是一个生成器迭代器 generator。

【生成器函数】是写一个函数，利用用yield关键字，产生一个结果返回。

语法
```text
(expression for iter_val in iterable)
```
或
```text
(expression for iter_val in iterable if cond_expr)
```

例如，
```text
gen_exp1 = (x ** 2 for x in range(10))
print(gen_exp1)
print(tuple(gen_exp1))  # 需要获得结果可以转成list或tuple

gen_exp2 = (x ** 2 for x in range(10) if x % 2 == 0)
print(gen_exp2)
print(list(gen_exp2))  # 需要获得结果可以转成list或tuple
```
output:
```text
<generator object <genexpr> at 0x000002005F9723C0>
(0, 1, 4, 9, 16, 25, 36, 49, 64, 81)
<generator object <genexpr> at 0x000002005F972660>
[0, 4, 16, 36, 64]
```

### 生成器原理
生成器的原理与功能可以分为以下几个核心点：

```text
1）迭代行为：
    Python中的生成器作为一种特殊的迭代器，实现了迭代器协议，即它们有一个__next__()的方法。
    这允许生成器逐个产生序列中的元素。
2）状态挂起：
    当生成器函数执行到yield语句时，它会返回一个值给调用者，并且暂停其自身的状态（包括局部变量、指令指针等）。
    在下一次调用__next__()方法时，它会从上次暂停的地方继续执行，而不是从头开始执行函数。
3）内存效率：
    普通函数一次性计算并返回所有结果，通常需要存储整个结果集合，这对内存是一种负担。
    生成器函数则是按需计算，每次只产生一个结果，所以非常节省内存。
4）代码简洁：
    生成器提供了一种简便的方法来实现迭代器而无需定义一个类来实现__iter__()和__next__()方法，使得代码更加简洁。
```

下面是一个简单的生成器函数的例子，例如：
```text
def simple_generator():
    yield 1
    yield 2
    yield 3

# 生成器函数被调用时，返回一个生成器对象
gen = simple_generator()

# 通过迭代器协议来访问元素
print(next(gen))  # 输出 1
print(next(gen))  # 输出 2
print(next(gen))  # 输出 3
# 再次调用会引发 StopIteration 异常，因为生成器中没有更多元素
```
生成器背后的原理使它们非常适合于需要延迟处理的数据流、大数据集处理以及管道（pipeline）的场景。
例如，你可以用生成器来逐行读取一个非常大的文件，而不是一次性将其全部加载到内存中。
这提高了程序的性能并减少了内存使用。

当生成器函数执行到yield语句时，会发生以下几件事：
```text
1）返回值：
    执行到yield语句时，生成器函数返回yield后面表达式的值给生成器的调用者。这个过程被称为产出值（yielding a value）。
2）函数状态挂起：
    生成器函数暂停执行其余的代码，并且保存当前的执行状态（包括局部变量，指令指针等）。
    这样，在下一次使用next()函数或在for循环中请求下一个值时，生成器可以准确无误地从上次暂停的地方恢复执行。
3）等待下一次激活：
    一旦产出了一个值并暂停，生成器函数会等待，直到外部再次调用next()函数或通过其他方式请求下一个值，
    比如将生成器对象用在for循环中。
4）继续执行：
    当生成器函数再次被激活时，它会从上次yield暂停的位置继续执行，而不是从头开始执行函数。
    如果达到了另一个yield表达式，则重复上述过程。
    如果没有更多的yield，则继续执行直到函数结束。
5）函数结束：
    如果生成器函数正常执行完毕，再次调用next()函数时，它将触发一个StopIteration异常。
    异常表明所有值都已经产出，没有更多的值了。
```
通过这种方式，生成器函数允许逐个地产出系列值，而不是立即产生所有值占用大量内存。
这种按需计算和值的产出方式使得生成器非常适合处理大型或无限的数据集合。

例如：
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
打印结果可以看出是一个生成器对象，而不是一个元组！
注意，在Python中有【列表推导式】、【字典推导式】、【集合推导式】，唯独没有元组推导式，
一是因为元组本身具备不可变性，二是圆括号语法被生成器推导式占用了。

