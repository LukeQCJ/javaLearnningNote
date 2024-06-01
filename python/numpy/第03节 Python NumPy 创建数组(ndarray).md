# Python NumPy 创建数组(ndarray)

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。
本文主要介绍使用Python NumPy 创建数组(ndarray)。

## 1、创建一个NumPy ndarray对象
NumPy用于处理数组。 NumPy中的数组对象称为ndarray。

我们可以使用array()函数创建一个NumPy ndarray对象。

例如：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5])

print(arr)

print(type(arr))
```

type()：此内置的Python函数告诉我们传递给它的对象的类型。像上面的代码一样，它表明arr是numpy.ndarray类型。

要创建一个ndarray，我们可以将一个列表，元组或任何类似数组的对象传递给array()方法，然后它将被转换为一个ndarray：

例如：

使用元组创建一个NumPy数组：
```text
import numpy as np

arr = np.array((1, 2, 3, 4, 5))

print(arr)
```

## 2、数组维数
数组中的维是数组深度的一个级别（嵌套数组）。

嵌套数组：是将数组作为元素的数组。

## 3、0-D Arrays(数组)
0-D数组或标量是数组中的元素。数组中的每个值都是一个0-D数组。

例如：

创建一个值为42的0-D数组
```text
import numpy as np

arr = np.array(42)
print(type(arr))
print(arr)
```
output:
```text
<class 'numpy.ndarray'>
42
```

## 4、1-D Arrays(数组)
以0-D数组作为元素的数组称为一维数组或1-D array。

这些是最常见的基本数组。

例如：

创建一个包含值1,2,3,4,5的一维数组：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5])

print(arr)
```

## 5、2-D Arrays(数组)
以一维数组为元素的数组称为二维数组。

这些通常用于表示**矩阵**或**二阶张量**。

**NumPy有一个专门用于矩阵运算的完整子模块，称为numpy.mat**。

例如：

创建一个二维数组，其中包含两个具有值1,2,3和4,5,6的数组：
```text
import numpy as np

arr = np.array([[1, 2, 3], [4, 5, 6]])

print(arr)
```
output:
````text
[[1 2 3]
 [4 5 6]]
````

## 6、3-D arrays(数组)
以2-D数组(矩阵)作为元素的数组称为3-D数组。

这些通常用于表示三阶张量。

例如：

用两个2-D数组创建一个3-D数组，两个数组都包含两个值分别为1,2,3和4,5,6的数组：
```text
import numpy as np

arr = np.array([[[1, 2, 3], [4, 5, 6]], [[1, 2, 3], [4, 5, 6]]])

print(arr)
```
output:
```text
[[[1 2 3]
  [4 5 6]]

 [[1 2 3]
  [4 5 6]]]
```

## 7、判断是几维数组
NumPy数组提供了ndim属性，该属性返回一个整数，该整数告诉我们数组具有多少维。

例如：

检查数组有多少维：
```text
import numpy as np

a = np.array(42)
b = np.array([1, 2, 3, 4, 5])
c = np.array([[1, 2, 3], [4, 5, 6]])
d = np.array([[[1, 2, 3], [4, 5, 6]], [[1, 2, 3], [4, 5, 6]]])

print(a.ndim)
print(b.ndim)
print(c.ndim)
print(d.ndim)
```
output:
```text
0
1
2
3
```

## 8、高维数组
数组可以具有任意数量的维。

创建数组后，可以使用ndmin参数定义维数。

例如：

创建一个有5维的数组，并验证它有5维:
```text
import numpy as np

arr = np.array([1, 2, 3, 4], ndmin=5)

print(arr)
print('number of dimensions :', arr.ndim)
```
output:
```text
[[[[[1 2 3 4]]]]]
number of dimensions : 5
```
在此数组中，最里面的维度（第5个dim）具有4个元素，第4个dim具有1个元素作为向量，
第3个dim具有1个元素是与向量的矩阵，第2个dim具有1个元素是3D数组，并且第1个dim具有1个元素，该元素是4D数组。