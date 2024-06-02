# Python NumPy ufunc diff 离散差

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy ufunc diff 离散差。

## 1、diff
离散差意味着减去两个连续的元素。

例如，[1,2,3,4]，离散差分为[2-1,3-2,4-3]= [1,1,1]

要找到离散差异，请使用diff()函数。

例如：

计算以下数组的离散差：
```text
import numpy as np

arr = np.array([10, 15, 25, 5])

newarr = np.diff(arr)

print(newarr)
```

返回：[5 10 -20]，因为15-10 = 5、25-15 = 10和5-25 = -20

我们可以通过给参数n重复执行此操作。

例如：

计算以下数组的离散差两次：
```text
import numpy as np

arr = np.array([10, 15, 25, 5])

newarr = np.diff(arr, n=2)

print(newarr)
```

返回：[5 -30]，因为：15-10 = 5、25-15 = 10和5-25 = -20 AND 10-5 = 5和 -20-10 = -30