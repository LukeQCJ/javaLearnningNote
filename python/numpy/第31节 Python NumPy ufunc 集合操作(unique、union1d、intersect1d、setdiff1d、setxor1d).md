# Python NumPy ufunc 集合操作(unique、union1d、intersect1d、setdiff1d、setxor1d)

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy ufunc 集合操作(unique、union1d、intersect1d、setdiff1d、setxor1d)。

## 1、什么是集合
数学上的集合是唯一元素的集合。

集合用于频繁的交、并、差运算。

## 2、在NumPy中创建集合
我们可以使用NumPy的unique()方法从任何数组中查找唯一元素。 创建一个set数组，但请记住set数组只能是一维数组。

例如：

将具有重复元素的以下数组转换为集合：
```text
import numpy as np

arr = np.array([1, 1, 1, 2, 3, 4, 5, 5, 6, 7])

x = np.unique(arr)

print(x)
```
output:
```text
[1 2 3 4 5 6 7]
```

## 3、求并集(union1d)
要查找两个数组的唯一值，请使用union1d()方法。

例如：

计算以下两个集合数组的并集：
```text
import numpy as np

arr1 = np.array([1, 2, 3, 4])
arr2 = np.array([3, 4, 5, 6])

newarr = np.union1d(arr1, arr2)

print(newarr)
```
output:
```text
[1 2 3 4 5 6]
```

## 4、求交集(intersect1d)
若要仅查找两个数组中都存在的值，请使用intersect1d()方法。

例如：

计算以下两个集合数组的交集：
```text
import numpy as np

arr1 = np.array([1, 2, 3, 4])
arr2 = np.array([3, 4, 5, 6])

newarr = np.intersect1d(arr1, arr2, assume_unique=True)

print(newarr)
```
output:
```text
[3 4]
```

注意：intersect1d()方法采用可选参数assume_unique，如果将其设置为True，则可以加快计算速度。处理集合时，应始终将其设置为True。

## 5、求差集(setdiff1d)
只查找第一个集合中没有出现在第二个集合中的值，请使用setdiff1d()方法。

例如：

计算set1与set2的区别:
```text
import numpy as np

set1 = np.array([1, 2, 3, 4])
set2 = np.array([3, 4, 5, 6])

newarr = np.setdiff1d(set1, set2, assume_unique=True)
print(newarr)

newarr = np.setdiff1d(set2, set1, assume_unique=True)
print(newarr)
```
output:
```text
[1 2]
[5 6]
```

注意：setdiff1d()方法采用可选参数assume_unique，如果将其设置为True，则可以加快计算速度。 处理集合时，应始终将其设置为True。

## 6、求对称差集(setxor1d)
若要仅查找两个集合中都不存在的值，请使用setxor1d()方法。

例如：

找到set1和set2的对称差：
```text
import numpy as np

set1 = np.array([1, 2, 3, 4])
set2 = np.array([3, 4, 5, 6])

newarr = np.setxor1d(set1, set2, assume_unique=True)

print(newarr)
```
output:
```text
[1 2 5 6]
```

注意：setxor1d()方法采用可选参数assume_unique，如果将其设置为True，则可以加快计算速度。 处理集合时，应始终将其设置为True。