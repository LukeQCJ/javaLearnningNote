# Python NumPy ufunc sum 求和

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy ufunc sum 求和。

## 1、sum
sum和add有什么区别？

在两个参数之间进行add，而sum发生在n个元素上。

例如：

将arr1中的值添加到arr2中的值：
```text
import numpy as np

arr1 = np.array([1, 2, 3])
arr2 = np.array([1, 2, 3])

newarr = np.add(arr1, arr2)

print(newarr)
```

返回：[2 4 6]

例如：

将arr1中的值与arr2中的值相加:
```text
import numpy as np

arr1 = np.array([1, 2, 3])
arr2 = np.array([1, 2, 3])

newarr = np.sum([arr1, arr2])

print(newarr)
```

返回：12

## 2、对轴求和
如果指定axis = 1，则NumPy将对每个数组中的数字求和。

例如：

在第一个轴上以以下数组进行求和：
```text
import numpy as np

arr1 = np.array([1, 2, 3])
arr2 = np.array([1, 2, 3])

newarr = np.sum([arr1, arr2], axis=1)

print(newarr)
```

返回：[6 6]

## 3、cumsum累计和
累积和表示将数组中的元素部分相加。

例如，[1、2、3、4]的部分和是[1、1+2、1+2+3、1+2+3+4]=[1、3、6、10]。

使用cumsum()函数执行部分求和。

例如：

在以下数组中执行累积求和：
```text
import numpy as np

arr = np.array([1, 2, 3])

newarr = np.cumsum(arr)

print(newarr)
```

返回：[1 3 6]