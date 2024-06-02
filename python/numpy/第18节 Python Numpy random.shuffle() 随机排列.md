# Python Numpy random.shuffle() 随机排列

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。
本文主要介绍Python Numpy random shuffle() 随机排列。

## 1、元素的随机排列
排列是指元素的排列。 例如 [3，2，1]是[1,2,3]的排列，反之亦然。

NumPy Random模块为此提供了两种方法：shuffle()和permutation()。

## 2、乱序排列
随机打乱意味着就地更改元素的排列。 即在数组本身中。

例如：

随机打乱下列数组的元素:
```text
from numpy import random
import numpy as np

arr = np.array([1, 2, 3, 4, 5])

random.shuffle(arr)

print(arr)
```
output:
```text
[2 1 3 5 4]
```

shuffle()方法对原始数组进行更改。

## 3、生成数组的排列
例如：

生成以下数组的元素的随机排列：
```text
from numpy import random
import numpy as np

arr = np.array([1, 2, 3, 4, 5])

print(random.permutation(arr))
```
output:
```text
[5 1 4 2 3]
```

permutation()方法返回一个重新排列的数组（并使原始数组保持不变）。