# Python NumPy 数组形状(array shape)

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy 数组形状(array shape)。

## 1、 array的形状(shape)
数组的形状是每个维中元素的数量。

## 2、获取数组的形状
NumPy数组具有一个名为shape的属性，该属性返回一个元组，每个索引具有相应元素的数量。

例如：

打印二维数组的形状:
```text
import numpy as np

arr = np.array([[1, 2, 3, 4], [5, 6, 7, 8]])

print(arr.shape)
```
output:
```text
(2, 4)
```

上面的示例返回(2，4)，这意味着数组有2个维，每个维有4个元素。

例如：

使用ndmin使用值1,2,3,4的向量创建具有5个维度的数组，并验证最后一个维度的值为4：
```text
import numpy as np

arr = np.array([1, 2, 3, 4], ndmin=5)

print(arr)
print('shape of array :', arr.shape)
```
output:
```text
[[[[[1 2 3 4]]]]]
shape of array : (1, 1, 1, 1, 4)
```

## 3、shape的元组
每个索引处的整数表示对应维度的元素数量。

在上面的例子中，shape元组索引为4的值为4，所以我们可以说第5维(4 + 1)有4个元素。