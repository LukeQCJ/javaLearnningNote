# Python NumPy 数组 排序

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy 数组排序。

## 1、数组排序
NumPy ndarray对象具有一个名为sort()的函数，该函数将对指定的数组进行排序。

例如：

排序数组：
```text
import numpy as np

arr = np.array([3, 2, 0, 1])

print(np.sort(arr))
```
output:
```text
[0 1 2 3]
```

注意：此方法返回数组的副本，而原始数组保持不变。

还可以对字符串数组或任何其他数据类型进行排序：

例如：

按字母顺序对数组排序：
```text
import numpy as np

arr = np.array(['banana', 'cherry', 'apple'])

print(np.sort(arr))
```
output:
```text
['apple' 'banana' 'cherry']
```

例如：

对布尔数组进行排序：
```text
import numpy as np

arr = np.array([True, False, True])

print(np.sort(arr))
```
output:
```text
[False  True  True]
```

## 2、排序 2-D Array
如果在二维数组上使用sort()方法，则两个数组都将被排序：

例如：

排序二维数组：
```text
import numpy as np

arr = np.array([[3, 2, 4], [5, 0, 1]])

print(np.sort(arr))
```
output:
```text
[[2 3 4]
 [0 1 5]]
```
