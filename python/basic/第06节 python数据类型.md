# Python 数据类型

本文主要介绍Python中的数据类型，包括内置数据类型、获取数据类型、设置数据类型、设置特定的数据类型，
以及相关的示例代码。

## 1、内置数据类型
使用变量存储数据时，为了更充分利用内存空间，我们可以为变量指定不同的数据类型。
并且不同类型可以执行不同的操作。Python默认具有以下内置数据类型：

字符串类型：str

数值类型： int，float， complex

序列类型： list，tuple， range

映射类型： dict

集合类型： set， frozenset

布尔值类型： bool

二进制类型： bytes，bytearray， memoryview

## 2、使用type()获取变量的数据类型
可以使用以下type()函数获取任何对象的数据类型：

例子，

打印输出变量x的数据类型：
```text
x = 5
print(type(x))
```
结果：
```text
<class 'int'>
```

以下是Python中所有内置数据类型：

例如：
```text
print(type("Hello"))
print(type(3))
print(type(3.14))
print(type(1j))
print(type(["c", "java", "python"]))
print(type(("c", "java", "python")))
print(type(range(6)))
print(type({"name" : "python", "age" : 12}))
print(type({"c", "java", "python"}))
print(type(frozenset({"c", "java", "python"})))
print(type(True))
print(type(b"Hello"))
print(type(bytearray(5)))
print(type(memoryview(bytes(5))))
```
结果：
```text
<class 'str'>
<class 'int'>
<class 'float'>
<class 'complex'>
<class 'list'>
<class 'tuple'>
<class 'range'>
<class 'dict'>
<class 'set'>
<class 'frozenset'>
<class 'bool'>
<class 'bytes'>
<class 'bytearray'>
<class 'memoryview'>
```

## 3、指定变量数据类型
在Python中，当将值分配给变量时，将设置数据类型：

| 示例                                     | 数据类型       |
|----------------------------------------|------------|
| x = "Hello World"                      | str        |
| x = 20                                 | int        |
| x = 20.5                               | float      |
| x = 1j                                 | complex    |
| x = ["c", "python", "java"]            | list       |
| x = ("c", "python", "java")            | tuple      |
| x = range(6)                           | range      |
| x = {"name" : "python", "age" : 20}    | dict       |
| x = {"c", "python", "java"}            | set        |
| x = frozenset({"c", "python", "java"}) | frozenset  |
| x = True                               | bool       |
| x = b"Hello"                           | bytes      |
| x = bytearray(5)                       | bytearray  |
| x = memoryview(bytes(5))               | memoryview |

## 4、通过构造函数指定数据类型
如果要指定数据类型，则可以使用以下构造函数：

| 示例                                     | 数据类型       |
|----------------------------------------|------------|
| x = str("Hello World")                 | str        |
| x = int(20)                            | int        |
| x = float(20.5)                        | float      |
| x = complex(1j)                        | complex    |
| x = list(("c", "python", "java"))      | list       |
| x = tuple(("c", "python", "java"))     | tuple      |
| x = range(6)                           | range      |
| x = dict(name="cjavapy", age=3)        | dict       |
| x = set(("c", "java", "python"))       | set        |
| x = frozenset(("c", "python", "java")) | frozenset  |
| x = bool(5)                            | bool       |
| x = bytes(5)                           | bytes      |
| x = bytearray(5)                       | bytearray  |
| x = memoryview(bytes(5))               | memoryview |

## 5、Python常用数据类型示例
```text
shoplist = ['apple', 'mango', 'carrot', 'banana']

print('I have ', len(shoplist), ' items to purchase.')
print('These items are: ', end = '')
for  item in shoplist:
    print(item, end = ' ')

print('\nI also have to buy rice.')
shoplist.append('rice')
print('My shopping list is now ', shoplist)

print('I will sort my list now')
shoplist.sort()
print('Sorted shopping list is ', shoplist)

print('The first item I will buy is ', shoplist[0])
olditem = shoplist[0]
del shoplist[0]
print('I bought the ', olditem)
print('My shopping list is now ', shoplist)

zoo = ('python', 'elephant', 'penguin')
print('Number of animals in the zoo is ', len(zoo))

new_zoo = 'monkey', 'camel', zoo
print('Number of cages in the new zoo is ', len(new_zoo))
print('All animals in new zoo are ',new_zoo)
print('Animals brought from old zoo are', new_zoo[2])
print('Last animal brought from old zoo is ', new_zoo[2][2])
print('Number of animals in the new zoo is ', len(new_zoo) - 1 + len(new_zoo[2]))

ab = {  'Swaroop'    : 'Swaroop@Swaroopch.com',
'Larry'        : 'larry@wall.org',
'Matsumoto'    : 'matz@ruby-lang.org',
'Spammer'    : 'spammer@hotmail.com'
}

print("Swaroop's address is ", ab['Swaroop'])

del ab['Spammer']

print('\nThere are {0} contacts in the address-book\n'.format(len(ab)))

for name, address in ab.items():
    print('Contatc {0} at {1}'.format(name, address))

ab['Guido'] = 'guido@python.org'

if 'Guido' in ab:
    print("\nGuido's address is ", ab['Guido'])

name = 'Swaroop'

print('Item 0 is ', shoplist[0])
print('Item 1 is ', shoplist[1])
print('Item 2 is ', shoplist[2])
print('Item 3 is ', shoplist[3])
print('Item -1 is ', shoplist[-1])
print('Item -2 is ', shoplist[-2])
print('Character 0 is ', name[0])

print('Item 1 to 3 is ',shoplist[1:3])
print('Item 2 to end is ',shoplist[2:])
print('Item 1 to -1 is ',shoplist[1:-1])
print('Item start to end is ',shoplist[:])

print('Character 1 to 3 is ', name[1:3])
print('Character 2 to end is ', name[2:])
print('Character 1 to -1 is ', name[1:-1])
print('Character start to end is ', name[:])

bri = set(['brazil', 'russia', 'India', 'China'])
print('India' in bri)
```