# Python NumPy ufunc lcm 最小公倍数

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy ufunc lcm 最小公倍数。

## 1、求最小公倍数(lcm)
最小公倍数是这两个数的公倍数的最小数。

例如：

查找以下两个数字的最小公倍数：
```text
import numpy as np

num1 = 4
num2 = 6

x = np.lcm(num1, num2)

print(x)
```

返回：12，因为这是两个数字的最小公倍数（4 * 3 = 12和6 * 2 = 12）。

## 2、在数组中查找最小公倍数
要查找数组中所有值的最低公倍数，可以使用reduce()方法。

reduce()方法将在每个元素上使用ufunc（在本例中为lcm()函数），并将数组缩小一维。

例如：

查找以下数组的值的最小公倍数：
```text
import numpy as np

arr = np.array([3, 6, 9])

x = np.lcm.reduce(arr)

print(x)
```

返回：18，因为这是所有三个数字（3 * 6 = 18、6 * 3 = 18和9 * 2 = 18）的最小公倍数。

例如：

查找所有数组的最小公倍数，其中数组包含从1到10的所有整数：
```text
import numpy as np

arr = np.arange(1, 11)

x = np.lcm.reduce(arr)

print(x)
```
output:
```text
2520
```
