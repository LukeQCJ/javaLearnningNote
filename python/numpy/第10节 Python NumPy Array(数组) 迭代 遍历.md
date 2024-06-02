# Python NumPy Array(数组) 迭代 遍历

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy Array(数组) 迭代 遍历。

## 1、迭代遍历数组
当我们在numpy中处理多维数组时，可以使用python的基本for循环来完成此操作。

如果我们对一维数组进行迭代，它将一一遍历每个元素。

例如：

遍历一维数组的元素：
```text
import numpy as np

arr = np.array([1, 2, 3])

for x in arr:
    print(x)
```
output:
```text
1
2
3
```

## 2、迭代遍历二维数组
在二维数组中，它将遍历所有行。

例如：

遍历下二维数组的元素：
```text
import numpy as np

arr = np.array([[1, 2, 3], [4, 5, 6]])

for x in arr:
    print(x)
```
output:
```text
[1 2 3]
[4 5 6]
```

如果我们迭代一个n-D数组，它将一一遍历第n-1维。

要返回标量的实际值，我们必须迭代每个维中的数组。

例如：

遍历二维数组的每个标量元素:
```text
import numpy as np

arr = np.array([[1, 2, 3], [4, 5, 6]])

for x in arr:
    for y in x:
        print(y)
```
output:
```text
1
2
3
4
5
6
```

## 3、迭代遍历三维数组
在3-D阵列中，它将遍历所有2-D阵列。

例如：

迭代以下3-D数组的元素：
```text
import numpy as np

arr = np.array([[[1, 2, 3], [4, 5, 6]], [[7, 8, 9], [10, 11, 12]]])

for x in arr:
    print(x)
```
output:
```text
[[1 2 3]
 [4 5 6]]
[[ 7  8  9]
 [10 11 12]]
```

为了返回实际的值，标量，我们必须在每个维度中迭代数组。

例如：

迭代到标量：
```text
import numpy as np

arr = np.array([[[1, 2, 3], [4, 5, 6]], [[7, 8, 9], [10, 11, 12]]])

for x in arr:
    for y in x:
        for z in y:
            print(z)
```
output:
```text
1
2
3
4
5
6
7
8
9
10
11
12
```

## 4、使用nditer()迭代数组
函数nditer()是一个有用的函数，可以从非常基本的迭代到非常高级的迭代都可以使用。 
它解决了我们在迭代中面临的一些基本问题，让我们通过示例进行介绍。

在每个标量元素上迭代。

在基本的for循环中，遍历数组的每个标量，我们需要使用nfor循环，对于具有非常大的数组很难编写高维度。

例如：

遍历以下3-D数组：
```text
import numpy as np

arr = np.array([[[1, 2], [3, 4]], [[5, 6], [7, 8]]])

for x in np.nditer(arr):
    print(x)
```
output:
```text
1
2
3
4
5
6
7
8
```

## 5、使用不同数据类型迭代数组
我们可以使用op_dtypes参数，并将其传递给期望的数据类型以在迭代时更改元素的数据类型。

NumPy不会就地更改元素的数据类型（该元素位于数组中），
因此它需要一些其他空间来执行此操作，该额外空间称为**缓冲区**，并且为了在nditer()中启用它，我们传递flags = ['buffered']。

例如：

以字符串形式遍历数组：
```text
import numpy as np

arr = np.array([1, 2, 3])

for x in np.nditer(arr, flags=['buffered'], op_dtypes=['S']):
    print(x)
```
output:
```text
b'1'
b'2'
b'3'
```

## 6、用不同的步长遍历
我们可以使用过滤，然后进行遍历。

例如：

遍历2D数组的每个标量元素，跳过1个元素：
```text
import numpy as np

arr = np.array([[1, 2, 3, 4], [5, 6, 7, 8]])

for x in np.nditer(arr[:, ::2]):
    print(x)
```
output:
```text
1
3
5
7
```

## 7、使用ndenumerate()的枚举迭代
**枚举**是指一个接一个地提及某物的序号。

有时我们在迭代时需要元素的相应索引，对于这些用例，可以使用ndenumerate()方法。

例如：

遍历一维数组元素：
```text
import numpy as np

arr = np.array([1, 2, 3])

for idx, x in np.ndenumerate(arr):
    print(idx, x)
```
output:
```text
(0,) 1
(1,) 2
(2,) 3
```

例如：

遍历2D数组的元素：
```text
import numpy as np

arr = np.array([[1, 2, 3, 4], [5, 6, 7, 8]])

for idx, x in np.ndenumerate(arr):
    print(idx, x)
```
output:
```text
(0, 0) 1
(0, 1) 2
(0, 2) 3
(0, 3) 4
(1, 0) 5
(1, 1) 6
(1, 2) 7
(1, 3) 8
```
