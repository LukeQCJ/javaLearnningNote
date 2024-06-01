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

## 4、生成器表达式
生成器表达式与列表推导式使用相同，区别在于生成器表达式用小括号()。生成器表达式返回的是一个生成器迭代器 generator。

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
