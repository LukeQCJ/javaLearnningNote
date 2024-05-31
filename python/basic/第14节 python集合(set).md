# Python 集合(set)

Python的set和其他语言类似, 是一个无序不重复元素集, 基本功能包括关系测试和消除重复元素. 
集合对象还支持union(联合), intersection(交), difference(差)和sysmmetric difference(对称差集)等数学运算。

## 1、集合(set)
集合是无序且无索引的集合。 在Python中，集合用大括号括起来。

例如：

创建一个集合：
```text
thisset = {"c", "java", "python"}
print(thisset)
```
output:
```text
{'c', 'python', 'java'}
```

注意：集合是无序的，因此您不能确定的元素将以什么顺序出现。

## 2、访问集合中元素
不能通过引用索引或键来访问集合中的项目。

但是，可以使用for循环遍历设置项，或者通过使用in关键字询问集合中是否存在指定值。

例如：

遍历集合，打印输出值:
```text
thisset = {"c", "java", "python"}

for x in thisset:
    print(x)
```
output:
```text
python
c
java
```
或
```text
c
python
java
```
每次输出的顺序是不一定相同的。

例如：

判断集合中是否存在"python"：
```text
thisset = {"c", "java", "python"}

print("c" in thisset)
```
output:
```text
True
```

## 3、修改集合元素
创建集后，您将无法更改其项目，但可以添加新项目。

## 4、添加元素
要将一个项目添加到集合中，请使用add()方法。

要向一个集合中添加多个项目，请使用update()方法。

例如：

使用add()方法将项目添加到集合中：
```text
thisset = {"c", "java", "python"}
thisset.add("c#")
print(thisset)
```
output:
```text
{'python', 'c', 'c#', 'java'}
```
或
```text
{'python', 'c#', 'c', 'java'}
```
每次输出的顺序是不一定相同的。

例如：

使用update()方法将多个项目添加到集合中：
```text
thisset = {"c", "java", "python"}
thisset.update(["js", "cjavapy", "linux"])
print(thisset)
```
output:
```text
{'cjavapy', 'linux', 'java', 'python', 'js', 'c'}
```
或
```text
{'cjavapy', 'java', 'python', 'js', 'linux', 'c'}
```
每次输出的顺序是不一定相同的。

## 5、判断集合中是否存在指定元素
要确定集合中是否存在指定元素，使用in关键字：

例如：

判断集合中是否存在“python”：
```text
thisset = {"c", "java", "python"}

if "python" in thisset:
    print("python是集合中的元素")
```
output:
```text
python是集合中的元素
```

## 6、获取集合的长度
要确定集合中有多少项，请使用len()方法。

例如：

获取集合中的项目数：
```text
thisset = {"c", "java", "python"}
print(len(thisset))
```
output:
```text
3
```

## 7、删除集合中元素
要删除集合中的项目，请使用remove()或discard()方法。

例如：

使用remove()方法删除"java"：
```text
thisset = {"c", "java", "python"}
thisset.remove("java")
print(thisset)
```
output:
```text
{'c', 'python'}
```
或
```text
{'python', 'c'}
```

注意：如果不存在要删除的项目，remove()将引发错误。
```text
thisset = {"c", "java", "python"}
thisset.remove("java")
print(thisset)
thisset.remove("java")
```
报错：
```text
python-BaseException

KeyError: 'java'
```

例如：

使用discard()方法删除"python"：
```text
thisset = {"c", "java", "python"}
thisset.discard("java")
print(thisset)
thisset.discard("java")
```
output:
```text
{'c', 'python'}
```
或
```text
{'python', 'c'}
```
注意：如果不存在要删除的项目，discard()将不引发错误。

您还可以使用pop()方法来删除一个项目，但是这个方法将删除最后的item。
请记住，集合是无序的，因此您将不知道要删除的项是什么。

pop()方法的返回值是已删除的项目。

例如：

使用pop()方法删除最后一项：
```text
thisset = {"c", "java", "python"}
x = thisset.pop()

print(x)
print(thisset)
```
output:
```text
java
{'c', 'python'}
```
或
```text
python
{'java', 'c'}
```
注意：集合是无序的，因此，当使用pop()方法时，您将不知道要删除的项目，所以多次执行后返回的结果会有不同的可能性。

例如：

clear()方法清空集合：
```text
thisset = {"c", "java", "python"}
thisset.clear()
print(thisset)
```
output:
```text
set()
```

例如：

del关键字将完全删除该集合：
```text
thisset = {"c", "java", "python"}
del thisset
```
del关键字删除的变量不能再使用，否则会报错。
```text
thisset = {"c", "java", "python"}
del thisset
print(thisset)
```
报错：
```text
NameError: name 'thisset' is not defined
```

## 8、连接两个集合
有几种方法可以在Python中连接两个或多个集合。

可以使用union()方法返回包含两个集合中所有项的新集合，或使用update()方法将一个集合中的所有项插入到另一个集合中:

例如：

**union()方法**返回一个新集合，其中包含两个集合中的所有项目：
```text
set1 = {"a", "b" , "c"}
set2 = {1, 2, 3}

set3 = set1.union(set2)
print(set3)
```
output:（每次的输出可能会不同，因为集合是无序的）
```text
{'c', 'b', 1, 2, 3, 'a'}
```

例如：

**update()方法**将set2中的项插入到set1中:
```text
set1 = {"a", "b" , "c"}
set2 = {1, 2, 3}

set1.update(set2)
print(set1)
```
output:（每次的输出可能会不同，因为集合是无序的）
```text
{1, 2, 'c', 3, 'b', 'a'}
```

注意：union()和update()都将排除所有重复项。

还有其他方法将两个集合连接在一起，并且仅保留重复项，或者永不重复，请查看此页面底部的set方法的完整列表。

## 9、set()集合构造函数
也可以使用set()构造函数进行设置。

例如：

使用**set()构造函数**创建一个集合:
```text
thisset = set(("c", "java", "python")) # 请注意双括号
print(thisset)
```
output:（每次的输出可能会不同，因为集合是无序的）
```text
{'java', 'c', 'python'}
```

## 10、set集合方法
Python有一组内置方法，可在集合上使用。

| 方法                            | 描述                                 |
|-------------------------------|------------------------------------|
| add()                         | 将元素添加到集合中                          |
| clear()                       | 从集合中删除所有元素                         |
| copy()                        | 返回集合的副本                            |
| difference()                  | a.difference(b): 返回a集合中不在b集合的元素的集合 |
| difference_update()           | 删除此集合中还包含在另一个指定集合中的项目              |
| discard()                     | 删除指定的项目                            |
| intersection()                | 返回一个集合，即另外两个集合的交集                  |
| intersection_update()         | 删除此集合中其他指定集合中不存在的项目                |
| isdisjoint()                  | 返回两个集合是否相交                         |
| issubset()                    | 返回另一个集合是否包含此集合                     |
| issuperset()                  | 返回此集合是否包含另一个集合                     |
| pop()                         | 从集合中删除一个元素                         |
| remove()                      | 删除指定的元素                            |
| symmetric_difference()        | 返回具有两组对称差的一组                       |
| symmetric_difference_update() | 插入此集合和另一个中的对称差                     |
| union()                       | 返回一个包含集合并集的集合                      |
| update()                      | 使用此集合和其他集合的并集更新集合                  |

