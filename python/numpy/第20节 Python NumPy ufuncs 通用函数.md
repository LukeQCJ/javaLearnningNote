# Python NumPy ufuncs 通用函数

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python Numpy ufuncs通用函数。

## 1、ufuncs
ufuncs代表“通用函数”，它们是在ndarray对象上运行的NumPy函数。

## 2、使用ufuncs的原因
ufunc用于**在NumPy中实现矢量化**，这比迭代元素要快得多。

它们还提供广播和其他方法，如减少、累积等，这些方法对计算非常有帮助。

ufuncs还接受其他参数，例如：

where布尔数组或条件，用于定义应在何处进行操作。

dtype定义元素的返回类型。

out需要复制返回值的输出数组。

## 3、向量化
将迭代语句转换为基于向量的操作称为**向量化**。

由于现代CPU已针对此类操作进行了优化，因此速度更快。

添加两个列表的元素

```text
list 1：[1、2、3、4]

list 2：[4、5、6、7]
```

一种方法是遍历两个列表，然后对每个元素求和。

例如：

如果没有ufunc，我们可以使用Python内置的zip()方法：
```text
x = [1, 2, 3, 4]
y = [4, 5, 6, 7]
z = []

for i, j in zip(x, y):
    z.append(i + j)
print(z)
```
output:
```text
[5, 7, 9, 11]
```

NumPy为此有一个ufunc，称为add(x，y)，它将产生相同的结果。

例如：

使用ufunc，我们可以使用add()函数：
```text
import numpy as np

x = [1, 2, 3, 4]
y = [4, 5, 6, 7]

z = np.add(x, y)
print(z)
```
output:
```text
[ 5  7  9 11]
```
