# Python NumPy 连接数组(array)

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy 连接数组(array)。

## 1、concatenate连接NumPy数组
连接意味着将两个或多个数组的内容放在单个数组中。

在SQL中，我们基于键联接表，而在NumPy中，我们按轴联接数组。

我们传递了一系列要与轴一起加入concatenate()函数的数组。 如果未显式传递轴，则将其视为0。

例如：

连接两个数组
```text
import numpy as np

arr1 = np.array([1, 2, 3])

arr2 = np.array([4, 5, 6])

arr = np.concatenate((arr1, arr2))

print(arr)
```
output:
```text
[1 2 3 4 5 6]
```

例如：

沿行（axis=1）联接两个二维数组：
```text
import numpy as np

arr1 = np.array([[1, 2], [3, 4]])

arr2 = np.array([[5, 6], [7, 8]])

arr = np.concatenate((arr1, arr2), axis=1)

print(arr)
```
output:
```text
[[1 2 5 6]
 [3 4 7 8]]
```

## 2、使用stack()函数连接数组
stack()与concatenate()相同，唯一的不同是stack()是沿着新轴完成的。

我们可以沿着第二个轴连接两个一维数组，这将导致它们一个放在另一个之上。

我们传递一个要连接到stack()方法的数组序列和axis。如果axis没有显式传递，则将其视为0。

例如：
```text
import numpy as np

arr1 = np.array([1, 2, 3])

arr2 = np.array([4, 5, 6])

arr = np.stack((arr1, arr2), axis=1)

print(arr)
```
output:
```text
[[1 4]
 [2 5]
 [3 6]]
```

## 3、使用hstack()通过行
NumPy提供了一个辅助函数：hstack()沿行堆叠。

例如：
```text
import numpy as np

arr1 = np.array([1, 2, 3])

arr2 = np.array([4, 5, 6])

arr = np.hstack((arr1, arr2))

print(arr)
```
output:
```text
[1 2 3 4 5 6]
```

## 4、使用vstack()通过列
NumPy提供了一个辅助函数：vstack()沿列堆叠。

例如：
```text
import numpy as np

arr1 = np.array([1, 2, 3])

arr2 = np.array([4, 5, 6])

arr = np.vstack((arr1, arr2))  # arr = np.stack((arr1, arr2)) 等价

print(arr)
```
output:
```text
[[1 2 3]
 [4 5 6]]
```

## 5、使用dstack()通过Height(depth)
NumPy提供了一个辅助函数：dstack()沿高度进行堆叠，该高度与深度相同。

例如：
```text
import numpy as np

arr1 = np.array([1, 2, 3])

arr2 = np.array([4, 5, 6])

arr = np.dstack((arr1, arr2))  # arr = np.stack((arr1, arr2), axis=1) 等价

print(arr)
```
output:
```text
[[[1 4]
  [2 5]
  [3 6]]]
```