# Python NumPy prod cumprod 乘积

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy prod cumprod 乘积。

## 1、prod
要查找数组中元素的乘积，请使用prod()函数。

例如：

查找此数组的元素的乘积：
```text
import numpy as np

arr = np.array([1, 2, 3, 4])

x = np.prod(arr)

print(x)
```

返回:24 因为 1*2*3*4 = 24

例如：

查找两个数组的元素的乘积：
```text
import numpy as np

arr1 = np.array([1, 2, 3, 4])
arr2 = np.array([5, 6, 7, 8])

x = np.prod([arr1, arr2])

print(x)
```

返回:40320 因为 1*2*3*4*5*6*7*8 = 40320

## 2、对轴求和乘积
如果指定axis = 1，则NumPy将返回每个数组的乘积。

例如：

在第一个轴上以以下数组进行求和：
```text
import numpy as np

arr1 = np.array([1, 2, 3, 4])
arr2 = np.array([5, 6, 7, 8])

newarr = np.prod([arr1, arr2], axis=1)

print(newarr)
```

返回：[24 1680]

## 3、累积乘积
累积乘积是指将乘积部分取用。

例如，[1、2、3、4]的部分积是[1、1*2、1*2*3、1*2*3*4]=[1、2、6、24]

使用cumprod()函数执行累积乘积。

例如：

取以下数组的所有元素的累积乘积：
```text
import numpy as np

arr = np.array([5, 6, 7, 8])

newarr = np.cumprod(arr)

print(newarr)
```

返回:[5 30 210 1680]