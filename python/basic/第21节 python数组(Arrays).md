# Python 数组(Arrays)

数组（Array）是有序的元素序列。 
若将有限个【类型相同】的变量的集合命名，那么这个名称为数组名。
组成数组的各个变量称为数组的分量，也称为数组的元素，有时也称为下标变量。
用于区分数组的各个元素的数字编号称为下标。

数组是在程序设计中，为了处理方便，把具有相同类型的若干元素按有序的形式组织起来的一种形式。 
这些有序排列的同类数据元素的集合称为数组。 

**Python不具有对数组的内置支持，但是可以使用Python列表代替**。


注意：Python不具有对数组的内置支持，但是可以使用Python列表代替。

## 1、数组
注意：此页面向展示如何将列表用作数组，但是，要在Python中使用数组，将必须导入一个库，例如，NumPy库。

数组用于在一个变量中存储多个值:

例如：

创建一个包含编程语言名称的数组：
```text
langs = ["c", "python", "java"]
```

## 2、什么是数组?
数组是一个特殊变量，一次可以存储多个值。

如果有item列表（例如，编程语言名称列表），则将编程语言名称存储在单个变量中可能如下所示：
```text
l1 = "c"
l2 = "java"
l3 = "python"
```

一个数组可以在一个名称下保存多个值，可以通过引用索引号来访问这些值。

## 3、访问数组中的元素
通过引用索引号来引用数组元素。

例如：

获取第一个数组项的值：
```text
x = langs[0]
```

例如：

修改第一个数组项的值：
```text
langs[0] = "cjavapy"
```

## 4、数组的长度
使用len()方法返回数组的长度（数组中元素的数量）。

例如：

返回langs数组中的元素数：
```text
x = len(langs)
```

注意: 数组的长度总是比数组的最大下标多1。

## 5、遍历数组元素
可以使用for in循环遍历数组的所有元素。

例如：

打印输出langs数组中的每个元素：
```text
for x in langs:
    print(x)
```

## 6、添加数组元素
可以使用append()方法将元素添加到数组中。

例如：

在langs数组中再添加一个元素：
```text
langs.append("cjavapy")
```

7、删除数组元素
可以使用pop()方法从数组中删除一个元素。

例如：

删除langs数组的第二个元素：
```text
langs.pop(1)
```

还可以使用remove()方法从数组中删除一个元素。

例如：

删除值为“ python”的元素：
```text
langs.remove("python")
```

注意：列表的remove()方法仅删除指定值的第一次出现。

## 8、数组的方法
Python有一组内置方法，可用于列表/数组。

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
| remove()  | 删除具有指定值的第一项              |
| reverse() | 颠倒列表的顺序                  |
| sort()    | 排序列表                     |

注意： Python不具有对数组的内置支持，但是可以使用 Python列表代替。