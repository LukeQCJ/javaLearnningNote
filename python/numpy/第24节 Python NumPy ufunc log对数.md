# Python NumPy ufunc log 对数

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy ufunc log 对数。

## 1、对数
NumPy提供了以2、e和10为底执行log的函数。

我们还将探讨如何通过创建自定义ufunc获取任何基础的对数。

如果无法计算对数，则所有log功能都会在元素中放置-inf或inf。

## 2、以2为底的对数
使用log2()函数返回以2为底的对数。

例如：

在以下数组的所有元素的以2为底的对数：
```text
import numpy as np

arr = np.arange(1, 10)

print(np.log2(arr))
```
output:
```text
[0.         1.         1.5849625  2.         2.32192809 2.5849625 2.80735492 3.         3.169925  ]
```

注意：arange(1，10)函数返回一个数组，该数组的整数从1（包括）到10（不包括）开始。

## 3、以10为底的对数
使用log10()函数返回以10为底的对数。

例如：

在以下数组的所有元素的以10为底的对数：
```text
import numpy as np

arr = np.arange(1, 10)

print(np.log10(arr))
```
output:
```text
[0.         0.30103    0.47712125 0.60205999 0.69897    0.77815125 0.84509804 0.90308999 0.95424251]
```

## 4、以自然对数为底的对数
使用log()函数返回以e为底的对数。

例如：

在以下数组的所有元素的以e为底的对数：
```text
import numpy as np

arr = np.arange(1, 10)

print(np.log(arr))
```
output:
```text
[0.         0.69314718 1.09861229 1.38629436 1.60943791 1.79175947 1.94591015 2.07944154 2.19722458]
```

## 5、以任意数为底的对数
NumPy不提供任何函数可以在任何底上获取对数，
因此我们可以将frompyfunc()函数与内置函数math.log()和两个输入参数一起使用，一个输出参数：

例如：
```text
from math import log
import numpy as np

np_log = np.frompyfunc(log, 2, 1)

print(np_log(100, 10))
```
output:
```text
2.0
```
