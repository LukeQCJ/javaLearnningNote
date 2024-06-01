# Python NumPy 数组(Array)切片

NumPy（Numerical Python的缩写）是一个开源的Python科学计算库。
使用NumPy，就可以很自然地使用数组和矩阵。
NumPy包含很多实用的数学函数，涵盖线性代数运算、傅里叶变换和随机数生成等功能。

本文主要介绍Python NumPy 数组(Array)切片。

## 1、数组切片

在Python中，切片是从一个给定索引获取元素到另一个给定索引。

这样传切片而不是索引：[start:end]。

还可以这样定义step：[start:end:step]。

如不传start，则将其视为0

如不传end，则考虑该维度中数组的长度

如不传step，则将其视为1

slice 对象由三个参数组成：start、stop 和 step。start 指定切片的起始位置，stop 指定切片的结束位置，step 指定切片的步长。

例如：

从以下数组中将元素从索引1切片到索引5：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7])

print(arr[1:5])
```
output:
```text
[2 3 4 5]
```

注意：结果包括开始索引，但不包括结束索引。

例如：

将元素从索引4切片到数组的末尾：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7])

print(arr[4:])
```
output:
```text
[5 6 7]
```

例如：

从开始到索引4切片元素（不包括在内）：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7])

print(arr[:4])
```
output:
```text
[1 2 3 4]
```

## 2、负切片
使用减号运算符从头开始引用索引：

例如：

从末尾的索引3切片到末尾的索引1：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7])

print(arr[-3:-1])
```
output:
```text
[5 6]
```

## 3、步长(step)
使用step值确定切片的步长：

例如：

将所有其他元素从索引1返回索引5：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7])

print(arr[1:5:2])
```
output:
```text
[2 4]
```

例如：

返回整个数组中的所有其他元素：
```text
import numpy as np

arr = np.array([1, 2, 3, 4, 5, 6, 7])

print(arr[::2])
```
output:
```text
[1 3 5 7]
```

## 4、二维数组(2-D Array)切片
例如：

从第二个元素开始，对从索引1到索引4的元素进行切片（不包括在内）：
```text
import numpy as np

arr = np.array([[1, 2, 3, 4, 5], [6, 7, 8, 9, 10]])

print(arr[1, 1:4])
```
output:
```text
[7 8 9]
```

注意：请记住，第二个元素的索引为1。

例如：

从这两个元素返回索引2：
```text
import numpy as np

arr = np.array([[1, 2, 3, 4, 5], [6, 7, 8, 9, 10]])

print(arr[0:2, 2])
```
output:
```text
[3 8]
```

例如：

从两个元素切片索引1到索引4（不包括在内），这将返回一个二维数组：
```text
import numpy as np

arr = np.array([[1, 2, 3, 4, 5], [6, 7, 8, 9, 10]])

print(arr[0:2, 1:4])
```
output:
```text
[[2 3 4]
 [7 8 9]]
```

## 5、使用示例
```text
import numpy as np

data = np.array([[1, 2, 3, 4], [5, 6, 7, 8]])
# 访问所有行，从第2个元素开始的所有元素
print(data[:, 1:])
# 输出：[[2, 3, 4], [6, 7, 8]]
# 访问从第2行开始，每行前2个元素
print(data[1:, :2])
# 输出：[[5, 6]]
# 访问第一行所有元素
print(data[0, :])
# 输出：[1 2 3 4]

# 访问第二行第三个元素
print(data[1, 2])
# 输出：7

# 访问第一列所有元素
print(data[:, 0])
# 输出：[1 5]

# 访问前两行所有元素
print(data[:2, :])
# 输出：[[1 2 3 4]
#        [5 6 7 8]]

# 访问从第二行开始的所有元素
print(data[1:])
# 输出：[[5 6 7 8]]

# 访问前两行，从第三个元素开始的所有元素
print(data[:2, 2:])
# 输出：[[3 4]
#        [7 8]]
# 省略号的使用
# 访问整个数组，可以使用省略号 (...) 来表示所有维度。
print(data[...])
# 输出：[[1 2 3 4]
#        [5 6 7 8]]

# 可以使用步长来指定切片的步长。
# 访问每隔一个元素
print(data[::2, ::2])
# 输出：[[1 3]]

# 从第二行开始，每隔两个元素
print(data[1::2, ::2])
# 输出： [5 7]]
```