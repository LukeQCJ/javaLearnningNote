# Python 字典(dict)

字典是另一种可变容器模型，且可存储任意类型对象。
字典的每个键值 key=>value 对用冒号 : 分割，每个对之间用逗号(,)分割。
本文主要介绍一下 Python 字典(dict)的使用。

## 1、字典(dict)

字典是无序，可变和索引的集合。在 Python中，字典用大括号括起来，并且具有键和值。

例如：

创建并打印字典：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
print(thisdict)
```
output:
```text
{'name': 'cjavapy', 'age': 3, 'gender': 'man'}
```

## 2、访问字典里的值
可以通过在方括号内引用其键名来访问字典的各项：

例如：

获取key为“name”的值:
```text
x = thisdict["name"]
```

还有一个称为get()的方法，它将为您提供相同的结果：

例如：

获取"age”键的值：
```text
x = thisdict.get("age")
```

## 3、改变字典中的值
可以通过参考特定项的key来更改其值：

例如：

将"age"更改为5：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
thisdict["age"] = 5
print(thisdict)
```
output:
```text
{'name': 'cjavapy', 'age': 5, 'gender': 'man'}
```

## 4、遍历字典
可以使用for循环一个字典。

当遍历字典时，返回值是字典的键，但是也有一些方法可以返回值。

例如：

逐一打印字典中的所有键名称：
```text
for x in thisdict:
    print(x)
```
output:
```text
name
age
gender
```

例如：

逐一打印字典中的所有值：
```text
for x in thisdict:
    print(thisdict[x])
```
output:
```text
cjavapy
5
man
```

例如：

也可以使用values()方法来返回字典的值:
```text
for x in thisdict.values():
    print(x)
```
output:
```text
cjavapy
5
man
```

例如：

使用items()方法循环遍历键和值：
```text
for x, y in thisdict.items():
    print(x, y)
```
output:
```text
name cjavapy
age 5
gender man
```

## 5、判断key是否存在
要确定字典中是否存在指定的键，请使用in关键字：

例如：

判断字典中是否存在"name"：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
if "name" in thisdict:
    print("'name'存在字典中")
```
output:
```text
'name'存在字典中
```

## 6、字典的长度(len())
要确定字典中有多少项（键值对），使用len()函数。

例如：

打印输出字典中的项目数：
```text
print(len(thisdict))
```
output:
```text
3
```

## 7、向字典添加项目元素
通过使用新的索引键并为其分配值，可以向字典中添加项目：

例如：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
thisdict["address"] = "web"
print(thisdict)
```
output:
```text
{'name': 'cjavapy', 'age': 3, 'gender': 'man', 'address': 'web'}
```

## 8、删除字典中项目元素
有几种方法可以从字典中删除项目：

例如：

**pop()方法**移除具有指定key的项:
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
thisdict.pop("age")
print(thisdict)
```
output:
```text
{'name': 'cjavapy', 'gender': 'man'}
```

例如：

**popitem()方法**删除最后插入的项（在3.7之前的版本中，将删除随机项）：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
thisdict.popitem()
print(thisdict)
```
output:
```text
{'name': 'cjavapy', 'age': 3}
```

例如：

**del关键字**删除具有指定键名的项目：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender":"man"
}
del thisdict["age"]
print(thisdict)
```
output:
```text
{'name': 'cjavapy', 'gender': 'man'}
```

例如：

**del关键字**也可以完全删除字典：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
del thisdict
# print(thisdict) #这将导致一个错误，因为“thisdict”不再存在。
```

例如：

**clear()方法**清空字典：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
thisdict.clear()
print(thisdict)
```
output:
```text
{}
```

## 9、复制一个字典
不能简单地通过输入dict2 = dict1来复制字典，
因为dict2将仅是对dict1的引用，对dict1所做的更改也将自动被改为indict2。

有很多方法可以制作副本，一种方法是使用内置的Dictionary方法copy()。

例如：

使用**copy()方法**制作字典的副本：
```text
this_dict = {
    "name": "cjavapy",
    "age": 3,
    "gender": "man"
}
my_dict = this_dict.copy()
this_dict["name"] = "c#"
print(this_dict)
print(my_dict)
```
output:
```text
{'name': 'c#', 'age': 3, 'gender': 'man'}
{'name': 'cjavapy', 'age': 3, 'gender': 'man'}
```

制作副本的另一种方法是使用内置函数dict()。

例如：

使用**dict()函数**复制字典：
```text
thisdict = {
"name": "cjavapy",
"age": 3,
"gender": "man"
}
mydict = dict(thisdict)
print(mydict)
```
output:
```text
{'name': 'c#', 'age': 3, 'gender': 'man'}
{'name': 'cjavapy', 'age': 3, 'gender': 'man'}
```

## 10、嵌套的字典
字典还可以包含许多字典，这称为嵌套字典。

例如：

创建一个包含三个词典的字典：
```text
my_family = {
    "child1": {
        "name": "Emil",
        "year": 2004
    },
    "child2": {
        "name": "Tobias",
        "year": 2007
    },
    "child3": {
        "name": "Linus",
        "year": 2011
    }
}
```

或者，如果想嵌套三个已经作为字典存在的字典：

例如：

创建三个字典，然后创建一个包含其他三个字典的字典：
```text
child1 = {
    "name": "Emil",
    "year": 2004
}
child2 = {
    "name": "Tobias",
    "year": 2007
}
child3 = {
    "name": "Linus",
    "year": 2011
}

my_family = {
    "child1": child1,
    "child2": child2,
    "child3": child3
}
```

## 11、dict() 构造函数
也可以使用dict()构造函数创建一个新字典：

例如：
```text
this_dict = dict(brand="Ford", model="Mustang", year=1964)
# 注意，关键字不是字符串
# 注意，赋值时使用的是等号而不是冒号
print(this_dict)
```
output:
```text
{'brand': 'Ford', 'model': 'Mustang', 'year': 1964}
```

## 12、字典方法
Python具有一组可用于词典的内置方法。

| 方法           | 描述                          |
|--------------|-----------------------------|
| clear()      | 从字典中删除所有元素                  |
| copy()       | 返回字典的副本                     |
| fromkeys()   | 返回具有指定键和值的字典                |
| get()        | 返回指定键的值                     |
| items()      | 返回一个列表，其中包含每个键值对的元组         |
| keys()       | 返回包含字典键的列表                  |
| pop()        | 删除具有指定键的元素                  |
| popitem()    | 删除最后插入的键值对                  |
| setdefault() | 返回指定键的值。 如果密钥不存在：插入具有指定值的密钥 |
| update()     | 使用指定的键值对更新字典                |
| values()     | 返回字典中所有值的列表                 |