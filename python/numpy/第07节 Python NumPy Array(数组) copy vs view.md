# Python NumPy Array(数组) copy vs view

NumPy（Numerical Python的缩写）是一个开源的Python**科学计算库**。
使用NumPy，就可以很自然地使用**数组**和**矩阵**。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy Array(数组) Copy和View。

## 1、copy 和 view的区别

copy和view之间的主要区别是copy是一个**新数组**，而该view只是**原始数组的view**。

copy拥有数据，对copy所做的任何更改都不会影响原始数组，对原始数组所做的任何更改也不会影响copy。

该view不拥有数据，对该view所做的任何更改都会影响原始数组，而对原始数组所做的任何更改都会影响该view。

## 2、copy
例如：

制作副本，更改原始数组，然后显示两个数组：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5])
x = arr.copy()
arr[0] = 42

print(arr)
print(x)
```
output:
```text
[42  2  3  4  5]
[1 2 3 4 5]
```

副本不应受到对原始阵列所做更改的影响。

## 3、view
例如：

进行查看，更改原始数组，然后显示两个数组：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5])
x = arr.view()
arr[0] = 42

print(arr)
print(x)
```
output:
```text
[42  2  3  4  5]
[42  2  3  4  5]
```

视图应该受到对原始数组所做更改的影响。

在VIEW中进行更改：

例如：

创建view，更改view，并显示两个数组：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5])
x = arr.view()
x[0] = 31

print(arr)
print(x)
```
output:
```text
[31  2  3  4  5]
[31  2  3  4  5]
```

原始数组应该受到对view所做更改的影响。

## 4、判断数组是否拥有数据
每个NumPy数组都具有属性base，如果该数组拥有数据，则该属性返回None。

否则，base属性引用原始对象。

例如：

打印base属性的值以检查数组是否拥有其数据：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5])

x = arr.copy()
y = arr.view()

print(x.base)
print(y.base)
```
output:
```text
None
[1 2 3 4 5]
```

copy返回None。
view返回原始数组。