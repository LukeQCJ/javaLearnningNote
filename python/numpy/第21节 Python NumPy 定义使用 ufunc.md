# Python NumPy 定义使用 ufunc

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。
本文主要介绍Python NumPy 定义使用 ufunc 函数。

## 1、如何创建定义ufunc
要创建自己的ufunc，必须定义一个函数，就像使用Python中的普通函数一样，然后使用frompyfunc()方法将其添加到NumPy ufunc库中。

frompyfunc()方法采用以下参数：
- function- 函数的名称。
- inputs- 输入参数(数组)的数目。
- outputs- 输出数组的数目。

例如：

创建自己的ufunc进行添加：
```text
import numpy as np


def my_add(x, y):
    return x + y


my_add = np.frompyfunc(my_add, 2, 1)

print(my_add([1, 2, 3, 4], [5, 6, 7, 8]))
```
output:
```text
[6 8 10 12]
```

## 2、判断一个函数是否是ufunc
判断函数的类型以检查它是否为ufunc。

ufunc应该返回<class 'numpy.ufunc'>.

例如：

判断函数是否为ufunc：
```text
import numpy as np

print(type(np.add))
```
output:
```text
<class 'numpy.ufunc'>
```

如果它不是ufunc，它会返回另一种类型，就像这个内置的用于连接两个或更多数组的NumPy函数:

例如：

检查另一个函数的类型：concatenate()：
```text
import numpy as np

print(type(np.concatenate))
```
output:
```text
<class 'function'>
```

如果根本无法识别该函数，它将返回错误：

例如：

检查不存在的内容的类型。 这将产生一个错误：
```text
import numpy as np

print(type(np.blahblah))
```
报错：
```text
AttributeError: module 'numpy' has no attribute 'blahblah'
```

要在if语句中测试函数是否为ufunc，请使用numpy.ufunc值（如果将np用作numpy的别名，请使用np.ufunc）：

例如：

使用if语句检查函数是否为ufunc:
```text
import numpy as np

if type(np.add) is np.ufunc:
    print('add is ufunc')
else:
    print('add is not ufunc')
```
output:
```text
add is ufunc
```
