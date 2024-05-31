# Python 元组

Python的元组与列表类似，不同之处在于元组的元素不能修改。元组使用**小括号**，列表使用**方括号**。

## 1、元组（Tuple）
元组是**有序**且**不可更改**的集合。在Python中，元组带有圆括号。

例如：

创建一个元组：
```text
thistuple = ("c", "java", "python")
print(thistuple)
```
output:
```text
('c', 'java', 'python')
```

## 2、访问元组中的值
可以通过在方括号内引用索引号来访问元组项：

例如：

打印元组中的第二项：
```text
thistuple = ("c", "java", "python")
print(thistuple[1])
```
output:
```text
java
```

**负索引**

负索引表示从最后开始，-1表示最后一项，-2表示倒数第二项，依此类推。

例如：

打印元组的最后一项：
```text
thistuple = ("c", "java", "python")
print(thistuple[-1])
```
output:
```text
python
```

**索引范围(切片)**

可以通过指定范围的起点和终点来指定索引范围。指定范围时，返回值将是带有指定项目的新元组。

例如：

返回第三，第四和第五项：
```text
thistuple = ("c", "java", "python", "cjavapy", "js", "linux", "docker")
print(thistuple[2:5])
```
output:
```text
('python', 'cjavapy', 'js')
```
注意：搜索将从索引2（包括）开始，到索引5（不包括）结束。

请记住，第一项的索引为0。

**负索引范围(切片)**

如果要从元组的末尾开始搜索，请指定负索引：

例如：

本示例将项目从索引-4（包括）返回到索引-1（排除）
```text
thistuple = ("c", "java", "python", "cjavapy", "js", "linux", "docker")
print(thistuple[-4:-1])
```
output:
```text
('cjavapy', 'js', 'linux')
```

## 3、改变元组的值
创建元组后，您将无法更改其值。元组是不变的，或者也称为不变。

```text
x = ("c", "java", "python")
x[1] = "c#"
print(x)
```
报错：
```text
TypeError: 'tuple' object does not support item assignment
```

但是有一种解决方法。可以将元组转换为列表，更改列表，然后将列表转换回元组。

例如：

将元组转换为列表即可进行更改：
```text
x = ("c", "java", "python")
y = list(x)
y[1] = "kiwi"
x = tuple(y)

print(x)
```
output:
```text
('c', 'kiwi', 'python')
```

## 4、循环遍历元组
可以使用for循环遍历元组项。

例如：

遍历项并打印输出值:
```text
thistuple = ("c", "java", "python")
for x in thistuple:
    print(x)
```
output:
```text
c
java
python
```

相关文档：Python for循环语句

## 5、判断元组中值是否存在
要确定元组中是否存在指定的值，请使用in关键字：

例如：

检查元组中是否存在"python"：
```text
thistuple = ("c", "java", "python")
if "python" in thistuple:
    print("Yes, 'python' is a programming language")
```
output:
```text
Yes, 'python' is a programming language
```

## 6、元组的长度
要确定一个元组有多少项，可以使用len()方法:

例如：

打印元组中的元素数量:
```text
thistuple = ("c", "java", "python")
print(len(thistuple))
```
output:
```text
3
```

## 7、元组添加元素
创建元组后，将无法向其添加元素。 元组不可更改。

例如：

不能向元组添加项:
```text
thistuple = ("c", "java", "python")
thistuple[3] = "c#" # 引发一个错误
print(thistuple)
```
报错：
```text
TypeError: 'tuple' object does not support item assignment
```

## 8、创建带有一个元素的元组
要创建仅包含一个项目的元组，必须在该项目后添加逗号，否则Python不会将其识别为元组。

例如：

一个值的元组，记住逗号：
```text
thistuple = ("python",)
print(type(thistuple))

# NOT a tuple
thistuple = ("python")
print(type(thistuple))
```
output:
```text
<class 'tuple'>
<class 'str'>
```

## 9、删除元组中的元素
注意：您不能删除元组中的元素。

元组不可更改，因此您无法从中删除元素，但可以完全删除元组：

例如：

del关键字可以完全删除元组：
```text
thistuple = ("c", "java", "python")
del thistuple
```

使用del关键字删除元组之后，不能在使用该变量，否则报错。
```text
thistuple = ("c", "java", "python")
del thistuple
print(thistuple)
```
报错：
```text
NameError: name 'thistuple' is not defined
```

## 10、连接两个元组
要连接两个或多个元组，可以使用 **+运算符**：

例如：

加入两个元组：
```text
tuple1 = ("a", "b" , "c")
tuple2 = (1, 2, 3)

tuple3 = tuple1 + tuple2
print(tuple3)
```
output:
```text
('a', 'b', 'c', 1, 2, 3)
```

## 11、元组的构造函数tuple()
也可以使用tuple()构造函数创建一个元组。

例如：

使用tuple()方法生成一个元组:
```text
thistuple = tuple(("c", "java", "python")) # 注意双括号
print(thistuple)
```
output:
```text
('c', 'java', 'python')
```

## 12、元组方法
Python有两个可在元组上使用的内置方法。

| 方法      | 描述                   |
|---------|----------------------|
| count() | 返回指定值在元组中出现的次数       |
| index() | 在元组中搜索指定的值，并返回找到它的位置 |

## 13、元组运算符
与字符串一样，元组之间可以使用 + 号和 * 号进行运算。这就意味着他们可以组合和复制，运算后会生成一个新的元组。

| Python 表达式	                    | 结果	                           | 描述     |
|--------------------------------|-------------------------------|--------|
| len((1, 2, 3))	                | 3	                            | 计算元素个数 |
| (1, 2, 3) + (4, 5, 6)	         | (1, 2, 3, 4, 5, 6)	           | 连接     |
| ('Hi!',) * 4	                  | ('Hi!', 'Hi!', 'Hi!', 'Hi!')	 | 复制     |
| 3 in (1, 2, 3)	                | True	                         | 元素是否存在 |
| for x in (1, 2, 3): print(x),	 | 1 2 3	                        | 迭代     |


