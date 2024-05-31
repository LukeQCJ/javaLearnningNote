# Python 列表(List)

序列是Python中最基本的数据结构。
序列中的每个元素都分配一个数字 - 它的位置，或索引，第一个索引是0，第二个索引是1，依此类推。

Python有6个序列的内置类型，但最常见的是列表和元组。
序列都可以进行的操作包括索引，切片，加，乘，判断成员。

此外，Python已经内置确定序列的长度以及确定最大和最小的元素的方法。

列表是最常用的Python数据类型，它可以作为一个方括号内的逗号分隔值出现。列表的数据项不需要具有相同的类型。

## 1、Python Collections (Arrays)
Python编程语言中有四种容器数据类型：
- 列表(List)是一个有序且可更改的集合。允许重复的成员。
- 元组(Tuple)是一个有序且不可更改的集合。允许重复的成员。
- 集合(Set)是无序且未编制索引的集合。没有重复的成员。
- 字典(Dictionary)是无序，可变且已编入索引的集合。没有重复的成员。

选择集合(collection)类型时，了解该类型的属性很有用。 
为特定数据集选择正确的类型可能意味着保留含义，并且可能意味着效率或安全性的提高。

## 2、List(列表)
列表是有序且可更改的集合。 在Python中，列表用方括号括起来。

例如：

创建一个列表：
```text
thislist = ["c", "java", "python"]
print(thislist)
```
结果：
```text
['c', 'java', 'python']
```

## 3、访问list列表中元素
通过引用索引号访问列表项：

例如：

打印列表的第二项：
```text
thislist = ["c", "java", "python"]
print(thislist[1])
```
结果：
```text
java
```

**负索引**
```text
负索引表示从最后开始，-1表示最后一项，-2表示倒数第二项，依此类推。
```

例如：

打印列表的最后一项：
```text
thislist = ["c", "java", "python"]
print(thislist[-1])
```
output:
```text
python
```

**索引范围(切片)**

可以通过指定范围的起点和终点来指定索引范围。指定范围时，返回值将是包含指定项目的新列表。

例如：

返回第三，第四和第五项：
```text
thislist = ["c", "java", "python", "cjavapy", "js", "linux", "docker"]
print(thislist[2:5])
```
output:
```text
['python', 'cjavapy', 'js']
```
注意：搜索将从索引2（包括）开始，到索引5（不包括）结束。

请记住，第一项的索引为0。

通过省略起始值，范围将从第一项开始：

例如：

本示例将项目从开头返回到"cjavapy"：
```text
thislist = ["c", "java", "python", "cjavapy", "js", "linux", "docker"]
print(thislist[:4])
```
output:
```text
['c', 'java', 'python', 'cjavapy']
```

通过省略结束值，范围将继续到列表的末尾：

例如：

本示例输出从"python"到列表的末尾：
```text
thislist = ["c", "java", "python", "cjavapy", "js", "linux", "docker"]
print(thislist[2:])
```
output:
```text
['python', 'cjavapy', 'js', 'linux', 'docker']
```

**负索引范围(切片)**

如果要从列表末尾开始搜索，请指定负索引：

例如：

本示例将项目从索引-4（包括）返回到索引-1（不包括）
```text
thislist = ["c", "java", "python", "cjavapy", "js", "linux", "docker"]
print(thislist[-4:-1])
```
output:
```text
['cjavapy', 'js', 'linux']
```

## 4、改变列表中元素的值
例如：

更改第二个元素：
```text
thislist = ["c", "java", "python", "cjavapy", "js", "linux", "docker"]
thislist[1] = "c#"
print(thislist)
```
output:
```text
['c', 'c#', 'python', 'cjavapy', 'js', 'linux', 'docker']
```

## 5、循环遍历列表(List)
可以使用for循环遍历列表项：

例如：

一张一张打印列表中的所有元素：
```text
thislist = ["c", "java", "python"]
for x in thislist:
    print(x)
```
output:
```text
c
java
python
```

相关文档：Python for循环语句

## 6、判断列表中元素是否存在
要确定列表中是否存在指定的元素，请使用in关键字：

例如：

检查列表中是否存在"python"：
```text
thislist = ["c", "java", "python"]
if "python" in thislist:
    print("Yes, 'python' is a programming language")
```
output:
```text
Yes, 'python' is a programming language
```

## 7、列表生成器
当您要基于现有列表的值创建新列表时，列表生成器表达式提供了一种较短的语法。

例如：
```text
langs = ["c", "java", "python", "linux", "docker"]
newlist = []

for x in langs:
    if "a" in x:
        newlist.append(x)

print(newlist)
```
output:
```text
['java']
```

使用**列表生成器**，只需一行代码即可完成所有这些工作：

例如：
```text
langs = ["c", "java", "python", "linux", "docker"]

newlist = [x for x in langs if "a" in x]

print(newlist)
```
output:
```text
['java']
```

列表生成器用**方括号**括起来，包含一个或多个for语句，零个或多个if语句，并返回一个新列表。

## 8、List列表长度
要确定列表中有多少元素，请使用len()函数：

例如：

打印列表中的元素数：
```text
thislist = ["c", "java", "python"]
print(len(thislist))
```

## 9、列表中添加元素
要将元素添加到列表的末尾，请使用append()方法：

例如：

使用append()方法添加元素：
```text
thislist = ["c", "java", "python"]
thislist.append("cjavapy")
print(thislist)
```
output:
```text
['c', 'java', 'python', 'cjavapy']
```

要在指定的索引处添加元素，请使用insert()方法：

例如：

插入一个元素作为第二个位置：
```text
thislist = ["c", "java", "python"]
thislist.insert(1, "cjavapy")
print(thislist)
```
output:
```text
['c', 'cjavapy', 'java', 'python']
```

## 10、删除列表中元素
有几种方法可以从列表中删除元素：

例如：

**remove()方法**删除指定的元素：
```text
thislist = ["c", "java", "python"]
thislist.remove("java")
print(thislist)
```
output:
```text
['c', 'python']
```

例如：

**pop()方法**将删除指定的索引（如果未指定index，则删除最后一项）：
```text
thislist = ["c", "java", "python"]
thislist.pop()
print(thislist)
```
output:
```text
['c', 'java']
```

```text
thislist = ["c", "java", "python"]
thislist.pop(1) # 指定索引删除
print(thislist)
```
output:
```text
['c', 'python']
```

例如：

**del关键字**删除指定的索引：
```text
thislist = ["c", "java", "python"]
del thislist[0]
print(thislist)
```
output:
```text
['java', 'python']
```

例如：

**del关键字**也可以完全删除列表：
```text
thislist = ["c", "java", "python"]
del thislist
```
如果del关键字删除了整个列表，相当于在整个内存空间中删除了thislist变量，该变量已经不存在了，不能使用。

如果使用已经被del的变量，则会报错，例如：
```text
thislist = ["c", "java", "python"]
del thislist
print(thislist)
```
报错：
```text
NameError: name 'thislist' is not defined
```

例如：

**clear()方法**清空列表：
```text
thislist = ["c", "java", "python"]
thislist.clear()
print(thislist)
```
output:
```text
[]
```

## 11、复制list列表
不能简单地通过输入list2 = list1来复制列表，因为list2只是对list1的引用，
对list1所做的更改也会自动地对list2进行更改。

有很多方法可以制作副本，一种方法是使用内置的List方法copy()。

例如：

使用**copy()方法**制作列表的副本：
```text
thislist = ["c", "java", "python"]
mylist = thislist.copy()
thislist[1] = "c#"
print(mylist)
print(thislist)
```
output:
```text
['c', 'java', 'python']
['c', 'c#', 'python']
```

另一种复制方法是使用内置方法list()。

例如：

使用**list()方法**复制列表：
```text
thislist = ["c", "java", "python"]
mylist = list(thislist)
thislist[1] = "c#"
print(mylist)
print(thislist)
```
output:
```text
['c', 'java', 'python']
['c', 'c#', 'python']
```

## 12、join连接两个list列表
在Python中，有几种方法可以连接或连接两个或多个列表。

最简单的方法之一是**使用+运算符**。

例如：

连接两个列表：
```text
list1 = ["a", "b" , "c"]
list2 = [1, 2, 3]

list3 = list1 + list2
print(list3)
```
output:
```text
['a', 'b', 'c', 1, 2, 3]
```

联接两个列表的另一种方法是将list2中的所有项一个接一个地**追加**到list1中：

例如：

将list2追加到list1中：
```text
list1 = ["a", "b" , "c"]
list2 = [1, 2, 3]

for x in list2:
    list1.append(x)

print(list1)
```
output:
```text
['a', 'b', 'c', 1, 2, 3]
```

或者，可以使用**extend()方法**，其目的是将一个列表中的元素添加到另一列表中：

例如：

使用extend()方法将list2添加到list1的末尾：
```text
list1 = ["a", "b" , "c"]
list2 = [1, 2, 3]

list1.extend(list2)
print(list1)
```
output:
```text
['a', 'b', 'c', 1, 2, 3]
```

## 13、list()构造函数
也可以使用list()构造函数创建一个新列表。

例如：

使用list()构造函数创建一个列表：
```text
thislist = list(("c", "java", "python")) # 注意双括号
print(thislist)
```
output:
```text
['c', 'java', 'python']
```

## 14、list列表方法
Python有一组内置方法，您可以在列表上使用它们。

| 方法        | 描述                       |
|-----------|--------------------------|
| append()  | 在列表末尾添加元素                |
| clear()   | 从列表中删除所有元素               |
| copy()    | 返回列表的副本                  |
| count()   | 返回具有指定值的元素数              |
| extend()  | 将列表（或任何可迭代）的元素添加到当前列表的末尾 |
| index()   | 返回具有指定值的第一个元素的索引         |
| insert()  | 在指定位置添加元素                |
| pop()     | 删除指定位置的元素                |
| remove()  | 删除具有指定值的项目               |
| reverse() | 颠倒列表的顺序                  |
| sort()    | 排序列表                     |

例如：
```text
thislist = list(("c", "java", "python", "c#")) # 注意双括号
# 排序
thislist.sort()
print(thislist)

# 倒序
thislist.reverse()
print(thislist)
```
output:
```text
['c', 'c#', 'java', 'python']
['python', 'java', 'c#', 'c']
```