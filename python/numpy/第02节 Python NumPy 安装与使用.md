# Python NumPy 安装与使用

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍一下Python NumPy 安装与使用。

## 1、安装NumPy
如果您已经在系统上安装了Python和PIP，则安装NumPy非常容易。

使用以下命令安装:
```text
C:\Users\cjavapy>pip install numpy
```

如果此命令失败，则使用已经安装了NumPy的python发行版，例如Anaconda，Spyder等。

## 2、导入NumPy（Import NumPy）
安装NumPy后，通过添加import关键字将其导入您的应用程序：
```text
import numpy
```

现在，NumPy已导入并可以使用。

例如：
```text
import numpy

arr = numpy.array([1, 2, 3, 4, 5])
print(type(arr))
print(arr)

lst = list((2, 5, 8, 9))
print(type(lst))
print(lst)
```
output:
```text
<class 'numpy.ndarray'>
[1 2 3 4 5]
<class 'list'>
[2, 5, 8, 9]
```

## 3、指定导入的NumPy的别名(NumPy as np)
NumPy通常在np别名下导入。

别名(alias)：在Python中，别名是用于指代同一事物的替代名称。

导入时，使用as关键字创建别名：
```text
import numpy as np
```

现在，NumPy包可以称为np而不是numpy。

例如：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5])

print(arr)
```

## 4、判断NumPy版本
版本字符串存储在__ version __属性下。

例如：
```text
import numpy as np

print(np.__version__)
```
