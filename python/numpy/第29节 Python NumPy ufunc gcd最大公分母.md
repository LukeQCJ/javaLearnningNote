# Python NumPy ufunc gcd 最大公分母

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy ufunc gcd 最大公分母。

## 1、求最大公分母(GCD)
GCD（最大公分母），也称为HCF（最高公因数）是最大的数，是这两个数的公因数。

例如：

查找以下两个数字的HCF：
```text
import numpy as np

num1 = 6
num2 = 9

x = np.gcd(num1, num2)

print(x)
```

返回：3，因为这是两个数字均可以除以的最大值（6/3 = 2和9*3 = 3）。

## 2、在数组中查找最大公分母
要查找数组中所有值的最高公因子，可以使用reduce()方法。

reduce()方法将在每个元素上使用ufunc，在这种情况下为gcd()函数，并将数组缩小一维。

例如：

在以下数组中找到所有数字的GCD：
```text
import numpy as np

arr = np.array([20, 8, 32, 36, 16])

x = np.gcd.reduce(arr)

print(x)
```

返回：4，因为这是可以除以所有值的最高数字。